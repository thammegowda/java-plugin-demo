package com.example.sdk;


/**
 * A sample interface which defines a contract for plugins.
 * This interface defines contracts for binary operation on number
 */
public interface BinaryOperation{

    /**
     * Gets name of operation
     * @return name of operation
     */
    public String getName();

    /**
     * performs operation on operands to return the result. The operation is
     * a choice of implementer
     * @param num1 operand 1
     * @param num2 operand 2
     * @return the result
     */
    public Number operate(Number num1, Number num2);
}
