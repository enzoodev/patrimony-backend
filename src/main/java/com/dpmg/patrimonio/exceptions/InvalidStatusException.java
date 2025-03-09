package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException() {
        super(Messages.INVALID_STATUS);
    }
}
