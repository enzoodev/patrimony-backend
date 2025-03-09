package com.dpmg.patrimonio.exceptions;

import com.dpmg.patrimonio.utils.Messages;
import com.dpmg.patrimonio.utils.SheetUtils;

public class WrongSheetNameException extends RuntimeException {
    public WrongSheetNameException() {
        super(String.format(Messages.SHEET_INCORRET_NAME, SheetUtils.mainName));
    }
}
