package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.models.enums.InventorySituationEnum;
import com.dpmg.patrimonio.utils.Messages;

public class CanNotImportInventoryException extends RuntimeException {
    public CanNotImportInventoryException(InventorySituationEnum status) {
        super(String.format(Messages.CAN_NOT_IMPORT_INVENTORY, status));
    }
}
