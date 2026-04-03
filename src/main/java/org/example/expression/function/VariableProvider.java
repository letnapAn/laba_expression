package org.example.expression.function;

import org.example.expression.ExpressionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Провайдер значений переменных с механизмом кэширования.
 * <p>
 * Используется в StackEvaluator для получения значений переменных (x, y, z и т.д.)
 * во время вычисления выражения. При первом обращении к неизвестной переменной
 * запрашивает ввод через консоль и сохраняет результат в локальный кэш.
 * Повторные обращения к той же переменной возвращают закэшированное значение без запроса ввода.
 * <p>
 * <b>Контракт использования:</b>
 * <ul>
 *   <li>Не является потокобезопасным</li>
 *   <li>Метод {@link #getVariable} может блокировать поток ввода-вывода</li>
 * </ul>
 *
 */
public class VariableProvider {
    private final Map<String, Double> cache = new HashMap<>();
    private final Scanner scanner;

    /**
     * Создаёт провайдер с кастомным источником ввода.
     * <p>
     * Полезно для unit-тестирования, когда необходимо имитировать ввод пользователя
     * без блокировки консоли (например, через {@link java.io.ByteArrayInputStream}).
     *
     * @param scanner источник ввода значений
     */
    public VariableProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Создаёт провайдер с вводом из стандартного потока ({@code System.in}).
     * <p>
     * Используется по умолчанию при вычислении выражений с переменными.
     */
    public VariableProvider() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Возвращает значение переменной.
     * <p>
     * Алгоритм работы:
     * <ol>
     *   <li>Проверяет наличие значения в кэше, то возвращает, если найдено</li>
     *   <li>Если нет, то выводит приглашение в консоль и ожидает ввод числа</li>
     *   <li>Парсит введённое значение, сохраняет в кэш и возвращает</li>
     * </ol>
     *
     * @param name имя переменной (обычно одна буква: a, b, c, x, y, z etc)
     * @return числовое значение переменной
     * @throws ExpressionException если ввод не является валидным числом,
     *                             или источник ввода закрыт/исчерпан
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
     * Проверяет, было ли уже запрошено и закэшировано значение переменной.
     * <p>
     * Не вызывает блокирующий ввод, только проверяет внутреннее состояние кэша.
     *
     * @param name имя переменной
     * @return {@code true} если значение присутствует в кэше, иначе {@code false}
     */
    public boolean hasVariable(String name) {
        return cache.containsKey(name);
    }

    /**
     * Очищает кэш сохранённых переменных.
     * <p>
     * После вызова следующего {@link #getVariable(String)} для ранее кэшированной
     * переменной снова будет запрошен ввод с консоли.
     */
    public void clearCache() {
        cache.clear();
    }
}