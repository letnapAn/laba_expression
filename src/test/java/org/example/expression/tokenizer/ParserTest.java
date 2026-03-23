package org.example.expression.tokenizer;

import org.example.expression.ExpressionException;
import org.example.expression.tokenizer.Token;
import org.example.expression.tokenizer.Tokenizer;
import org.example.expression.tokenizer.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для Parser.
 */
class ParserTest {

    private Tokenizer tokenizer;
    private Parser parser;

    @BeforeEach
    void setUp() {
        tokenizer = new Tokenizer();
        parser = new Parser();
    }

    @Test
    void testParseSimpleAddition() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("2 + 3");
        List<Token> rpn = parser.parse(tokens);

        assertEquals(3, rpn.size());
        assertEquals(TokenType.NUMBER, rpn.get(0).type());
        assertEquals(TokenType.NUMBER, rpn.get(1).type());
        assertEquals(TokenType.PLUS, rpn.get(2).type());
    }

    @Test
    void testParseWithMultiplication() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("2 + 3 * 4");
        List<Token> rpn = parser.parse(tokens);

        // RPN: 2 3 4 * +
        assertEquals(5, rpn.size());
        assertEquals(TokenType.NUMBER, rpn.get(0).type());
        assertEquals(TokenType.NUMBER, rpn.get(1).type());
        assertEquals(TokenType.NUMBER, rpn.get(2).type());
        assertEquals(TokenType.MULTIPLY, rpn.get(3).type());
        assertEquals(TokenType.PLUS, rpn.get(4).type());
    }

    @Test
    void testParseWithBrackets() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("(2 + 3) * 4");
        List<Token> rpn = parser.parse(tokens);

        // RPN: 2 3 + 4 *
        assertEquals(5, rpn.size());
        assertEquals(TokenType.PLUS, rpn.get(2).type());
        assertEquals(TokenType.MULTIPLY, rpn.get(4).type());
    }

    @Test
    void testParseComplexExpression() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("2 * (3 + 4) - 5");
        List<Token> rpn = parser.parse(tokens);

        // RPN: 2 3 4 + * 5 -
        assertEquals(7, rpn.size());
    }

    @Test
    void testParseMismatchedParentheses() {
        assertThrows(ExpressionException.class, () -> {
            List<Token> tokens = tokenizer.tokenize("(2 + 3");
            parser.parse(tokens);
        });
    }

    @Test
    void testParseWithVariable() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("x + y * 2");
        List<Token> rpn = parser.parse(tokens);

        assertEquals(5, rpn.size());
        assertEquals(TokenType.VARIABLE, rpn.get(0).type());
        assertEquals(TokenType.VARIABLE, rpn.get(1).type());
    }
}