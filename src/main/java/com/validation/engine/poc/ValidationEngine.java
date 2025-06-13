package com.validation.engine.poc;

import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;
import com.validation.engine.poc.model.ValidationRule;
import com.validation.engine.poc.strategy.ValidationStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the ValidationEngine class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc
 * @class ValidationEngine
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class ValidationEngine {
    private final List<ValidationStrategy> strategies;

    public ValidationEngine(List<ValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Map<String, ValidationResult> validate(Map<String, Object> payload, Map<String, List<ValidationRule>> ruleMap) {
        Map<String, ValidationResult> result = new HashMap<>();
        ruleMap.forEach((fieldPath, rules) -> {
            List<Object> values = FieldPathResolver.resolve(payload, fieldPath);
            for (Object value : values) {
                for (ValidationRule rule : rules) {
                    FieldValidationContext context = new FieldValidationContext();
                    context.setFieldPath(fieldPath);
                    context.setFieldValue(value);
                    context.setFullPayload(payload);
                    context.setRule(rule);

                    ValidationResult fieldResult = result.computeIfAbsent(fieldPath, k -> new ValidationResult());
                    for (ValidationStrategy strategy : strategies) {
                        if (strategy.canHandle(context)) {
                            strategy.validate(context, fieldResult);
                        }
                    }
                }
            }
        });
        return result;
    }
}
