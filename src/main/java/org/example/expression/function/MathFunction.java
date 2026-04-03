package org.example.expression.function;

import org.example.expression.ExpressionException;

import java.util.function.DoubleUnaryOperator;

public class MathFunction implements Function {
    private static Function adaptUnary(DoubleUnaryOperator op) {
        return args -> op.applyAsDouble(args[0]);
    }
    private final Type type;

    MathFunction(Type type) {
        this.type = type;
    }

    public enum Type {
        ABS(adaptUnary(Math::abs)),
        SQRT(adaptUnary(Math::sqrt));

        private final Function delegate;

        Type(Function delegate) {
            this.delegate = delegate;
        }

        public Function getDelegate() {
            return delegate;
        }
    };
    @Override
    public double apply(double... args) throws ExpressionException {

        if (args.length != 1) {
            throw new ExpressionException(
                    "Math function " + type + " requires 1 argument, got " + args.length
            );
        }
        if (Double.isNaN(args[0])) {
            throw new ExpressionException("Math undefined for NaN");
        }
        double value = args[0];

        if (value < 0 && Type.SQRT == type) {
            throw new ExpressionException(
                    "Irrational: " + value
            );
        }

        return type.getDelegate().apply(value);
    }

    /**
     * Фабричный метод для создания по имени.
     *
     * @param name имя функции
     * @return экземпляр функции
     * @throws ExpressionException если имя неизвестно
     */
    public static MathFunction fromName(String name) throws ExpressionException {
        if (name == null) {
            throw new ExpressionException("Function name cannot be null. Available: abs, sqrt");
        }
        try {
            MathFunction.Type type = MathFunction.Type.valueOf(name.toUpperCase());
            return new MathFunction(type);
        } catch (IllegalArgumentException e) {
            throw new ExpressionException(
                    "Unknown"
            );
        }
    }



}
