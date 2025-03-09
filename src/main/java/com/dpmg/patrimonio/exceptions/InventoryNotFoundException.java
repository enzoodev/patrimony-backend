package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException() {
        super(Messages.INVENTORY_NOT_FOUND);
    }
}
