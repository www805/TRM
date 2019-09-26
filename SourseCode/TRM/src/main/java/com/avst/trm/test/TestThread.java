package com.avst.trm.test;

public class TestThread extends Thread {

    static TestThread testThread=new TestThread();
    int i=0;

    public  TestThread(){}

    public  TestThread(int n){

        synchronized (testThread){
            System.out.println(n+":n");
        }
        i = n;
    }

    @Override
    public void run() {

        synchronized (testThread){
            System.out.println(i);
        }

    }
}
