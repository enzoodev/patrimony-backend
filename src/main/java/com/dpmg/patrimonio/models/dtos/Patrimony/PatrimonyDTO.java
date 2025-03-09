package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PatrimonyDTO {
    private Long id;
    private PatrimonySituationEnum situacao;
    private Long numeroPatrimonio;
    private String descricao;
    private LocalDateTime createdDate;
    private Boolean patrimonioForaUnidade;
    private UnitDTO unidadeResponsavel;
    private Double valorBemPatrimonial;
}
