package com.dpmg.patrimonio.models.dtos.shared;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private String mensagem;
    private T dados;
}
