package Thead;

/**
 * Created by wujiao on 2016/12/25.
 */
public class runabledemo {
    public static void main(String args[]) {
        runable imrun = new runable();
        Thread a = new Thread(imrun, "a");
        Thread b = new Thread(imrun, "b");
        Thread c = new Thread(imrun, "c");
        a.start();
        b.start();
        c.start();

    }


}

