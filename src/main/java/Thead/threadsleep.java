package Thead;


import java.util.Date;

/**
 * Created by wujiao on 2016/12/25.
 */
public class threadsleep extends Thread {
    public static void main(String args[]) {
        threadsleep a = new threadsleep();
        threadsleep b = new threadsleep();
        threadsleep c = new threadsleep();
        a.start();
        a.yield();
        b.start();
        c.start();

    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.print(getName() + new Date());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


