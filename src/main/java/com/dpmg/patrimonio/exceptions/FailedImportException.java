package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class FailedImportException extends RuntimeException {
    public FailedImportException() {
        super(Messages.FAILED_TO_IMPORT_FILE);
    }
}
