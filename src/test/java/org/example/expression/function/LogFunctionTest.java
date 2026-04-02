package org.example.expression.function;
import org.example.expression.ExpressionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LogFunction")
class LogFunctionTest {

    @Nested
    @DisplayName("Натуральный логарифм (ln)")
    class LnTests {
        private final LogFunction ln = new LogFunction(LogFunction.Type.LN);

        @Test
        @DisplayName("ln(1) = 0")
        void ln_one_returnsZero() {
            assertThat(ln.apply(1.0)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("ln(e) ≈ 1")
        void ln_e_returnsOne() {
            assertThat(ln.apply(Math.E)).isCloseTo(1.0, within(1e-10));
        }

        @Test
        @DisplayName("ln(0) выбрасывает исключение")
        void ln_zero_throwsException() {
            assertThatThrownBy(() -> ln.apply(0.0))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("undefined");
        }

        @Test
        @DisplayName("ln(отрицательное) выбрасывает исключение")
        void ln_negative_throwsException() {
            assertThatThrownBy(() -> ln.apply(-5.0))
                    .isInstanceOf(ExpressionException.class);
        }
    }

    @Nested
    @DisplayName("Логарифм по основанию 10 (log10)")
    class Log10Tests {
        private final LogFunction log10 = new LogFunction(LogFunction.Type.LOG10);

        @Test
        @DisplayName("log10(1) = 0")
        void log10_one_returnsZero() {
            assertThat(log10.apply(1.0)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("log10(100) = 2")
        void log10_hundred_returnsTwo() {
            assertThat(log10.apply(100.0)).isCloseTo(2.0, within(1e-10));
        }

        @Test
        @DisplayName("log10(0.01) = -2")
        void log10_small_returnsNegative() {
            assertThat(log10.apply(0.01)).isCloseTo(-2.0, within(1e-10));
        }
    }

    @Nested
    @DisplayName("Логарифм по основанию 2 (log2)")
    class Log2Tests {
        private final LogFunction log2 = new LogFunction(LogFunction.Type.LOG2);

        @Test
        @DisplayName("log2(8) = 3")
        void log2_eight_returnsThree() {
            assertThat(log2.apply(8.0)).isCloseTo(3.0, within(1e-10));
        }

        @Test
        @DisplayName("log2(1024) = 10")
        void log2_1024_returnsTen() {
            assertThat(log2.apply(1024.0)).isCloseTo(10.0, within(1e-10));
        }
    }

    @Nested
    @DisplayName("Валидация аргументов")
    class ArgumentValidationTests {

        @Test
        @DisplayName("Нет аргументов → исключение")
        void noArgs_throwsException() {
            LogFunction fn = new LogFunction(LogFunction.Type.LN);
            assertThatThrownBy(() -> fn.apply())
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("requires 1 argument");
        }

        @Test
        @DisplayName("Два аргумента → исключение")
        void twoArgs_throwsException() {
            LogFunction fn = new LogFunction(LogFunction.Type.LN);
            assertThatThrownBy(() -> fn.apply(1.0, 2.0))
                    .isInstanceOf(ExpressionException.class);
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -0.1, -100, Double.NEGATIVE_INFINITY})
        @DisplayName("Неположительные значения → исключение")
        void nonPositive_throwsException(double value) {
            LogFunction fn = new LogFunction(LogFunction.Type.LN);
            assertThatThrownBy(() -> fn.apply(value))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("undefined");
        }

        @Test
        @DisplayName("NaN → исключение (защита от скрытых ошибок)")
        void nan_throwsException() {
            LogFunction fn = new LogFunction(LogFunction.Type.LN);
            assertThatThrownBy(() -> fn.apply(Double.NaN))
                    .isInstanceOf(ExpressionException.class);
        }

        @Test
        @DisplayName("Positive Infinity → допустимо (возвращает Infinity)")
        void positiveInfinity_returnsInfinity() {
            LogFunction fn = new LogFunction(LogFunction.Type.LN);
            assertThat(fn.apply(Double.POSITIVE_INFINITY)).isEqualTo(Double.POSITIVE_INFINITY);
        }
    }

    @Nested
    @DisplayName("Фабричный метод fromName")
    class FactoryMethodTests {

        @ParameterizedTest
        @ValueSource(strings = {"ln", "LN", "Ln", "log10", "LOG10", "log2", "Log2"})
        @DisplayName("Валидные имена (регистронезависимые) → создаёт функцию")
        void validNames_createsFunction(String name) {
            assertThatCode(() -> LogFunction.fromName(name))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Неизвестное имя → исключение с подсказкой")
        void invalidName_throwsException() {
            assertThatThrownBy(() -> LogFunction.fromName("log"))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("Unknown");
        }

        @Test
        @DisplayName("null имя - исключение (защита от NPE)")
        void nullName_throwsException() {
            assertThatThrownBy(() -> LogFunction.fromName(null))
                    .isInstanceOf(ExpressionException.class)
                    .hasMessageContaining("null")
                    .extracting(Throwable::getMessage)
                    .isNotNull(); // гарантируем, что не NPE
        }
    }

    @Nested
    @DisplayName("Поведенческие тесты")
    class BehavioralTests {

        @Test
        @DisplayName("Малое положительное число => отрицательный результат")
        void smallPositive_returnsNegative() {
            LogFunction ln = new LogFunction(LogFunction.Type.LN);
            assertThat(ln.apply(0.5)).isNegative();
        }

        @Test
        @DisplayName("Большое число → положительный результат")
        void largeValue_returnsPositive() {
            LogFunction ln = new LogFunction(LogFunction.Type.LN);
            assertThat(ln.apply(1e10)).isPositive();
        }

        @Test
        @DisplayName("Пограничное значение: минимальное нормализованное double")
        void minNormalValue_doesNotThrow() {
            LogFunction ln = new LogFunction(LogFunction.Type.LN);
            assertThatCode(() -> ln.apply(Double.MIN_NORMAL))
                    .doesNotThrowAnyException();
        }
    }
}