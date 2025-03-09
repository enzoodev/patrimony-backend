package com.dpmg.patrimonio.models.dtos.InventoryControl;

import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryControlDTO {
    private Long id;
    private Integer ano;
    private InventoryControlSituationEnum status;
    private String observacao;
}
