package org.example.expression.function;

import org.example.expression.ExpressionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VariableProvider")
class VariableProviderTest {

    @Nested
    @DisplayName("Кэширование переменных")
    class CachingTests {

        @Test
        @DisplayName("Первый запрос → ввод, второй → из кэша")
        void firstRequest_readsInput_secondRequest_returnsCached() {
            String input = "5.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                double result = provider.getVariable("x");
                assertEquals(5.0, result);
            });

            // Второй запрос — из кэша
            assertDoesNotThrow(() -> {
                double result = provider.getVariable("x");
                assertEquals(5.0, result);
            });
        }

        @Test
        @DisplayName("Разные переменные → разные значения")
        void differentVariables_differentValues() {
            String input = "10.0\n20.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(10.0, provider.getVariable("a"));
                assertEquals(20.0, provider.getVariable("b"));
            });
        }

        @Test
        @DisplayName("Множественные запросы одной переменной → один ввод")
        void multipleRequests_singleInput() {
            String input = "42.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(42.0, provider.getVariable("x"));
                assertEquals(42.0, provider.getVariable("x"));
                assertEquals(42.0, provider.getVariable("x"));
            });
        }
    }

    @Nested
    @DisplayName("Проверка наличия переменной")
    class HasVariableTests {

        @Test
        @DisplayName("hasVariable до запроса → false")
        void hasVariable_beforeRequest_false() {
            VariableProvider provider = new VariableProvider();
            assertFalse(provider.hasVariable("x"));
        }

        @Test
        @DisplayName("hasVariable после запроса → true")
        void hasVariable_afterRequest_true() {
            String input = "7.5\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> provider.getVariable("y"));
            assertTrue(provider.hasVariable("y"));
        }
    }

    @Nested
    @DisplayName("Очистка кэша")
    class ClearCacheTests {

        @Test
        @DisplayName("clearCache → hasVariable возвращает false")
        void clearCache_hasVariableReturnsFalse() {
            String input = "3.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> provider.getVariable("x"));
            assertTrue(provider.hasVariable("x"));

            provider.clearCache();
            assertFalse(provider.hasVariable("x"));
        }

        @Test
        @DisplayName("clearCache → следующий запрос требует новый ввод")
        void clearCache_nextRequestRequiresNewInput() {
            String input = "5.0\n10.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(5.0, provider.getVariable("x"));
                provider.clearCache();
                assertEquals(10.0, provider.getVariable("x"));
            });
        }
    }

    @Nested
    @DisplayName("Обработка ошибок ввода")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Некорректный ввод → ExpressionException")
        void invalidInput_throwsException() {
            String input = "abc\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            ExpressionException exception = assertThrows(ExpressionException.class, () -> {
                provider.getVariable("x");
            });
            assertTrue(exception.getMessage().contains("Ожидается число"));
        }

        @Test
        @DisplayName("Пустой ввод → ExpressionException")
        void emptyInput_throwsException() {
            String input = "";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertThrows(ExpressionException.class, () -> {
                provider.getVariable("x");
            });
        }

        @Test
        @DisplayName("Отрицательные числа → допустимы")
        void negativeNumbers_accepted() {
            String input = "-15.5\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(-15.5, provider.getVariable("x"));
            });
        }

        @Test
        @DisplayName("Дробные числа → допустимы")
        void decimalNumbers_accepted() {
            String input = "3.14159\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(3.14159, provider.getVariable("pi"));
            });
        }
    }

    @Nested
    @DisplayName("Конструкторы")
    class ConstructorTests {

        @Test
        @DisplayName("Конструктор без параметров → использует System.in")
        void defaultConstructor_usesSystemIn() {
            VariableProvider provider = new VariableProvider();
            assertNotNull(provider);
            assertFalse(provider.hasVariable("any"));
        }

        @Test
        @DisplayName("Конструктор с Scanner → использует переданный Scanner")
        void scannerConstructor_usesProvidedScanner() {
            String input = "99.0\n";
            Scanner customScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(customScanner);

            assertDoesNotThrow(() -> {
                assertEquals(99.0, provider.getVariable("test"));
            });
        }
    }

    @Nested
    @DisplayName("Интеграционные сценарии")
    class IntegrationTests {

        @Test
        @DisplayName("Сложный сценарий: несколько переменных, повторное использование")
        void complexScenario_multipleVariables_reuse() {
            String input = "2.5\n3.5\n4.0\n";
            Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
            VariableProvider provider = new VariableProvider(scanner);

            assertDoesNotThrow(() -> {
                assertEquals(2.5, provider.getVariable("x"));
                assertEquals(3.5, provider.getVariable("y"));
                assertEquals(2.5, provider.getVariable("x")); // из кэша
                assertEquals(4.0, provider.getVariable("z"));

                assertTrue(provider.hasVariable("x"));
                assertTrue(provider.hasVariable("y"));
                assertTrue(provider.hasVariable("z"));
            });
        }
    }
}