package com.example.plugin;

import com.example.sdk.BinaryOperation;

/**
 * This class performs division operations on two numbers
 */
public class DivideOperation implements BinaryOperation {

    @Override
    public String getName() {
        return "Division";
    }

    @Override
    public Number operate(Number num1, Number num2) {
        return num1.doubleValue() / num2.doubleValue();
    }
}
