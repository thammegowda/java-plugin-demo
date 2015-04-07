package com.example.plugin;

import com.example.sdk.BinaryOperation;

/**
 * This class performs subtraction of two numbers
 */
public class SubtractOperation implements BinaryOperation {

    @Override
    public String getName() {
        return "Subtraction";
    }

    @Override
    public Number operate(Number num1, Number num2) {
        return num1.doubleValue() - num2.doubleValue();
    }
}
