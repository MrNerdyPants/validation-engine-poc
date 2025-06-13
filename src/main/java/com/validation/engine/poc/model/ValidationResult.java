package com.validation.engine.poc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the ValidationResult class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.model
 * @class ValidationResult
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
public class ValidationResult {
    private boolean valid = true;
    private List<String> errors = new ArrayList<>();
}
