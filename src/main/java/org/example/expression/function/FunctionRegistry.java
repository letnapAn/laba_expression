package org.example.expression.function;

import org.example.expression.ExpressionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр доступных функций.
 * <p>
 * Централизованное хранилище всех зарегистрированных функций.
 * Паттерн: Registry + Factory.
 */
public class FunctionRegistry {

    private final Map<String, Function> functions;

    /**
     * Создает реестр с базовыми функциями (sin, cos, ln, log10).
     */
    public FunctionRegistry() {
        this.functions = new HashMap<>();
        registerDefaults();
    }

    /**
     * Регистрирует функции по умолчанию.
     */
    private void registerDefaults() {
        // Тригонометрические
        register("sin", new TrigonometricalFunction(TrigonometricalFunction.Type.SIN));
        register("cos", new TrigonometricalFunction(TrigonometricalFunction.Type.COS));
        register("tan", new TrigonometricalFunction(TrigonometricalFunction.Type.TAN));

        // Логарифмические
        register("ln", new LogFunction(LogFunction.Type.LN));
        register("log10", new LogFunction(LogFunction.Type.LOG10));
        register("log2", new LogFunction(LogFunction.Type.LOG2));

        // Другие (через лямбды для простоты)
        register("sqrt", args->Math.sqrt(args[0]));
        register("abs", args -> Math.abs(args[0]));
    }

    /**
     * Регистрирует функцию по имени.
     *
     * @param name имя функции
     * @param function реализация
     */
    public void register(String name, Function function) {
        functions.put(name.toLowerCase(), function);
    }

    /**
     * Получает функцию по имени.
     *
     * @param name имя функции
     * @return реализация функции
     * @throws ExpressionException если функция не найдена
     */
    public Function get(String name) throws ExpressionException {
        Function function = functions.get(name.toLowerCase());
        if (function == null) {
            throw new ExpressionException("Unknown function: " + name);
        }
        return function;
    }

    /**
     * Проверяет наличие функции.
     *
     * @param name имя функции
     * @return true если функция зарегистрирована
     */
    public boolean hasFunction(String name) {
        return functions.containsKey(name.toLowerCase());
    }
}