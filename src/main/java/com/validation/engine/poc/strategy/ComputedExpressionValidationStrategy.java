package com.validation.engine.poc.strategy;

import com.validation.engine.poc.ExpressionEvaluator;
import com.validation.engine.poc.MathFunctions;
import com.validation.engine.poc.model.FieldValidationContext;
import com.validation.engine.poc.model.ValidationResult;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;

import java.util.Map;

/**
 * Represents the ComputedExpressionValidationStrategy class in the poc project.
 *
 * @author Kashan Asim
 * @version 1.0
 * @project poc
 * @module com.validation.engine.poc.strategy
 * @class ComputedExpressionValidationStrategy
 * @lastModifiedBy Kashan.Asim
 * @lastModifiedDate 6/13/2025
 * @license Licensed under the Apache License, Version 2.0
 * @description A brief description of the class functionality.
 * @notes <ul>
 * <li>Provide any additional notes or remarks here.</li>
 * </ul>
 * @since 6/13/2025
 */
public class ComputedExpressionValidationStrategy implements ValidationStrategy {
    private final JexlEngine jexlEngine = new JexlBuilder()
            .namespaces(Map.of("math", new MathFunctions()))
            .create();

    @Override
    public boolean canHandle(FieldValidationContext context) {
        return context.getRule().getComputedExpression() != null;
    }

    @Override
    public void validate(FieldValidationContext context, ValidationResult result) {

        String expr = context.getRule().getComputedExpression();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(context.getFullPayload());


        try {
            Object evalResult = evaluator.evaluate(expr, context.getFullPayload());
            if (!(evalResult instanceof Boolean bool) || !bool) {
                result.setValid(false);
                result.getErrors().add("Computed expression failed: " + expr);
            }
        } catch (Exception e) {
            result.setValid(false);
            result.getErrors().add("Expression error: " + e.getMessage());
        }

//        String expr = context.getRule().getComputedExpression();
//        JexlContext jexlContext = new MapContext(context.getFullPayload());
//        try {
//            Object evalResult = jexlEngine.createExpression(expr).evaluate(jexlContext);
//            if (!(evalResult instanceof Boolean bool) || !bool) {
//                result.setValid(false);
//                result.getErrors().add("Computed expression failed: " + expr);
//            }
//        } catch (Exception e) {
//            result.setValid(false);
//            result.getErrors().add("Expression error: " + e.getMessage());
//        }
    }
}
