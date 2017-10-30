package com.learn.multithreading.volatile_examples;

import com.learn.multithreading.volatile_examples.*;
import org.junit.Test;

/*
 * example inspired from http://tutorials.jenkov.com/java-concurrency/volatile.html
 */
public class VolatileTestExample {
    static int volatileIntValue = 0;
    int maxIterations = 1000;

    /*
        * this test is to exemplify volatile variable usage
    */
    @Test
    public void testVolatile(){
        ChangeValueEngine engine = new ChangeValueEngine(maxIterations, new SharedVolatile());

        Thread t1 = getPutThread("t1", engine, maxIterations);
        Thread t2 = getTakeThread("t2", engine, maxIterations);

        executeThreads("Test Volatile", t1, t2);
    }

    /*
       * this test is to exemplify what happnes if instead of a volatile variable a non volatile is used
       * the threads will remain blocked
   */
    @Test
    public void testNonVolatile(){
        ChangeValueEngine engine = new ChangeValueEngine(maxIterations, new SharedNonVolatile());

        Thread t1 = getPutThread("t1", engine, maxIterations);
        Thread t2 = getTakeThread("t2", engine, maxIterations);

        executeWithInfiniteLoopThreads("Test non volatile", t1, t2);
    }

    /*
      * this test is to exemplify what happnes if we use a non volatile variable by engine class has a
        dummy volatile variable that will cause other variables become available to all threads
      * the threads will not block
    */
    @Test
    public void testNonVolatileWithATrick(){
        ChangeValueEngineWithATrick engine = new ChangeValueEngineWithATrick(maxIterations, new SharedNonVolatile());

        Thread t1 = getPutThread("t1", engine, maxIterations);
        Thread t2 = getTakeThread("t2", engine, maxIterations);

        executeThreads("Test non volatile value with a dummy volatile field", t1, t2);
    }

    /*
      * this test is to exemplify what happnes if have no volatile variables byt we have a call to Thread.interrupted() method
      * the threads will not block
    */
    @Test
    public void testNonVolatileWithAInteruptionTrick(){
        ChangeValueEngineWithAInteruptionTrick engine = new ChangeValueEngineWithAInteruptionTrick(maxIterations, new SharedNonVolatile());

        Thread t1 = getPutThread("t1", engine, maxIterations);
        Thread t2 = getTakeThread("t2", engine, maxIterations);

        executeThreads("Test non volatile value with a a call to Thread.interupted()", t1, t2);
    }

    /*
     * this test is to compare speed of running threads with volatile and with synchronized on this object
     * the threads will not block
    */
    @Test
    public void compareVolatileAndSyncronizedOnThis(){
        ChangeValueEngineWithSynchronizedOnThis engineSynchronized = new ChangeValueEngineWithSynchronizedOnThis(maxIterations, new SharedNonVolatile());
        ChangeValueEngine engineVolatile = new ChangeValueEngine(maxIterations, new SharedVolatile());

        Thread t1Sybcronized = getPutThread("t1", engineSynchronized, maxIterations);
        Thread t2Syncronized = getTakeThread("t2", engineSynchronized, maxIterations);

        Thread t1Volatile = getPutThread("t1", engineVolatile, maxIterations);
        Thread t2Volatile = getTakeThread("t2", engineVolatile, maxIterations);

        executeThreads("Synchronized", t1Sybcronized, t2Syncronized);
        executeThreads("Volatile    ", t1Volatile, t2Volatile);
    }

    /*
        * this test is to compare speed of running threads with volatile and with synchronized on different objects
        * the threads will not block
    */
    @Test
    public void compareVolatileAndSyncronizedOnDifferentObjects(){
        ChangeValueEngineWithSynchronized engineSynchronized = new ChangeValueEngineWithSynchronized(maxIterations, new SharedNonVolatile());
        ChangeValueEngine engineVolatile = new ChangeValueEngine(maxIterations, new SharedVolatile());

        Thread t1Sybcronized = getPutThread("t1", engineSynchronized, maxIterations);
        Thread t2Syncronized = getTakeThread("t2", engineSynchronized, maxIterations);

        Thread t1Volatile = getPutThread("t1", engineVolatile, maxIterations);
        Thread t2Volatile = getTakeThread("t2", engineVolatile, maxIterations);

        executeThreads("Synchronized", t1Sybcronized, t2Syncronized);
        executeThreads("Volatile    ", t1Volatile, t2Volatile);
    }

    public void executeThreads(String message, Thread t1, Thread t2){
        long start = System.nanoTime();
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();

        long timeElapsed = (end - start)/1000;

        System.out.println(String.format(message + " example: " + timeElapsed));
    }

    public void executeWithInfiniteLoopThreads(String message, Thread t1, Thread t2){
        long start = System.nanoTime();
        t1.start();
        t2.start();
        int lastvolatileIntValue = volatileIntValue;
        sleep(2000);

        int i=0;
        int maxSamePairs=10;
        int samePairs=0;
        while(i++<maxIterations){
            if(lastvolatileIntValue == volatileIntValue){
                sleep(1000);
                samePairs++;
            } else {
                samePairs = 0;
                lastvolatileIntValue = volatileIntValue;
            }
            if(samePairs>maxSamePairs) break;
        }
        long end = System.nanoTime();

        long timeElapsed = (end - start)/1000;

        System.out.println(String.format(message + " example: " + timeElapsed));
    }

    public void sleep(long milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Thread getPutThread(String threadName, Engine engine, int maxIterations){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i++<maxIterations){
                    engine.print(threadName + " before iteration ", i);
                    engine.put(i);
                    engine.print(threadName + "  after iteration ", i);
                }
            }
        });
    }

    public Thread getTakeThread(String threadName, Engine engine, int maxIterations){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(i++<maxIterations){
                    volatileIntValue = engine.take();
                    engine.print(threadName + "  after  iteration ", i);
                }
            }
        });
    }
}
