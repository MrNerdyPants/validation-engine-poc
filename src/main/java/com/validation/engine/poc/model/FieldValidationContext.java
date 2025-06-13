package com.validation.engine.poc.model;

import lombok.Data;

import java.util.Map;

/**
 * Represents the FieldValidationContext class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.model
 * @class FieldValidationContext
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
@Data
public class FieldValidationContext {
    private String fieldPath;
    private Object fieldValue;
    private Map<String, Object> fullPayload;
    private ValidationRule rule;
}
