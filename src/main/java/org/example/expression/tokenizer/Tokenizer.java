package org.example.expression.tokenizer;

import org.example.expression.ExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Лексический анализатор выражения.
 * Преобразует строку в список токенов.
 */
public class Tokenizer {

    private String expression;
    private int position;

    /**
     * Токенизирует выражение.
     * @param expression строка выражения
     * @return список токенов
     * @throws ExpressionException если встречен недопустимый символ
     */
    public List<Token> tokenize(String expression) throws ExpressionException {
        this.expression = expression.replaceAll("\\s+", "");
        this.position = 0;
        List<Token> tokens = new ArrayList<>();

        while (position < this.expression.length()) {
            char current = peek();

            if (Character.isDigit(current) || current == '.') {
                tokens.add(readNumber());
            } else if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
            } else if (isOperator(current)) {
                tokens.add(readOperator());
            } else if (current == '(') {
                tokens.add(new Token(TokenType.LBRACKET, "("));
                position++;
            } else if (current == ')') {
                tokens.add(new Token(TokenType.RBRACKET, ")"));
                position++;
            } else {
                throw new ExpressionException("Unexpected character: " + current);
            }
        }
        return tokens;
    }

    private char peek() {
        return expression.charAt(position);
    }

    private boolean isEOF() {
        return position >= expression.length();
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        while (!isEOF() && (Character.isDigit(peek()) || peek() == '.')) {
            sb.append(peek());
            position++;
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (!isEOF() && Character.isLetterOrDigit(peek())) {
            sb.append(peek());
            position++;
        }
        String value = sb.toString();
        // Простая эвристика: если после имени идёт '(' — это функция
        TokenType type = (!isEOF() && peek() == '(') ? TokenType.FUNCTION : TokenType.VARIABLE;
        return new Token(type, value);
    }

    private Token readOperator() {
        char op = peek();
        position++;
        TokenType type;
        switch (op) {
            case '+': type = TokenType.PLUS; break;
            case '-': type = TokenType.MINUS; break;
            case '*': type = TokenType.MULTIPLY; break;
            case '/': type = TokenType.DIVIDE; break;
            default: throw new ExpressionException("Unknown operator: " + op);
        }
        return new Token(type, String.valueOf(op));
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}