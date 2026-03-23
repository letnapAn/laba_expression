package org.example.expression.tokenizer;

import org.example.expression.ExpressionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для Tokenizer.
 */
class TokenizerTest {

    private Tokenizer tokenizer;

    @BeforeEach
    void setUp() {
        tokenizer = new Tokenizer();
    }

    @Test
    void testTokenizeSimpleNumber() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("42");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type());
        assertEquals("42", tokens.get(0).value());
    }

    @Test
    void testTokenizeSimpleExpression() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("2 + 3 * 4");
        assertEquals(5, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type());
        assertEquals(TokenType.PLUS, tokens.get(1).type());
        assertEquals(TokenType.NUMBER, tokens.get(2).type());
        assertEquals(TokenType.MULTIPLY, tokens.get(3).type());
        assertEquals(TokenType.NUMBER, tokens.get(4).type());
    }

    @Test
    void testTokenizeWithBrackets() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("(2 + 3) * 4");
        assertEquals(7, tokens.size());
        assertEquals(TokenType.LBRACKET, tokens.get(0).type());
        assertEquals(TokenType.RBRACKET, tokens.get(4).type());
    }

    @Test
    void testTokenizeWithVariable() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("x + 5");
        assertEquals(3, tokens.size());
        assertEquals(TokenType.VARIABLE, tokens.get(0).type());
        assertEquals("x", tokens.get(0).value());
    }

    @Test
    void testTokenizeWithFunction() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("sin(x)");
        assertEquals(4, tokens.size());
        assertEquals(TokenType.FUNCTION, tokens.get(0).type());
        assertEquals("sin", tokens.get(0).value());
        assertEquals(TokenType.LBRACKET, tokens.get(1).type());
        assertEquals(TokenType.VARIABLE, tokens.get(2).type());
        assertEquals(TokenType.RBRACKET, tokens.get(3).type());
    }

    @Test
    void testTokenizeInvalidCharacter() {
        assertThrows(ExpressionException.class, () -> {
            tokenizer.tokenize("2 @ 3");
        });
    }

    @Test
    void testTokenizeDecimalNumber() throws ExpressionException {
        List<Token> tokens = tokenizer.tokenize("3.14 + 2.5");
        assertEquals(3, tokens.size());
        assertEquals("3.14", tokens.get(0).value());
        assertEquals("2.5", tokens.get(2).value());
    }
}