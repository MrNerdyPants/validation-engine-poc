package com.validation.engine.poc.strategy;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;
import org.springframework.stereotype.Service;

/**
 * Represents the RequiredValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.strategy
 * @class RequiredValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
@Service
public class RequiredValidationStrategy implements ValidationStrategy {
    @Override
    public boolean canHandle(FieldValidationContext context) {
        return context.getRule().isRequired();
    }

    @Override
    public void validate(FieldValidationContext context, ValidationResult result) {
        if (context.getFieldValue() == null || context.getFieldValue().toString().trim().isEmpty()) {
            result.setValid(false);
            result.getErrors().add("Field is required");
        }
    }
}
