package org.example;

import org.example.expression.Expression;

public class Main {
    public static void main(String[] args) {
        Expression expression = new Expression("a*x+y-2/y");
        System.out.println(expression.calc());
    }
}