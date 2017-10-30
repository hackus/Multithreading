package com.learn.multithreading.volatile_examples;

class ChangeValueEngineWithATrick implements Engine {
    volatile boolean dummyValue=true;
    public final int maxIterations;
    int putIterations = 0;
    int takeIterations = 0;
    public Integer intValue = 1;
    public SharedVariable hasNextObject;

    public ChangeValueEngineWithATrick(int maxIterations, SharedVariable shared){
        this.maxIterations = maxIterations;
        hasNextObject = shared;
    }

    public void put(Integer intValue){
        while(hasNextObject.getValue() && isPutFinalized() && dummyValue){}
        putIterations++;
        this.intValue = intValue;
        hasNextObject.setValue(true);
    }

    public Integer take(){
        while(!hasNextObject.getValue() && isTakeFinalized() && dummyValue){}
        takeIterations++;
        hasNextObject.setValue(false);
        return intValue;
    }

    public void print(String threadName, int iteration){
//        System.out.println(String.format("%s %d intValue = %d, hasNextObject = %s",threadName, iteration, intValue, hasNextObject.getValue()));
    }

    private boolean isPutFinalized(){
        return putIterations<maxIterations;
    }

    private boolean isTakeFinalized(){
        return takeIterations<maxIterations;
    }
}