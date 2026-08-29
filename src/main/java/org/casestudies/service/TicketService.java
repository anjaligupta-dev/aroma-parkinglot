package org.casestudies.service;

import org.casestudies.enums.*;
import org.casestudies.model.*;
import org.casestudies.repository.TicketRepository;
import org.casestudies.exception.ParkingLotException;
import org.casestudies.repository.ParkingLotRepository;
import org.casestudies.strategy.SpotAssignmentStrategy;

import java.time.LocalDateTime;


public class TicketService {
    private final TicketRepository ticketRepository;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final ParkingLotRepository parkingLotRepository;

    public TicketService(TicketRepository ticketRepository, SpotAssignmentStrategy spotAssignmentStrategy, ParkingLotRepository parkingLotRepository) {
        this.ticketRepository = ticketRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.parkingLotRepository = parkingLotRepository;
    }

    public Ticket generateTicket(Vehicle vehicle, EntryGate entryGate, SpotType spotType, Long parkingLotId) {
        // Validate vehicle
        if (vehicle == null) {
            throw new ParkingLotException("Vehicle details are required");
        }

        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new ParkingLotException("Vehicle license plate is required");
        }

        if (vehicle.getVehicleType() == null) {
            throw new ParkingLotException("Vehicle type is required");
        }

        // Validate entry gate
        if (entryGate == null) {
            throw new ParkingLotException("Entry gate is required");
        }

        // Validate spot type
        if (spotType == null) {
            throw new ParkingLotException("Spot type is required");
        }

        // Validate parking lot ID
        if (parkingLotId == null) {
            throw new ParkingLotException("Parking lot id is required");
        }


        // Find parking lot
        ParkingLot parkingLot = parkingLotRepository.getById(parkingLotId);

        if (parkingLot == null) {
            throw new ParkingLotException("Parking lot not found");
        }

        // Validate that the entry gate belongs to this parking lot
        if (parkingLot.getEntryGates() == null || !parkingLot.getEntryGates().contains(entryGate)) {
            throw new ParkingLotException("Entry gate does not belong to parking lot");
        }

        // Entry gate must be open
        if (entryGate.getGateStatus() != GateStatus.OPEN) {
            throw new ParkingLotException("Entry gate is closed");
        }


        // Find a suitable parking spot using the configured strategy
        ParkingSpot parkingSpot = spotAssignmentStrategy.assignSpot(parkingLot, spotType);

        if (parkingSpot == null) {
            throw new ParkingLotException("No available parking spot found for requested type");
        }

        // Mark the assigned spot as occupied
        parkingSpot.setParkingSpotStatus(ParkingSpotStatus.OCCUPIED);


        // Create ticket
        Ticket ticket = new Ticket();

        ticket.setVehicle(vehicle);
        ticket.setParkingSpot(parkingSpot);
        ticket.setEntryGate(entryGate);
        ticket.setParkingLot(parkingLot);
        ticket.setEntryTime(LocalDateTime.now());

        // Persist ticket
        return ticketRepository.save(ticket);
    }
}