package org.casestudies.dtos;

import org.casestudies.model.Bill;

public class ProcessExitResponseDto extends ResponseDto {
    private Bill bill;

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
}
