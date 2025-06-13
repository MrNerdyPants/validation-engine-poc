package com.validation.engine.poc.strategy;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;

/**
 * Represents the LengthValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.strategy
 * @class LengthValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class LengthValidationStrategy implements ValidationStrategy {

    @Override
    public boolean canHandle(FieldValidationContext context) {
        return context.getRule().getMinLength() != null || context.getRule().getMaxLength() != null;
    }

    @Override
    public void validate(FieldValidationContext context, ValidationResult result) {
        Object value = context.getFieldValue();
        if (value == null) return; // Let RequiredStrategy handle nulls

        String str = value.toString();
        int length = str.length();
        Integer min = context.getRule().getMinLength();
        Integer max = context.getRule().getMaxLength();

        if (min != null && length < min) {
            result.setValid(false);
            result.getErrors().add("Minimum length is " + min + " but got " + length);
        }

        if (max != null && length > max) {
            result.setValid(false);
            result.getErrors().add("Maximum length is " + max + " but got " + length);
        }
    }
}
