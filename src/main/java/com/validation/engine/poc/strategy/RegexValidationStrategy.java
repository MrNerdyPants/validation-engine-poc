package com.validation.engine.poc.strategy;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;

import java.util.regex.Pattern;

/**
 * Represents the RegexValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.strategy
 * @class RegexValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class RegexValidationStrategy implements ValidationStrategy {

    @Override
    public boolean canHandle(FieldValidationContext context) {
        return context.getRule().getRegexPattern() != null;
    }

    @Override
    public void validate(FieldValidationContext context, ValidationResult result) {
        Object value = context.getFieldValue();
        if (value == null) return;

        String regex = context.getRule().getRegexPattern();
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(value.toString()).matches()) {
            result.setValid(false);
            result.getErrors().add("Value does not match pattern: " + regex);
        }
    }
}
