package com.zptioning.code.chapter04;

/**
 * 6-5
 * mian方法执行完毕，此时Java虚拟机中已经没有非Daemon线程，虚拟机要退出。
 * Java虚拟机中的所有Daemon线程都需要立即终止，因为DaemonRunner立即终止，
 * 但是，DaemonRunner中的finally块并没有执行。
 */
public class A05_Daemon {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner());
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(100);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }
}
