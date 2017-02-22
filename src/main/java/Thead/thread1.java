package Thead;

/**
 * Created by wujiao on 2016/12/25.
 * 不是所有代码都需要被线程执行，Thread类中的run()方法包含哪些需要被线程执行的代码
 * run()方法直接调用个就相当于普通方法调用
 * run()方法和start()方法
 * start()：首先启动了一个线程，再由JVM去调用该线程的run()方法
 */
//    public Thread() {
//        init(null, null, "Thread-" + nextThreadNum(), 0);
//                }
//
public class thread1 extends Thread {
    public static void main(String args[]) {
        //创建两个线程对象
        Thread.currentThread().setName("主线程名字");
        //获取当前主线程，main的线程名
        System.out.println(Thread.currentThread().getName());

        thread1 my = new thread1();
        //获取线程优先级
        System.out.println(my.getPriority());
        my.start();
        thread2 my2 = new thread2("线程b");
        my2.setPriority(10);
        my2.start();
    }

    @Override
    public void run() {
        //第一种设置线程名的方法
        setName("线程a");
        for (int i = 0; i < 100; i++) {
            System.out.print(getName() + " " + i + " ");
        }

        //调用父亲的run方法  super.run();
    }
}
