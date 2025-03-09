package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class CanNotUpdateItemIfInventoryIsNotOpenException extends RuntimeException {
    public CanNotUpdateItemIfInventoryIsNotOpenException() {
        super(Messages.CAN_NOT_UPDATE_ITEM_IF_INVENTORY_IS_NOT_OPEN);
    }
}
