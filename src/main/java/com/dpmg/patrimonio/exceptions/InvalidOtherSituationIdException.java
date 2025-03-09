package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class InvalidOtherSituationIdException extends RuntimeException {
    public InvalidOtherSituationIdException(Long id) {
        super(String.format(Messages.INVALID_OTHER_SITUATION, id));
    }
}
