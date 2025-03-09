package com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation;

import com.dpmg.patrimonio.models.dtos.Patrimony.UnitDTO;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PatrimonyOtherSituationDTO {
    private Long id;
    private PatrimonySituationEnum situacao;
    private Long numeroPatrimonio;
    private String descricao;
    private LocalDateTime createdDate;
    private Boolean situacaoCadastroManual;
    private UnitDTO unidadeResponsavel;
    private UnitDTO unidadeOndeFoiEncontrado;
}
