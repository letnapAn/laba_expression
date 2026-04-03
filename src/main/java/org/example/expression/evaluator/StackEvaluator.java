package org.example.expression.evaluator;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.TokenType;
import org.example.expression.function.Function;
import org.example.expression.function.FunctionRegistry;
import org.example.expression.function.VariableProvider;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Интерпретатор математических выражений в обратной польской записи (RPN).
 * <p>
 * Реализует паттерн <b>Interpreter</b>. Вычисление происходит за один проход (O(N))
 * с использованием стека для операндов.
 * <p>
 * <b>Алгоритм:</b>
 * 1. Если токен — число: положить на стек.
 * 2. Если токен — переменная: получить значение из {@link VariableProvider} и положить на стек.
 * 3. Если токен — оператор: извлечь операнды, вычислить результат, положить обратно.
 * 4. Если токен — функция: извлечь аргумент, вычислить, положить обратно.
 *
 * @see Evaluator
 * @see VariableProvider
 */
public class StackEvaluator implements Evaluator {
    private final FunctionRegistry functionRegistry;
    private final VariableProvider variableProvider;

    /**
     * Создаёт эвалюатор с зависимостями.
     *
     * @param variableProvider провайдер для получения значений переменных
     * @param functionRegistry реестр доступных функций
     */
    public StackEvaluator(VariableProvider variableProvider, FunctionRegistry functionRegistry) {
        this.variableProvider = variableProvider;
        this.functionRegistry = functionRegistry;
    }

    /**
     * Создаёт эвалюатор с настройками по умолчанию (без переменных).
     * Полезен для простых выражений вида "2 + 2".
     */
    public StackEvaluator() {
        this(new VariableProvider(), new FunctionRegistry());
    }

    @Override
    public double evaluate(List<Token> rpn) throws ExpressionException {
        Deque<Double> stack = new ArrayDeque<>();

        for (Token token : rpn) {
            TokenType type = token.type();

            switch (type) {
                case NUMBER:
                    stack.push(Double.parseDouble(token.value()));
                    break;

                case VARIABLE:
                    if (variableProvider == null) {
                        throw new ExpressionException("Variables not supported");
                    }
                    // Запрашиваем значение (может блокировать ввод/вывод)
                    stack.push(variableProvider.getVariable(token.value()));
                    break;

                case PLUS:
                    checkStackSize(stack, 2);
                    // Порядок: второе извлеченное - первое извлеченное
                    stack.push(stack.pop() + stack.pop());
                    break;

                case MINUS:
                    checkStackSize(stack, 2);
                    double rightSub = stack.pop();
                    double leftSub = stack.pop();
                    stack.push(leftSub - rightSub);
                    break;

                case MULTIPLY:
                    checkStackSize(stack, 2);
                    stack.push(stack.pop() * stack.pop());
                    break;

                case DIVIDE:
                    checkStackSize(stack, 2);
                    double rightDiv = stack.pop();
                    double leftDiv = stack.pop();
                    if (rightDiv == 0) {
                        throw new ExpressionException("Division by zero");
                    }
                    stack.push(leftDiv / rightDiv);
                    break;

                case FUNCTION:
                    Function func = functionRegistry.get(token.value());
                    checkStackSize(stack, 1);
                    stack.push(func.apply(stack.pop()));
                    break;

                default:
                    throw new ExpressionException("Unexpected token: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new ExpressionException("Invalid RPN");
        }
        return stack.pop();
    }

    /**
     * Проверяет, достаточно ли операндов на стеке для операции.
     *
     * @param stack    текущий стек
     * @param required необходимое количество элементов
     * @throws ExpressionException если элементов меньше required
     */
    private void checkStackSize(Deque<Double> stack, int required) {
        if (stack.size() < required) {
            throw new ExpressionException(
                    "Invalid RPN: not enough operands (have " + stack.size() + ", need " + required + ")"
            );
        }
    }
}