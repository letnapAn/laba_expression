package org.example.expression.evaluator;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;

import java.util.List;

/**
 * Вычислитель выражений в обратной польской нотации (RPN).
 */
public interface Evaluator {

    /**
     * Вычисляет значение выражения.
     * @param rpn список токенов в постфиксной нотации
     * @return результат вычисления
     * @throws ExpressionException если выражение некорректно
     */
    double evaluate(List<Token> rpn) throws ExpressionException;
}