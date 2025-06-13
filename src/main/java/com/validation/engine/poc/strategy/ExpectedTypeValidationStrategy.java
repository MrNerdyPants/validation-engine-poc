package com.validation.engine.poc.strategy;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;

/**
 * Represents the ExpectedTypeValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.strategy
 * @class ExpectedTypeValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class ExpectedTypeValidationStrategy implements ValidationStrategy {
    @Override
    public boolean canHandle(FieldValidationContext context) {
        return context.getRule().getExpectedType() != null;
    }

    @Override
    public void validate(FieldValidationContext context, ValidationResult result) {
        String expected = context.getRule().getExpectedType();
        Object value = context.getFieldValue();
        boolean valid = switch (expected.toLowerCase()) {
            case "string" -> value instanceof String;
            case "integer" -> value instanceof Integer;
            case "double" -> value instanceof Double;
            case "boolean" -> value instanceof Boolean;
            default -> true;
        };
        if (!valid) {
            result.setValid(false);
            result.getErrors().add("Field type mismatch. Expected: " + expected);
        }
    }
}
