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
 * Вычисление RPN через стек.
 * Поддерживает бинарные операции и функции.
 */
public class StackEvaluator implements Evaluator {
    private final FunctionRegistry functionRegistry;
    private final VariableProvider variableProvider;

    public StackEvaluator(VariableProvider variableProvider, FunctionRegistry functionRegistry) {
        this.variableProvider = variableProvider;
        this.functionRegistry = functionRegistry;
    }

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
                    stack.push(variableProvider.getVariable(token.value()));
                    break;

                case PLUS:
                    checkStackSize(stack, 2);
                    stack.push(stack.pop() + stack.pop());
                    break;

                case MINUS:
                    checkStackSize(stack, 2);
                    double right = stack.pop();
                    double left = stack.pop();
                    stack.push(left - right);
                    break;

                case MULTIPLY:
                    checkStackSize(stack, 2);
                    stack.push(stack.pop() * stack.pop());
                    break;

                case DIVIDE:
                    checkStackSize(stack, 2);
                    right = stack.pop();
                    left = stack.pop();
                    if (right == 0) {
                        throw new ExpressionException("Division by zero");
                    }
                    stack.push(left / right);
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

    private void checkStackSize(Deque<Double> stack, int required) {
        if (stack.size() < required) {
            throw new ExpressionException(
                    "Invalid RPN: not enough operands (have " + stack.size() + ", need " + required + ")"
            );
        }
    }
}