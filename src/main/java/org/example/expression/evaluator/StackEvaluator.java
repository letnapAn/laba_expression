package org.example.expression.evaluator;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Вычисление RPN через стек.
 */
public class StackEvaluator implements Evaluator {

    @Override
    public double evaluate(List<Token> rpn) throws ExpressionException {
        Deque<Double> stack = new ArrayDeque<>();

        for (Token token : rpn) {
            switch (token.type()) {
                case NUMBER:
                    stack.push(Double.parseDouble(token.value()));
                    break;

                case PLUS:
                    checkStackSize(stack, 2);
                    stack.push(applyOperation(stack.pop(), stack.pop(), '+'));
                    break;

                case MINUS:
                    checkStackSize(stack, 2);
                    double right = stack.pop();
                    double left = stack.pop();
                    stack.push(applyOperation(left, right, '-'));
                    break;

                case MULTIPLY:
                    checkStackSize(stack, 2);
                    stack.push(applyOperation(stack.pop(), stack.pop(), '*'));
                    break;

                case DIVIDE:
                    checkStackSize(stack, 2);
                    right = stack.pop();
                    left = stack.pop();
                    stack.push(applyOperation(left, right, '/'));
                    break;

                default:
                    throw new ExpressionException("Unexpected token in RPN: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new ExpressionException("Invalid expression: expected 1 result, got " + stack.size());
        }

        return stack.pop();
    }

    /**
     * Проверяет, что в стеке достаточно элементов.
     */
    private void checkStackSize(Deque<Double> stack, int required) {
        if (stack.size() < required) {
            throw new ExpressionException(
                    "Invalid RPN: not enough operands for operator (have " +
                            stack.size() + ", need " + required + ")"
            );
        }
    }

    private double applyOperation(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) {
                    throw new ExpressionException("Division by zero");
                }
                return a / b;
            default:
                throw new ExpressionException("Unknown operator: " + op);
        }
    }
}