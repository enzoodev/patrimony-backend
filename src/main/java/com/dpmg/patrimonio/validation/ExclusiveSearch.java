package com.dpmg.patrimonio.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExclusiveSearchValidator.class)
@Documented
public @interface ExclusiveSearch {
    String message() default "Quando {mainField} está preenchido, os demais parâmetros de busca não devem ser informados.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * Nome do campo que, se preenchido, torna os outros inválidos.
     */
    String mainField();

    /**
     * Lista dos campos que devem estar nulos caso o mainField seja informado.
     */
    String[] otherFields();
}
