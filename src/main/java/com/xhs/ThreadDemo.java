package com.xhs;

/**
 * @author xuhan  build  2019/2/13
 */
public class ThreadDemo implements Runnable{

    private  int i=0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadDemo());
        System.out.println(t1.isAlive());
        t1.start();
        System.out.println(t1.isAlive());
        System.out.println(t1.isAlive());
    }
    public void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(i);
            }
        }).start();
    }

    @Override
    public void run() {
        synchronized (Thread.currentThread()){
            System.out.println("开始");
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束");
        }
    }
}
