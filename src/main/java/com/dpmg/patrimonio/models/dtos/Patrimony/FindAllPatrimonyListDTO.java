package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.dtos.shared.FindPatrimonyPaginatedDataDTO;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import com.dpmg.patrimonio.validation.ExclusiveSearch;
import lombok.Data;

@Data
@ExclusiveSearch(
        mainField = "numeroPatrimonio",
        otherFields = { "situacao", "codUnidadeResponsavel", "descricaoItemMaterial" }
)
public class FindAllPatrimonyListDTO extends FindPatrimonyPaginatedDataDTO {
    private Long numeroPatrimonio;
    private PatrimonySituationEnum situacao;
    private Long codUnidadeResponsavel;
    private String descricaoItemMaterial;
}
