package org.example.expression.function;
import org.example.expression.ExpressionException;
import java.util.function.DoubleUnaryOperator;
/**
 * Стратегия для тригонометрических функций (sin, cos, tan).
 * <p>
 * Инкапсулирует логику выбора конкретной тригонометрической функции.
 * Паттерн: Strategy + Factory Method.
 */
public class TrigonometricalFunction implements Function {
    /**
     * Адаптер для функций с 1 аргументом.
     * @param op принимает только первый элемент выражения
     * @return
     */
    private static Function adaptUnary(DoubleUnaryOperator op) {
        return args -> op.applyAsDouble(args[0]);
    }
    /**
     * Тип тригонометрической функции.
     */
    public enum Type {
        SIN(adaptUnary(Math::sin)),
        COS(adaptUnary(Math::cos)),
        TAN(adaptUnary(Math::tan));

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
     * Создает тригонометрическую функцию.
     *
     * @param type тип функции (SIN, COS, TAN)
     */
    public TrigonometricalFunction(Type type) {
        this.type = type;
    }

    @Override
    public double apply(double... args) throws ExpressionException {
        // Валидация
        if (args.length != 1) {
            throw new ExpressionException(
                    "Trigonometrical function " + type + " requires 1 argument, got " + args.length
            );
        }

        // Делегирование
        return type.getDelegate().apply(args[0]);
    }

    /**
     * Фабричный метод для создания по имени.
     *
     * @param name имя функции ("sin", "cos", "tan")
     * @return экземпляр функции
     * @throws ExpressionException если имя неизвестно
     */
    public static TrigonometricalFunction fromName(String name) throws ExpressionException {
        try {
            Type type = Type.valueOf(name.toUpperCase());
            return new TrigonometricalFunction(type);
        } catch (IllegalArgumentException e) {
            throw new ExpressionException("Unknown trigonometrical function: " + name);
        }
    }
}