package org.example.expression.function;

import org.example.expression.ExpressionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MathFunction")
class MathFunctionTest {

    @Nested
    @DisplayName("Функция abs")
    class AbsTests {
        private final MathFunction abs = new MathFunction(MathFunction.Type.ABS);

        @Test
        @DisplayName("abs(положительное) = само число")
        void abs_positive() {
            assertThat(abs.apply(123.45)).isEqualTo(123.45);
        }

        @Test
        @DisplayName("abs(отрицательное) = модуль")
        void abs_negative() {
            assertThat(abs.apply(-67.89)).isEqualTo(67.89);
        }

        @Test
        @DisplayName("abs(-0.0) → 0.0")
        void abs_negativeZero() {
            assertThat(abs.apply(-0.0)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("abs(NaN) → исключение")
        void abs_nan() {
            assertThatThrownBy(() -> abs.apply(Double.NaN))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("NaN");
        }

        @ParameterizedTest
        @ValueSource(doubles = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.MAX_VALUE})
        @DisplayName("abs(special values) → делегирует Math.abs")
        void abs_special(double input) {
            assertThat(abs.apply(input)).isEqualTo(Math.abs(input));
        }
    }

    @Nested
    @DisplayName("Функция sqrt")
    class SqrtTests {
        private final MathFunction sqrt = new MathFunction(MathFunction.Type.SQRT);

        @Test
        @DisplayName("sqrt(1) = 1")
        void sqrt_one() {
            assertThat(sqrt.apply(1.0)).isEqualTo(1.0);
        }

        @Test
        @DisplayName("sqrt(2) ≈ 1.414")
        void sqrt_two() {
            assertThat(sqrt.apply(2.0)).isCloseTo(Math.sqrt(2), within(1e-10));
        }

        @Test
        @DisplayName("sqrt(0) = 0")
        void sqrt_zero_shouldWork() {
            assertThat(sqrt.apply(0.0)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("sqrt(отрицательное) → исключение")
        void sqrt_negative() {
            assertThatThrownBy(() -> sqrt.apply(-1.0))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("Irrational");
        }

        @Test
        @DisplayName("sqrt(NaN) → исключение")
        void sqrt_nan() {
            assertThatThrownBy(() -> sqrt.apply(Double.NaN))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("NaN");
        }

        @Test
        @DisplayName("sqrt(Infinity) → Infinity")
        void sqrt_infinity() {
            assertThat(sqrt.apply(Double.POSITIVE_INFINITY)).isEqualTo(Double.POSITIVE_INFINITY);
        }
    }

    @Nested
    @DisplayName("Валидация аргументов")
    class ArgumentValidationTests {

        @Test
        @DisplayName("0 аргументов → исключение")
        void noArgs() {
            MathFunction f = new MathFunction(MathFunction.Type.ABS);
            assertThatThrownBy(() -> f.apply())
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("requires 1 argument");
        }

        @Test
        @DisplayName("2 аргумента → исключение")
        void twoArgs() {
            MathFunction f = new MathFunction(MathFunction.Type.ABS);
            assertThatThrownBy(() -> f.apply(1.0, 2.0))
                    .isInstanceOf(ExpressionException.class);
        }
    }

    @Nested
    @DisplayName("Фабричный метод fromName")
    class FactoryTests {

        @ParameterizedTest
        @ValueSource(strings = {"abs", "ABS", "sqrt", "SQRT", "Abs", "Sqrt"})
        @DisplayName("Валидные имена → создаёт функцию")
        void validNames(String name) {
            assertThatCode(() -> MathFunction.fromName(name)).doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {"log", "pow", "", "root", "mod"})
        @DisplayName("Неизвестное имя → исключение")
        void invalidNames(String name) {
            assertThatThrownBy(() -> MathFunction.fromName(name))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessage("Unknown");  // ← плохое сообщение, сложно дебажить
        }

        @Test
        @DisplayName("null имя → исключение")
        void nullName() {
            assertThatThrownBy(() -> MathFunction.fromName(null))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("cannot be null");
        }
    }

    @Nested
    @DisplayName("Инкапсуляция и API")
    class ApiTests {

        @Test
        @DisplayName("Конструктор пакетного доступа — доступен из тестов")
        void constructor_packagePrivate() {
            // Тест в том же пакете — компилируется
            assertThat(new MathFunction(MathFunction.Type.ABS)).isNotNull();
        }

        @Test
        @DisplayName("getDelegate() возвращает функцию без валидации")
        void getDelegate_bypassesValidation() {
            Function delegate = MathFunction.Type.SQRT.getDelegate();

            assertThatThrownBy(() -> delegate.apply())
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);

            MathFunction sqrt = new MathFunction(MathFunction.Type.SQRT);
            assertThatThrownBy(() -> sqrt.apply())
                    .isInstanceOf(ExpressionException.class);
        }
    }
}