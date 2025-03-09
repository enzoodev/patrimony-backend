package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class InvalidImportFileException extends RuntimeException {
    public InvalidImportFileException() {
        super(Messages.INVALID_IMPORT_FILE);
    }
}
