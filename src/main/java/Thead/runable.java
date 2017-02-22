package Thead;

import java.util.Date;

/**
 * Created by wujiao on 2016/12/25.
 */
public class runable implements Runnable {
    public void run() {
        //由于接口，不能直接实现Thread类的方法
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + new Date());

        }
    }
}
