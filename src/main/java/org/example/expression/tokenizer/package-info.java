/**
 * Компоненты лексического и синтаксического анализа.
 * <p>
 * Отвечают за преобразование строкового представления выражения в поток токенов
 * и последующую конвертацию инфиксной записи в обратную польскую (RPN)
 * с использованием алгоритма <b>Shunting Yard</b>.
 *
 * @see org.example.expression.tokenizer.Tokenizer
 * @see org.example.expression.tokenizer.Parser
 */
package org.example.expression.tokenizer;