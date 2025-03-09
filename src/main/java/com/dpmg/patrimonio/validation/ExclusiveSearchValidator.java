package com.dpmg.patrimonio.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class ExclusiveSearchValidator implements ConstraintValidator<ExclusiveSearch, Object> {
    private String mainField;
    private String[] otherFields;

    @Override
    public void initialize(ExclusiveSearch constraintAnnotation) {
        this.mainField = constraintAnnotation.mainField();
        this.otherFields = constraintAnnotation.otherFields();
    }

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(dto);
        Object mainValue = wrapper.getPropertyValue(mainField);

        if (mainValue == null) {
            return true;
        }

        boolean valid = true;
        for (String field : otherFields) {
            Object fieldValue = wrapper.getPropertyValue(field);
            if (fieldValue != null) {
                valid = false;
                break;
            }
        }

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Quando '%s' está preenchido, os demais parâmetros (%s) não devem ser informados.",
                            mainField, String.join(", ", otherFields))
            ).addConstraintViolation();
        }

        return valid;
    }
}
