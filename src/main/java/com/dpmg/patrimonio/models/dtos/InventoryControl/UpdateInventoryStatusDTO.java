package com.dpmg.patrimonio.models.dtos.InventoryControl;

import com.dpmg.patrimonio.models.dtos.shared.SavePatrimonyDataDTO;
import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import lombok.Data;

@Data
public class UpdateInventoryStatusDTO extends SavePatrimonyDataDTO {
    private InventoryControlSituationEnum status;
}
