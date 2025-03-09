package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.models.enums.InventoryControlSituationEnum;
import com.dpmg.patrimonio.utils.Messages;

public class CanNotImportInventoryException extends RuntimeException {
    public CanNotImportInventoryException(InventoryControlSituationEnum status) {
        super(String.format(Messages.CAN_NOT_IMPORT_INVENTORY, status));
    }
}
