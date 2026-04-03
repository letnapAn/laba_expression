package org.example.expression;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для Expression.
 */
class ExpressionTest {

    @Test
    void testSimpleAddition() throws ExpressionException {
        Expression expr = new Expression("2 + 3");
        assertEquals(5.0, expr.calc());
    }

    @Test
    void testSimpleMultiplication() throws ExpressionException {
        Expression expr = new Expression("4 * 5");
        assertEquals(20.0, expr.calc());
    }

    @Test
    void testOperatorPrecedence() throws ExpressionException {
        Expression expr = new Expression("2 + 3 * 4");
        assertEquals(14.0, expr.calc()); // 2 + 12 = 14
    }

    @Test
    void testParentheses() throws ExpressionException {
        Expression expr = new Expression("(2 + 3) * 4");
        assertEquals(20.0, expr.calc()); // 5 * 4 = 20
    }

    @Test
    void testComplexExpression() throws ExpressionException {
        Expression expr = new Expression("2 * (3 + 4) - 5");
        assertEquals(9.0, expr.calc()); // 2 * 7 - 5 = 9
    }

    @Test
    void testDecimalNumbers() throws ExpressionException {
        Expression expr = new Expression("3.14 + 2.5");
        assertEquals(5.64, expr.calc(), 0.001);
    }

    @Test
    void testDivisionByZero() {
        Expression expr = new Expression("5 / 0");
        assertThrows(ExpressionException.class, expr::calc);
    }

    @Test
    void testInvalidExpression() {
        Expression expr = new Expression("2 + + 3");
        assertThrows(ExpressionException.class, expr::calc);
    }

    @Test
    void testSinWithAddition() throws ExpressionException {
        Expression expr = new Expression("2 + sin(0)");
        double result = expr.calc();
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    void testCosWithMultiplication() throws ExpressionException {
        Expression expr = new Expression("5 * cos(0)");
        double result = expr.calc();
        assertEquals(5.0, result, 1e-10);
    }

    @Test
    void testSinWithParentheses() throws ExpressionException {
        Expression expr = new Expression("(sin(0) + cos(0)) * 2");
        double result = expr.calc();
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    void testComplexTrigonometricExpression() throws ExpressionException {
        Expression expr = new Expression("sin(0) + cos(7 - 6 / 2 - 2 * (0.5 * 4)) / 2 + 7 * 3");
        double result = expr.calc();
        assertEquals(21.5, result, 1e-10);
    }
    @Test
    void testSimpleLogExpression() throws ExpressionException {
        Expression expr = new Expression("log10(100)+ln(1)+log2(8)");
        double result = expr.calc();
        assertEquals(5, result, 1e-10);
    }
    @Test
    void testComplexLogExpression() throws ExpressionException {
        String pi = Double.toString(Math.PI);
        Expression expr = new Expression("sin("+pi+"/2) + log10(100/2 + 17 - 7 + 80*0.5)");
        double result = expr.calc();
        assertEquals(3, result, 1e-10);
    }
    @Test
    void testComplexMathExpression() throws ExpressionException {
        Expression expr = new Expression("sqrt(17-1/2*3 + 0.5) - abs(9-10)");
        double result = expr.calc();
        assertEquals(3, result, 1e-10);
    }

}