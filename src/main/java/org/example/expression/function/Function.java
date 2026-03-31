package org.example.expression.function;
import org.example.expression.ExpressionException;
/**
 * Интерфейс математической функции (паттерн Strategy).
 * <p>
 * Все функции реализуют этот интерфейс для единообразного вызова
 * из StackEvaluator.
 */

public interface Function {
        /**
         * Вычисляет значение функции.
         *
         * @param args аргументы функции (varargs для гибкости)
         * @return результат вычисления
         * @throws ExpressionException если аргументы некорректны
         */
        double apply(double... args) throws ExpressionException;

}
