package com.dpmg.patrimonio.models.dtos.Inventory;

import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    private Integer ano;
    private InventorySituationEnum status;
    private String observacao;
}
