package com.harryseong.books.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by harry on 4/17/17.
 */

public class ISBNValidator implements ConstraintValidator<ISBN, String> {

    private static org.apache.commons.validator.routines.ISBNValidator validator =
            new org.apache.commons.validator.routines.ISBNValidator(); // Validator from commons validator

    public void initialize(ISBN isbn) {
    }

    public boolean isValid(String value, ConstraintValidatorContext ctx) { // The validation logic
        if (value == null || value.trim().equals("")) return true;
        return validator.isValid(value); // We simply delegate in commons validator
    }

}