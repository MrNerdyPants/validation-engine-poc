package com.validation.engine.poc.strategy;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;

/**
 * Represents the ValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.expression.evaluator.poc.v2
 * @class ValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 5/23/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 5/23/2025
 */
// Strategy Pattern for Validation Rules
public interface ValidationStrategy {
    boolean canHandle(FieldValidationContext context);

    void validate(FieldValidationContext context, ValidationResult result);
}
