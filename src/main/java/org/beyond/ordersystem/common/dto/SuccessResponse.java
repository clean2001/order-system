package org.beyond.ordersystem.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse extends CustomResponse {
    String statusMessage;
    Object result;

    public SuccessResponse(HttpStatus httpStatus, String statusMessage, Object result) {
        super(httpStatus.value());
        this.statusMessage = httpStatus.getReasonPhrase();
        this.result = result;
    }
}
