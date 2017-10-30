package com.learn.multithreading.volatile_examples;

public interface Engine {
    void put(Integer intValue);
    Integer take();
    void print(String threadName, int iteration);
}
