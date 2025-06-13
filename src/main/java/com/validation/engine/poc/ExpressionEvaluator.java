package com.validation.engine.poc;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the ExpressionEvaluator class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.expression.evaluator.poc.v3
 * @class ExpressionEvaluator
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 5/26/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 5/26/2025
 */
public class ExpressionEvaluator {
    private final JexlEngine jexl;
    private final MathFunctions mathFunctions;

    public ExpressionEvaluator() {
        Map<String, Object> funcs = new HashMap<>();
        funcs.put("math", new MathFunctions());
//        jexl = new JexlBuilder().namespaces(funcs).strict(true).silent(false).create();

        jexl = new JexlBuilder().namespaces(funcs).create();
        mathFunctions = null;
    }

    public ExpressionEvaluator(Map<String, Object> data) {
        this.mathFunctions = new MathFunctions();
        mathFunctions.setContext(data);

        Map<String, Object> funcs = new HashMap<>();
        funcs.put("math", mathFunctions);

        jexl = new JexlBuilder().namespaces(funcs).create();
    }

    public Object evaluate(String userExpression, Map<String, Object> data) {

        JexlContext jexlContext = new MapContext(data);
        return jexl.createExpression(userExpression).evaluate(jexlContext);
    }

    private String preprocess(String expr, Map<String, Object> data) {

        if (expr.contains("items[*].")) {
            expr = expr.replaceAll("items\\[\\*\\]\\.([a-zA-Z_][a-zA-Z0-9_]*)", "item['$1']");
            expr = "sum(items, item -> " + expr + ")";
        }

        return expr;
    }

    public static Map<String, Object> flattenItems(List<Map<String, Object>> items) {
        double total = items.stream()
                .mapToDouble(i -> ((Number) i.get("price")).doubleValue() * ((Number) i.get("qty")).doubleValue())
                .sum();

        Map<String, Object> context = new HashMap<>();
        context.put("items", items);
        context.put("total", total); // flattened field for simplified expressions
        return context;
    }

}