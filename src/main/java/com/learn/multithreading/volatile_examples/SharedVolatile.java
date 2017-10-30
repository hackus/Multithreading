package com.learn.multithreading.volatile_examples;

public class SharedVolatile implements SharedVariable {
    volatile boolean sharedValue;

    @Override
    public boolean getValue() {
        return sharedValue;
    }

    @Override
    public void setValue(boolean value) {
        this.sharedValue = value;
    }
}
