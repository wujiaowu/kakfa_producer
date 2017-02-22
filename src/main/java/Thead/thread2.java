package Thead;

/**
 * Created by wujiao on 2016/12/25.
 */
public class thread2 extends Thread {
    thread2() {
    }

    thread2(String name) {
        super.setName(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            //继承的Thread，中无参构造
            System.out.print(getName() + " " + i + " ");

        }
    }
}
