package org.example.expression.function;

import org.example.expression.ExpressionException;

import java.util.function.DoubleUnaryOperator;

/**
 * Стратегия для логарифмических функций (ln, log10, log).
 * <p>
 * Инкапсулирует логику выбора конкретного логарифма.
 * Паттерн: Strategy.
 */
public class LogFunction implements Function {
private static Function adaptUnary(DoubleUnaryOperator op) {
    return args -> op.applyAsDouble(args[0]);
}
    /**
     * Тип логарифмической функции.
     */
    public enum Type {
        LN(adaptUnary(Math::log)),      // Натуральный логарифм
        LOG10(adaptUnary(Math::log10)), // Логарифм по основанию 10
        LOG2(adaptUnary(x -> Math.log(x) / Math.log(2))); // Логарифм по основанию 2

        private final Function delegate;

        Type(Function delegate) {
            this.delegate = delegate;
        }

        public Function getDelegate() {
            return delegate;
        }
    }

    private final Type type;

    /**
     * Создает логарифмическую функцию.
     *
     * @param type тип функции (LN, LOG10, LOG2)
     */
    public LogFunction(Type type) {
        this.type = type;
    }

    @Override
    public double apply(double... args) throws ExpressionException {

        if (args.length != 1) {
            throw new ExpressionException(
                    "Logarithmic function " + type + " requires 1 argument, got " + args.length
            );
        }
        if (Double.isNaN(args[0])) {
            throw new ExpressionException("Logarithm undefined for NaN");
        }
        double value = args[0];

        if (value <= 0) {
            throw new ExpressionException(
                    "Logarithm undefined for value: " + value
            );
        }

        return type.getDelegate().apply(value);
    }

    /**
     * Фабричный метод для создания по имени.
     *
     * @param name имя функции ("ln", "log10", "log2")
     * @return экземпляр функции
     * @throws ExpressionException если имя неизвестно
     */
    public static LogFunction fromName(String name) throws ExpressionException {
        if (name == null) {
            throw new ExpressionException("Function name cannot be null. Available: ln, log10, log2");
        }
        try {
            Type type = Type.valueOf(name.toUpperCase());
            return new LogFunction(type);
        } catch (IllegalArgumentException e) {
            throw new ExpressionException(
                    "Unknown"
            );
        }
    }
}