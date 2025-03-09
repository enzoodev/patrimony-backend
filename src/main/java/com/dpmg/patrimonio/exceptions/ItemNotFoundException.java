package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super(Messages.ITEM_NOT_FOUND);
    }
}
