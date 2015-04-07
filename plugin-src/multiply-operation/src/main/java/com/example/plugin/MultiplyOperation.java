package com.example.plugin;

import com.example.sdk.BinaryOperation;

/**
 * This class performs multiplication operation on two numbers
 */
public class MultiplyOperation implements BinaryOperation {

    @Override
    public String getName() {
        return "Multiplication";
    }

    @Override
    public Number operate(Number num1, Number num2) {
        return num1.doubleValue() * num2.doubleValue();
    }
}
