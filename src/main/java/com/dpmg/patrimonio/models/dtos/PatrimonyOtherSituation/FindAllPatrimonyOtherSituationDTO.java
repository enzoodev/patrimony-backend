package com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation;

import com.dpmg.patrimonio.models.dtos.shared.FindPatrimonyPaginatedDataDTO;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import com.dpmg.patrimonio.validation.ExclusiveSearch;
import lombok.Data;

@Data
@ExclusiveSearch(
        mainField = "numeroPatrimonio",
        otherFields = { "situacao", "codUnidadeResponsavel", "descricaoItemMaterial" }
)
public class FindAllPatrimonyOtherSituationDTO extends FindPatrimonyPaginatedDataDTO {
    private PatrimonySituationEnum situacao;
    private Long numeroPatrimonio;
    private Long codUnidadeResponsavel;
    private String descricaoItemMaterial;
}
