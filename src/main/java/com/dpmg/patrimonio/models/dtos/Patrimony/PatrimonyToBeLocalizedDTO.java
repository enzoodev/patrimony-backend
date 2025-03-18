package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatrimonyToBeLocalizedDTO {
    private Long id;
    private String descricao;
    private UnitDTO unidadeEncontrado;
    private PatrimonySituationEnum situacaoAtual;
    private PatrimonySituationEnum situacaoFutura;
}
