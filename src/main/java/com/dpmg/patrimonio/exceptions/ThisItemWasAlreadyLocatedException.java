package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;

public class ThisItemWasAlreadyLocatedException extends RuntimeException {
    public ThisItemWasAlreadyLocatedException(Long patrimonyNumber) {
        super(String.format(Messages.THIS_ITEM_WAS_ALREADY_LOCATED, patrimonyNumber));
    }
}
