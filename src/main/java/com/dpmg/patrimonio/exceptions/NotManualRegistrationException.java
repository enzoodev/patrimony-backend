package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class NotManualRegistrationException extends RuntimeException {
    public NotManualRegistrationException() {
        super(Messages.NOT_MANUAL_REGISTRATION);
    }
}
