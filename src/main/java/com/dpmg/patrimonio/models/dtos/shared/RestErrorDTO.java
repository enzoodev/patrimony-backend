package com.dpmg.patrimonio.models.dtos.shared;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RestErrorDTO {
    private Integer status;
    private String message;
    private Instant timestamp;

    public RestErrorDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
    }
}
