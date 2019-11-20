package com.zptioning.code.chapter04;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 6-1
 */
public class A01_MultiThread {

    public static void main(String[] args) {
        // 获取Java线程管理MXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // 不需要获取同步的monitor和synchronizer信息，仅仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        // 遍历线程信息，仅打印线程ID和线程名称信息
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("[" + threadInfo.getThreadId() + "] " +
                    threadInfo.getThreadName());
        }
    }
}
/*
可以看到，一个Java程序的运行不仅仅是main()方法的运行，而是
main 线程和多个其他线程的同时运行。
[4] Signal Dispatcher   // 分发处理 发送给 JVM 信号的线程
[3] Finalizer           // 调用对象 finalize 方法的线程
[2] Reference Handler   // 清除 Reference 的线程
[1] main                // main线程，用户程序入口
* */
