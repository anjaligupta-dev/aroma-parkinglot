package org.casestudies.dtos;

import org.casestudies.enums.ResponseStatus;

public abstract class ResponseDto {
    private String errorMessage;
    private ResponseStatus responseStatus;

    // Getter Setter for error message
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getter Setter for response status
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
