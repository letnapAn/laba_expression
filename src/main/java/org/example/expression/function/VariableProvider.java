package org.example.expression.function;

import org.example.expression.ExpressionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Провайдер переменных с кэшированием.
 * Запрашивает значение у пользователя при первом обращении.
 */
public class VariableProvider {
    private final Map<String, Double> cache = new HashMap<>();
    private final Scanner scanner;

    /**
     * Конструктор для тестов (позволяет внедрить Scanner).
     */
    public VariableProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Конструктор по умолчанию (читает из System.in).
     */
    public VariableProvider() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Возвращает значение переменной.
     * Если переменной нет в кэше — запрашивает ввод.
     */
    public double getVariable(String name) throws ExpressionException {
        // Проверяем кэш
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        // Запрашиваем ввод
        System.out.print("Введите значение переменной '" + name + "': ");
        try {
            if (!scanner.hasNext()) {
                throw new ExpressionException("Ожидается число для переменной '" + name + "'");
            }

            String input = scanner.next();
            double value;
            try {
                value = Double.parseDouble(input);
            } catch (NumberFormatException e) {
                throw new ExpressionException("Ожидается число для переменной '" + name + "', получено: " + input);
            }

            cache.put(name, value);
            return value;
        } catch (ExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new ExpressionException("Ошибка чтения переменной: " + e.getMessage());
        }
    }

    /**
     * Проверяет наличие переменной в кэше.
     */
    public boolean hasVariable(String name) {
        return cache.containsKey(name);
    }

    /**
     * Очищает кэш переменных.
     */
    public void clearCache() {
        cache.clear();
    }
}