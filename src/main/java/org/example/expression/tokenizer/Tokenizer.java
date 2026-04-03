package org.example.expression.tokenizer;

import org.example.expression.ExpressionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Лексический анализатор (Lexer).
 * <p>
 * Преобразует входную строку выражения в список {@link Token}.
 * Использует подход ручного разбора символов (char-by-char parsing).
 * <p>
 * <b>Особенности:</b>
 * <ul>
 *   <li>Автоматически удаляет все пробельные символы</li>
 *   <li>Поддерживает целые и дробные числа</li>
 *   <li>Различает функции и переменные по наличию открывающей скобки '{@code (}' сразу после имени</li>
 * </ul>
 *
 * @see Token
 * @see TokenType
 */
public class Tokenizer {
    private String expression;
    private int position;

    /**
     * Токенизирует входное выражение.
     *
     * @param expression строка с математическим выражением
     * @return упорядоченный список токенов
     * @throws ExpressionException если выражение содержит недопустимые символы
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

    /**
     * Возвращает текущий символ без смещения позиции.
     */
    private char peek() {
        return expression.charAt(position);
    }

    /**
     * Проверяет, достигнут ли конец строки.
     */
    private boolean isEOF() {
        return position >= expression.length();
    }

    /**
     * Считывает число (целое или дробное) до первого нецифрового символа.
     *
     * @return токен типа NUMBER
     */
    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        while (!isEOF() && (Character.isDigit(peek()) || peek() == '.')) {
            sb.append(peek());
            position++;
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    /**
     * Считывает идентификатор (имя функции или переменной).
     * <p>
     * <b>Эвристика определения типа:</b>
     * Если сразу за именем следует символ '{@code (}', то это функция.
     * В противном случае — переменная.
     *
     * @return токен типа FUNCTION или VARIABLE
     */
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

    /**
     * Считывает один символ оператора (+, -, *, /).
     *
     * @return токен соответствующего оператора
     * @throws ExpressionException если символ не является поддерживаемым оператором
     */
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

    /**
     * Вспомогательный метод проверки принадлежности символа к операторам.
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}