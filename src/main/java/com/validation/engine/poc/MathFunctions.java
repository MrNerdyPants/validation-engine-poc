package com.validation.engine.poc;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the MathFunctions class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.expression.evaluator.poc.v2
 * @class MathFunctions
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 5/23/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 5/23/2025
 */
public class MathFunctions {
    private Map<String, Object> context;
    private static final JexlEngine jexl = new JexlBuilder().create();

    public static double sum(List<?> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .mapToDouble(v -> Double.parseDouble(v.toString()))
                .sum();
    }

    public static double sumMultiply(List<?> values1, List<?> values2) {
        double sum = 0.0;
        int minSize = Math.min(values1.size(), values2.size());

        for (int i = 0; i < minSize; i++) {
            Object v1 = values1.get(i);
            Object v2 = values2.get(i);

            if (v1 != null && v2 != null) {
                sum += Double.parseDouble(v1.toString()) * Double.parseDouble(v2.toString());
            }
        }

        return sum;
    }

    public static double round(double value) {
        return Math.round(value);
    }

    public static double avg(List<Number> values) {
        return values.stream().mapToDouble(Number::doubleValue).average().orElse(0);
    }


    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public double sumProjection(String projectionExpr) {
        Pattern wildcardPattern = Pattern.compile("([a-zA-Z0-9_.]+)\\[\\*]");
        Matcher matcher = wildcardPattern.matcher(projectionExpr);

        Set<String> wildcardRoots = new HashSet<>();
        while (matcher.find()) {
            wildcardRoots.add(matcher.group(1));
        }

        if (wildcardRoots.size() != 1) {
            throw new IllegalArgumentException("Only one wildcard root allowed in expression: " + wildcardRoots);
        }

        String rootPath = wildcardRoots.iterator().next();
        List<Map<String, Object>> items = resolvePathToList(context, rootPath);

        double sum = 0;
        for (Map<String, Object> item : items) {
            String exprForItem = projectionExpr.replaceAll(Pattern.quote(rootPath + "[*]."), "");
            JexlExpression expr = jexl.createExpression(exprForItem);
            JexlContext itemCtx = new MapContext(item);
            Object result = expr.evaluate(itemCtx);
            sum += (result instanceof Number) ? ((Number) result).doubleValue() : 0;
        }

        return sum;
    }

    private List<Map<String, Object>> resolvePathToList(Map<String, Object> context, String path) {
        String[] parts = path.split("\\.");
        Object current = context;
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
            } else {
                throw new IllegalArgumentException("Invalid path: " + path);
            }
        }
        if (current instanceof List<?>) {
            List<?> list = (List<?>) current;
            if (!list.stream().allMatch(x -> x instanceof Map)) {
                throw new IllegalArgumentException("List at path does not contain objects: " + path);
            }
            return (List<Map<String, Object>>) current;
        } else {
            throw new IllegalArgumentException("Path does not resolve to list: " + path);
        }
    }
}