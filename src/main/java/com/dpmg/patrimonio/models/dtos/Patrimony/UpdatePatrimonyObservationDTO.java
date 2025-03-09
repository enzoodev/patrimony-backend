package com.dpmg.patrimonio.models.dtos.Patrimony;

import com.dpmg.patrimonio.models.dtos.shared.SaveDataDTO;
import lombok.Data;

@Data
public class UpdatePatrimonyObservationDTO extends SaveDataDTO {
    private String sala;
    private String observacao;
}
