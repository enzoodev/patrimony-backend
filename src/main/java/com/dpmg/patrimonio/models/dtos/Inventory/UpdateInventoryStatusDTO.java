package com.dpmg.patrimonio.models.dtos.Inventory;

import com.dpmg.patrimonio.models.dtos.shared.SavePatrimonyDataDTO;
import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import lombok.Data;

@Data
public class UpdateInventoryStatusDTO extends SavePatrimonyDataDTO {
    private InventorySituationEnum status;
}
