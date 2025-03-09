package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class CanNotUpdateFinishedInventoryException extends RuntimeException {
    public CanNotUpdateFinishedInventoryException(Integer year) {
        super(String.format(Messages.CAN_NOT_UPDATE_FINISHED_INVENTORY, year));
    }
}
