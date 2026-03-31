package org.example.expression.function;

import org.example.expression.ExpressionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для TrigonometricalFunction.
 */
class TrigonometricalFunctionTest {

    private static final double DELTA = 1e-10;

    // ===== Тесты для SIN =====

    @Test
    void testSinZero() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(0.0);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testSinPiOver2() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(Math.PI / 2);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    void testSinPi() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(Math.PI);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testSinThreePiOver2() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(3 * Math.PI / 2);
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    void testSinTwoPi() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(2 * Math.PI);
        assertEquals(0.0, result, DELTA);
    }

    // ===== Тесты для COS =====

    @Test
    void testCosZero() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(0.0);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    void testCosPiOver2() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(Math.PI / 2);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testCosPi() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(Math.PI);
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    void testCosTwoPi() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(2 * Math.PI);
        assertEquals(1.0, result, DELTA);
    }

    // ===== Тесты для TAN =====

    @Test
    void testTanZero() throws ExpressionException {
        TrigonometricalFunction tan = new TrigonometricalFunction(TrigonometricalFunction.Type.TAN);
        double result = tan.apply(0.0);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testTanPiOver4() throws ExpressionException {
        TrigonometricalFunction tan = new TrigonometricalFunction(TrigonometricalFunction.Type.TAN);
        double result = tan.apply(Math.PI / 4);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    void testTanPi() throws ExpressionException {
        TrigonometricalFunction tan = new TrigonometricalFunction(TrigonometricalFunction.Type.TAN);
        double result = tan.apply(Math.PI);
        assertEquals(0.0, result, DELTA);
    }

    // ===== Тесты на ошибки =====

    @Test
    void testApplyWithNoArguments() {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        assertThrows(ExpressionException.class, () -> sin.apply());
    }

    @Test
    void testApplyWithTwoArguments() {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        assertThrows(ExpressionException.class, () -> sin.apply(1.0, 2.0));
    }

    @Test
    void testApplyWithThreeArguments() {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        assertThrows(ExpressionException.class, () -> cos.apply(1.0, 2.0, 3.0));
    }

    // ===== Тесты для fromName() =====

    @Test
    void testFromNameSin() throws ExpressionException {
        TrigonometricalFunction func = TrigonometricalFunction.fromName("sin");
        assertNotNull(func);
        assertEquals(0.0, func.apply(0.0), DELTA);
    }

    @Test
    void testFromNameCos() throws ExpressionException {
        TrigonometricalFunction func = TrigonometricalFunction.fromName("cos");
        assertNotNull(func);
        assertEquals(1.0, func.apply(0.0), DELTA);
    }

    @Test
    void testFromNameTan() throws ExpressionException {
        TrigonometricalFunction func = TrigonometricalFunction.fromName("tan");
        assertNotNull(func);
        assertEquals(0.0, func.apply(0.0), DELTA);
    }

    @Test
    void testFromNameSinUppercase() throws ExpressionException {
        TrigonometricalFunction func = TrigonometricalFunction.fromName("SIN");
        assertNotNull(func);
        assertEquals(1.0, func.apply(Math.PI / 2), DELTA);
    }

    @Test
    void testFromNameCosMixedCase() throws ExpressionException {
        TrigonometricalFunction func = TrigonometricalFunction.fromName("CoS");
        assertNotNull(func);
        assertEquals(0.0, func.apply(Math.PI / 2), DELTA);
    }

    @Test
    void testFromNameUnknownFunction() {
        assertThrows(ExpressionException.class, () ->
                TrigonometricalFunction.fromName("unknown")
        );
    }

    @Test
    void testFromNameEmptyString() {
        assertThrows(ExpressionException.class, () ->
                TrigonometricalFunction.fromName("")
        );
    }

    // ===== Тесты на специальные значения =====

    @Test
    void testSinNegativeAngle() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(-Math.PI / 2);
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    void testCosNegativeAngle() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(-Math.PI);
        assertEquals(-1.0, result, DELTA);
    }

    @Test
    void testSinLargeAngle() throws ExpressionException {
        TrigonometricalFunction sin = new TrigonometricalFunction(TrigonometricalFunction.Type.SIN);
        double result = sin.apply(100 * Math.PI);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    void testCosLargeAngle() throws ExpressionException {
        TrigonometricalFunction cos = new TrigonometricalFunction(TrigonometricalFunction.Type.COS);
        double result = cos.apply(100 * Math.PI);
        assertEquals(1.0, result, DELTA);
    }
}