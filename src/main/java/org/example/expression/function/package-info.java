/**
 * Математические функции, реестр операций и провайдер переменных.
 * <p>
 * Содержит контракт {@link org.example.expression.function.Function},
 * централизованный реестр {@link org.example.expression.function.FunctionRegistry}
 * и механизм кэширования ввода {@link org.example.expression.function.VariableProvider}.
 * Позволяет расширять набор операций без изменения ядра парсера (принцип OCP).
 *
 * @see org.example.expression.function.Function
 * @see org.example.expression.function.VariableProvider
 */
package org.example.expression.function;