package com.validation.engine.poc;

import java.util.List;
import java.util.Map;

/**
 * Represents the FieldPathResolver class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc
 * @class FieldPathResolver
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class FieldPathResolver {
    public static List<Object> resolve(Map<String, Object> payload, String path) {
        // You’ll need to recursively support dot and [*] notations here.
        // Placeholder: returns one value only.
        String[] parts = path.split("\\.");
        Object current = payload;
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                return List.of();
            }
        }
        return current != null ? List.of(current) : List.of();
    }
}
