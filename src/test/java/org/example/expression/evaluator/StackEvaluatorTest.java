package org.example.expression.evaluator;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для StackEvaluator.
 * Тестируют вычисление RPN напрямую (без Tokenizer и Parser).
 */
class StackEvaluatorTest {

    private StackEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new StackEvaluator();
    }

    @Test
    void testEvaluateSingleNumber() throws ExpressionException {
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "42")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(42.0, result);
    }

    @Test
    void testEvaluateSimpleAddition() throws ExpressionException {
        // RPN: 2 3 +
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.PLUS, "+")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(5.0, result);
    }

    @Test
    void testEvaluateSimpleSubtraction() throws ExpressionException {
        // RPN: 10 3 -
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "10"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.MINUS, "-")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(7.0, result);
    }

    @Test
    void testEvaluateSimpleMultiplication() throws ExpressionException {
        // RPN: 4 5 *
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.MULTIPLY, "*")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(20.0, result);
    }

    @Test
    void testEvaluateSimpleDivision() throws ExpressionException {
        // RPN: 20 4 /
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "20"),
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.DIVIDE, "/")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(5.0, result);
    }

    @Test
    void testEvaluateComplexExpression() throws ExpressionException {
        // RPN: 2 3 4 + * 5 -  = 2 * (3 + 4) - 5 = 9
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.MULTIPLY, "*"),
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.MINUS, "-")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(9.0, result);
    }

    @Test
    void testEvaluateDecimalNumbers() throws ExpressionException {
        // RPN: 3.14 2.5 +
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "3.14"),
                new Token(TokenType.NUMBER, "2.5"),
                new Token(TokenType.PLUS, "+")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(5.64, result, 0.001);
    }

    @Test
    void testEvaluateDivisionByZero() {
        // RPN: 5 0 /
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.NUMBER, "0"),
                new Token(TokenType.DIVIDE, "/")
        );

        assertThrows(ExpressionException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    void testEvaluateEmptyRPN() {
        List<Token> rpn = Collections.emptyList();

        assertThrows(ExpressionException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    void testEvaluateInvalidRPNMultipleResults() {
        // RPN: 2 3 + 4  (недостаточно операторов)
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.NUMBER, "4")
        );

        assertThrows(ExpressionException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    void testEvaluateInvalidRPNOperatorFirst() {
        // RPN: + 2 3  (оператор перед операндами)
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3")
        );

        assertThrows(ExpressionException.class, () -> evaluator.evaluate(rpn));
    }

    @Test
    void testEvaluateNegativeResult() throws ExpressionException {
        // RPN: 5 10 - = -5
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "5"),
                new Token(TokenType.NUMBER, "10"),
                new Token(TokenType.MINUS, "-")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(-5.0, result);
    }

    @Test
    void testEvaluateChainOfOperations() throws ExpressionException {
        // RPN: 1 2 + 3 + 4 + = ((1 + 2) + 3) + 4 = 10
        List<Token> rpn = Arrays.asList(
                new Token(TokenType.NUMBER, "1"),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.PLUS, "+")
        );

        double result = evaluator.evaluate(rpn);
        assertEquals(10.0, result);
    }
}