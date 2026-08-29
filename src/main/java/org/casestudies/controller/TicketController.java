package org.casestudies.controller;

import org.casestudies.dtos.*;
import org.casestudies.model.Ticket;
import org.casestudies.enums.ResponseStatus;
import org.casestudies.service.TicketService;
import org.casestudies.exception.ParkingLotException;


public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public GenerateTicketResponseDto generateTicket(GenerateTicketRequestDto requestDto) {
        GenerateTicketResponseDto responseDto = new GenerateTicketResponseDto();

        if (requestDto == null) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            responseDto.setErrorMessage("Request cannot be null");
            return responseDto;
        }


        try {
            Ticket ticket = ticketService.generateTicket(requestDto.getVehicle(), requestDto.getEntryGate(), requestDto.getSpotType(), requestDto.getParkingLotId());

            responseDto.setTicket(ticket);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);

        } catch (ParkingLotException exception) {
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
            responseDto.setErrorMessage(exception.getMessage());
        }

        return responseDto;
    }
}