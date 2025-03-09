package com.dpmg.patrimonio.models.dtos.shared;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PaginatedResponseDTO<T> extends ResponseDTO<List<T>> {
    private Long totalRegistros;

    public PaginatedResponseDTO(String mensagem, Long totalRegistros, List<T> dados) {
        super(mensagem, dados);
        this.totalRegistros = totalRegistros;
    }

    public static <T> PaginatedResponseDTO<T> fromPage(String mensagem, Page<T> page) {
        return new PaginatedResponseDTO<>(
                mensagem,
                page.getTotalElements(),
                page.getContent()
        );
    }
}
