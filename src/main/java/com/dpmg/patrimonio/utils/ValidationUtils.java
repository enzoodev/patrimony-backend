package com.dpmg.patrimonio.utils;
import java.util.List;

import lombok.experimental.UtilityClass;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@UtilityClass
public class ValidationUtils {
    public String getFirstErrorMessage(List<ObjectError> objectErrors, List<FieldError> fieldErrors) {
        if (!objectErrors.isEmpty()) {
            return objectErrors.getFirst().getDefaultMessage();
        }

        if (!fieldErrors.isEmpty()) {
            return fieldErrors.getFirst().getDefaultMessage();
        }

        return Messages.GENERIC_VALIDATION_ERROR;
    }
}
