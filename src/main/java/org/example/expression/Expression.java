package org.example.expression;

import org.example.expression.evaluator.Evaluator;
import org.example.expression.evaluator.StackEvaluator;
import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Parser;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.Tokenizer;

import java.util.List;

/**
 * Публичный API для вычисления математических выражений.
 *
 * Пример использования:
 * <pre>
 * Expression expr = new Expression("2 + 2 * (3 - 1)");
 * double result = expr.calc(); // 6.0
 * </pre>
 */
public class Expression {

    private final String expression;
    private final Tokenizer tokenizer;
    private final Parser parser;
    private final Evaluator evaluator;

    /**
     * Создает выражение для вычисления.
     * @param expression строка выражения
     */
    public Expression(String expression) {
        this.expression = expression;
        this.tokenizer = new Tokenizer();
        this.parser = new Parser();
        this.evaluator = new StackEvaluator();
    }

    /**
     * Вычисляет значение выражения.
     * @return результат вычисления
     * @throws ExpressionException если выражение некорректно
     */
    public double calc() throws ExpressionException {
        // 1. Токенизация
        List<Token> tokens = tokenizer.tokenize(expression);

        // 2. Парсинг (инфикс → RPN)
        List<Token> rpn = parser.parse(tokens);

        // 3. Вычисление
        return evaluator.evaluate(rpn);
    }
}