package org.example.expression.tokenizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для Token.
 */
class TokenTest {

    @Test
    void testNumberToken() {
        Token token = new Token(TokenType.NUMBER, "123.45");

        assertEquals(TokenType.NUMBER, token.type());
        assertEquals("123.45", token.value());
    }

    @Test
    void testOperatorToken() {
        Token plus = new Token(TokenType.PLUS, "+");
        Token multiply = new Token(TokenType.MULTIPLY, "*");

        assertEquals("+", plus.value());
        assertEquals("*", multiply.value());
    }

    @Test
    void testBracketTokens() {
        Token left = new Token(TokenType.LBRACKET, "(");
        Token right = new Token(TokenType.RBRACKET, ")");

        assertEquals(TokenType.LBRACKET, left.type());
        assertEquals(TokenType.RBRACKET, right.type());
    }

    @Test
    void testVariableToken() {
        Token token = new Token(TokenType.VARIABLE, "x");

        assertEquals(TokenType.VARIABLE, token.type());
        assertEquals("x", token.value());
    }

    @Test
    void testFunctionToken() {
        Token token = new Token(TokenType.FUNCTION, "sin");

        assertEquals(TokenType.FUNCTION, token.type());
        assertEquals("sin", token.value());
    }

    @Test
    void testEqualsAndHashCode() {
        Token token1 = new Token(TokenType.NUMBER, "42");
        Token token2 = new Token(TokenType.NUMBER, "42");
        Token token3 = new Token(TokenType.NUMBER, "100");

        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
        assertNotEquals(token1, token3);
    }

    @Test
    void testToString() {
        Token token = new Token(TokenType.NUMBER, "3.14");
        String str = token.toString();

        assertTrue(str.contains("NUMBER"));
        assertTrue(str.contains("3.14"));
    }
}