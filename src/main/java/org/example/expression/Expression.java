package org.example.expression;

import org.example.expression.evaluator.Evaluator;
import org.example.expression.evaluator.StackEvaluator;
import org.example.expression.tokenizer.Parser;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.Tokenizer;

import java.util.List;

/**
 * Публичный API для вычисления математических выражений.
 * <p>
 * Класс предоставляет простой интерфейс для парсинга и вычисления
 * арифметических выражений, включая поддержку:
 * <ul>
 *   <li>Базовых арифметических операций: {@code +}, {@code -}, {@code *}, {@code /}</li>
 *   <li>Скобок для группировки: {@code ( )}</li>
 *   <li>Математических функций: {@code sin}, {@code cos}, {@code tan}, {@code ln}, {@code log10}, {@code log2}, {@code sqrt}, {@code abs}</li>
 *   <li>Переменных (одиночные буквы): {@code x}, {@code y}, {@code z} и т.д.</li>
 * </ul>
 * <p>
 * <b>Архитектура:</b> Класс реализует паттерн <i>Facade</i>, скрывая сложность
 * токенизации, парсинга (Shunting Yard алгоритм) и вычисления (RPN через стек).
 * <p>
 * <b>Примеры использования:</b>
 * <pre>{@code
 * // Простое выражение
 * Expression expr1 = new Expression("2 + 2 * (3 - 1)");
 * double result1 = expr1.calc(); // 6.0
 *
 * // С функциями
 * Expression expr2 = new Expression("sqrt(16) + abs(-5)");
 * double result2 = expr2.calc(); // 9.0
 *
 * // С переменными (запрос значений в консоли)
 * Expression expr3 = new Expression("x * x + y");
 * double result3 = expr3.calc(); // запросит ввод x, затем y
 *
 * // С кастомным evaluator (для тестирования)
 * StackEvaluator customEvaluator = new StackEvaluator(provider, registry);
 * Expression expr4 = new Expression("x + y", customEvaluator);
 * }</pre>
 *
 * @see Tokenizer
 * @see Parser
 * @see StackEvaluator
 * @since 1.0
 */
public class Expression {

    /**
     * Исходное математическое выражение в строковом виде.
     */
    private final String expression;

    /**
     * Лексический анализатор для преобразования строки в токены.
     */
    private final Tokenizer tokenizer;

    /**
     * Парсер для преобразования инфиксной записи в обратную польскую (RPN).
     */
    private final Parser parser;

    /**
     * Вычислитель RPN выражений.
     */
    private final Evaluator evaluator;

    /**
     * Создаёт выражение с полной зависимостью (для тестирования).
     * <p>
     * Этот конструктор позволяет внедрить зависимости вручную,
     * что полезно для unit-тестирования с моками.
     *
     * @param expression строка математического выражения
     * @param tokenizer  лексический анализатор
     * @param parser     парсер инфикс → RPN
     * @param evaluator  вычислитель RPN
     * @apiNote Конструктор пакетного доступа (private) — используется
     *          только другими конструкторами этого класса.
     */
    private Expression(String expression, Tokenizer tokenizer, Parser parser, StackEvaluator evaluator) {
        this.expression = expression;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.evaluator = evaluator;
    }

    /**
     * Создаёт выражение для вычисления с настройками по умолчанию.
     * <p>
     * Использует стандартные компоненты:
     * <ul>
     *   <li>{@link Tokenizer} — для лексического анализа</li>
     *   <li>{@link Parser} — для преобразования в RPN</li>
     *   <li>{@link StackEvaluator} — для вычисления без переменных</li>
     * </ul>
     *
     * @param expression строка математического выражения
     * @throws ExpressionException если выражение содержит синтаксические ошибки
     * @see Expression#Expression(String, StackEvaluator)
     */
    public Expression(String expression) {
        this(expression, new Tokenizer(), new Parser(), new StackEvaluator());
    }

    /**
     * Создаёт выражение с кастомным вычислителем.
     * <p>
     * Позволяет передать {@link StackEvaluator} с переменными
     * с дополнительными функциями.
     *
     * @param expression строка математического выражения
     * @param evaluator  вычислитель RPN с настроенными зависимостями
     * @throws ExpressionException если выражение содержит синтаксические ошибки
     */
    public Expression(String expression, StackEvaluator evaluator) {
        this(expression, new Tokenizer(), new Parser(), evaluator);
    }

    /**
     * Вычисляет значение математического выражения.
     * <p>
     * Метод выполняет три этапа:
     * <ol>
     *   <li><b>Токенизация</b> — разбиение строки на токены (числа, операторы, функции)</li>
     *   <li><b>Парсинг</b> — преобразование инфиксной записи в обратную польскую (RPN)</li>
     *   <li><b>Вычисление</b> — вычисление RPN через стек</li>
     * </ol>
     * <p>
     * <b>Поддержка переменных:</b>
     * Если выражение содержит переменные (например, {@code "x + y"}),
     * и {@link StackEvaluator} был создан с VariableProvider,
     * то при вычислении значения переменных будут запрошены у пользователя
     * через консоль (если ещё не закэшированы).
     *
     * @return результат вычисления выражения
     * @throws ExpressionException если:
     *                           <ul>
     *                             <li>выражение содержит синтаксические ошибки</li>
     *                             <li>происходит деление на ноль</li>
     *                             <li>используется неизвестная функция или переменная</li>
     *                             <li>некорректное количество аргументов у функции</li>
     *                             <li>переменные не поддерживаются (нет VariableProvider)</li>
     *                           </ul>
     * @see Tokenizer#tokenize(String)
     * @see Parser#parse(List)
     * @see Evaluator#evaluate(List)
     */
    public double calc() throws ExpressionException {
        // 1. Токенизация
        List<Token> tokens = tokenizer.tokenize(expression);

        // 2. Парсинг (инфикс → RPN)
        List<Token> rpn = parser.parse(tokens);

        // 3. Вычисление
        return evaluator.evaluate(rpn);
    }
}