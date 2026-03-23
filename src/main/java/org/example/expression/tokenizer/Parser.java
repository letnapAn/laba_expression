package org.example.expression.tokenizer;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Парсер выражений: преобразует инфиксную нотацию в обратную польскую (RPN).
 * Использует алгоритм сортировочной станции Дейкстры.
 */
public class Parser {

    /**
     * Парсит список токенов в обратную польскую нотацию.
     * @param tokens список токенов в инфиксной нотации
     * @return список токенов в постфиксной нотации (RPN)
     * @throws ExpressionException если выражение синтаксически некорректно
     */
    public List<Token> parse(List<Token> tokens) throws ExpressionException {
        List<Token> output = new ArrayList<>();
        Deque<Token> operators = new ArrayDeque<>();

        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER:
                case VARIABLE:
                    output.add(token);
                    break;

                case FUNCTION:
                    operators.push(token);
                    break;

                case LBRACKET:
                    operators.push(token);
                    break;

                case RBRACKET:
                    while (!operators.isEmpty() &&
                            operators.peek().type() != TokenType.LBRACKET) {
                        output.add(operators.pop());
                    }
                    if (operators.isEmpty()) {
                        throw new ExpressionException("Mismatched parentheses");
                    }
                    operators.pop(); // Remove '('
                    if (!operators.isEmpty() &&
                            operators.peek().type() == TokenType.FUNCTION) {
                        output.add(operators.pop());
                    }
                    break;

                case PLUS:
                case MINUS:
                case MULTIPLY:
                case DIVIDE:
                    while (!operators.isEmpty() &&
                            hasHigherOrEqualPrecedence(operators.peek(), token)) {
                        output.add(operators.pop());
                    }
                    operators.push(token);
                    break;

                default:
                    throw new ExpressionException("Unexpected token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            Token op = operators.pop();
            if (op.type() == TokenType.LBRACKET) {
                throw new ExpressionException("Mismatched parentheses");
            }
            output.add(op);
        }

        return output;
    }

    private boolean hasHigherOrEqualPrecedence(Token op1, Token op2) {
        int p1 = precedence(op1.type());
        int p2 = precedence(op2.type());
        return p1 >= p2;
    }

    private int precedence(TokenType type) {
        switch (type) {
            case PLUS:
            case MINUS:
                return 1;
            case MULTIPLY:
            case DIVIDE:
                return 2;
            default:
                return 0;
        }
    }
}