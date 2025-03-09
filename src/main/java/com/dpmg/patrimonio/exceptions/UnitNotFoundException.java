package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class UnitNotFoundException extends RuntimeException {
    public UnitNotFoundException() {
        super(Messages.UNIT_NOT_FOUND);
    }
}
