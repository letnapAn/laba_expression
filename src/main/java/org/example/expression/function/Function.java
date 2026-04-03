package org.example.expression.function;

import org.example.expression.ExpressionException;

/**
 * Интерфейс математической функции (паттерн Strategy).
 * <p>
 * Реализации этого интерфейса позволяют добавлять кастомные функции
 * в систему вычисления выражений без изменения кода {@code StackEvaluator}.
 * <p>
 * <b>Требования к реализации:</b>
 * <ul>
 *   <li>Должен быть потокобезопасным и не хранить изменяемое состояние (stateless)</li>
 *   <li>Метод {@link #apply} должен проверять количество и валидность аргументов</li>
 *   <li>При некорректных аргументах выбрасывать {@link ExpressionException}</li>
 * </ul>
 *
 * @see FunctionRegistry
 */
public interface Function {
        /**
         * Вычисляет значение функции.
         *
         * @param args аргументы функции (varargs для поддержки унарных/бинарных/многоаргументных функций)
         * @return результат вычисления
         * @throws ExpressionException если количество аргументов не соответствует ожидаемому,
         *                             или значения аргументов выходят за допустимый диапазон
         */
        double apply(double... args) throws ExpressionException;
}