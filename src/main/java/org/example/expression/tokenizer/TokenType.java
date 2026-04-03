package org.example.expression.tokenizer;

/**
 * Типы токенов для лексического анализа.
 */
public enum TokenType {
    /** Число (целое или дробное) */
    NUMBER,
    /** Оператор сложения */
    PLUS,
    /** Оператор вычитания */
    MINUS,
    /** Оператор умножения */
    MULTIPLY,
    /** Оператор деления */
    DIVIDE,
    /** Открывающая скобка */
    LBRACKET,
    /** Закрывающая скобка */
    RBRACKET,
    /** Имя функции */
    FUNCTION,
    /** Имя переменной */
    VARIABLE
}