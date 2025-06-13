package com.validation.engine.poc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents the ValidationRule class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.expression.evaluator.poc
 * @class ValidationRule
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 5/23/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 5/23/2025
 */

@Data
public
// Validation Rule POJO
class ValidationRule {
    @JsonProperty("is_required")
    private boolean isRequired = false;

    @JsonProperty("expected_type")
    private String expectedType;

    @JsonProperty("regex_pattern")
    private String regexPattern;

    @JsonProperty("min_length")
    private Integer minLength;

    @JsonProperty("max_length")
    private Integer maxLength;

    @JsonProperty("computed_expression")
    private String computedExpression; /// sum > 0

    @JsonProperty("api_endpoint")
    private String apiEndpoint;

}
