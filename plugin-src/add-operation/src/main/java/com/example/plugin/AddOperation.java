package com.example.plugin;

import com.example.sdk.BinaryOperation;

/**
 * This class performs addition operation on  two numbers
 */
public class AddOperation implements BinaryOperation {

    @Override
    public String getName() {
        return "Addition";
    }

    @Override
    public Number operate(Number num1, Number num2) {
        return num1.doubleValue() + num2.doubleValue();
    }
}
