package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class ImportAlreadyInProgressException extends RuntimeException {
    public ImportAlreadyInProgressException() {
        super(Messages.IMPORT_ALREADY_IN_PROGRESS);
    }
}
