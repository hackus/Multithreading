package com.learn.multithreading.volatile_examples;

class ChangeValueEngineWithSynchronized implements Engine {
    public final int maxIterations;
    int putIterations = 0;
    int takeIterations = 0;
    public Integer intValue = 1;
    public SharedVariable hasNextObject;
    private Object putSynchronized = new Object();
    private Object takeSynchronized = new Object();

    public ChangeValueEngineWithSynchronized(int maxIterations, SharedVariable shared){
        this.maxIterations = maxIterations;
        hasNextObject = shared;
    }

    public void put(Integer intValue){
        synchronized (putSynchronized) {
            if (hasNextObject.getValue() && isPutFinalized()) {
                putIterations++;
                this.intValue = intValue;
                hasNextObject.setValue(true);
            }
        }
    }

    public synchronized Integer take(){
        synchronized (takeSynchronized) {
            if (!hasNextObject.getValue() && isTakeFinalized()) {
                takeIterations++;
                hasNextObject.setValue(false);
            }
        }
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