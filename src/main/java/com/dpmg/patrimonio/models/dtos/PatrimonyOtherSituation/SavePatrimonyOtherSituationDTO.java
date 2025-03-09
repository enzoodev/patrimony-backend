package com.dpmg.patrimonio.models.dtos.PatrimonyOtherSituation;

import com.dpmg.patrimonio.models.dtos.shared.SavePatrimonyDataDTO;
import com.dpmg.patrimonio.models.enums.PatrimonySituationEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavePatrimonyOtherSituationDTO extends SavePatrimonyDataDTO {
    @NotNull(message = "O parâmetro 'situação' é obrigatória")
    private PatrimonySituationEnum situacao;

    @NotNull(message = "O parâmetro 'numeroPatrimonio' é obrigatório")
    @Positive(message = "O parâmetro 'numeroPatrimonio' deve ser um valor positivo")
    private Long numeroPatrimonio;

    @NotNull(message = "O parâmetro 'codUnidadeResponsavel' é obrigatório")
    @Positive(message = "O parâmetro 'codUnidadeResponsavel' deve ser um valor positivo")
    private Long codUnidadeResponsavel;

    @NotNull(message = "O parâmetro 'descricaoItemMaterial' do item é obrigatória")
    @Size(min = 3, max = 255, message = "O parâmetro 'descricaoItemMaterial' do item deve ter entre 3 e 255 caracteres")
    private String descricaoItemMaterial;
}
