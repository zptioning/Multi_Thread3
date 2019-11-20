package com.zptioning.code.chapter04;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 6-3
 */
public class A03_ThreadState {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new Thread(new TimeWaiting(), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        // 使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
        new Thread(new Sync(), "SyncThread-1").start();
        new Thread(new Sync(), "SyncThread-2").start();
    }

    /**
     * 该线程不断的进行睡眠
     */
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(100);
            }
        }
    }

    /**
     * 该线程在Waiting.class实例上等待
     */
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 该线程在Blocked.class实例上加锁后，不会释放该锁
     */
    static class Blocked implements Runnable {
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(100);
                }
            }
        }
    }

    static class Sync implements Runnable {

        @Override
        public void run() {
            lock.lock();
            try {
                SleepUtils.second(100);
            } finally {
                lock.unlock();
            }

        }

    }
}
/*
Full thread dump OpenJDK 64-Bit Server VM (25.202-b49-5587405 mixed mode):

"Attach Listener" #16 daemon prio=9 os_prio=31 tid=0x00007f898402c800 nid=0x1007 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #15 prio=5 os_prio=31 tid=0x00007f8982840000 nid=0x1803 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"SyncThread-2" #14 prio=5 os_prio=31 tid=0x00007f898383a000 nid=0x5503 waiting on condition [0x000070000c019000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000007955f68a8> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at com.zptioning.code.chapter04.A03_ThreadState$Sync.run(A03_ThreadState.java:70)
	at java.lang.Thread.run(Thread.java:748)

"SyncThread-1" #13 prio=5 os_prio=31 tid=0x00007f898283f800 nid=0x4003 waiting on condition [0x000070000bf16000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at java.lang.Thread.sleep(Thread.java:340)
	at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
	at com.zptioning.code.chapter04.SleepUtils.second(SleepUtils.java:11)
	at com.zptioning.code.chapter04.A03_ThreadState$Sync.run(A03_ThreadState.java:72)
	at java.lang.Thread.run(Thread.java:748)

"BlockedThread-2" #12 prio=5 os_prio=31 tid=0x00007f8983839000 nid=0x4303 waiting for monitor entry [0x000070000be13000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.zptioning.code.chapter04.A03_ThreadState$Blocked.run(A03_ThreadState.java:60)
	- waiting to lock <0x00000007955fe9d8> (a java.lang.Class for com.zptioning.code.chapter04.A03_ThreadState$Blocked)
	at java.lang.Thread.run(Thread.java:748)

"BlockedThread-1" #11 prio=5 os_prio=31 tid=0x00007f898283e800 nid=0x3f03 waiting on condition [0x000070000bd10000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at java.lang.Thread.sleep(Thread.java:340)
	at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
	at com.zptioning.code.chapter04.SleepUtils.second(SleepUtils.java:11)
	at com.zptioning.code.chapter04.A03_ThreadState$Blocked.run(A03_ThreadState.java:60)
	- locked <0x00000007955fe9d8> (a java.lang.Class for com.zptioning.code.chapter04.A03_ThreadState$Blocked)
	at java.lang.Thread.run(Thread.java:748)

"WaitingThread" #10 prio=5 os_prio=31 tid=0x00007f8984026000 nid=0x3d03 in Object.wait() [0x000070000bc0d000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x00000007955fbca0> (a java.lang.Class for com.zptioning.code.chapter04.A03_ThreadState$Waiting)
	at java.lang.Object.wait(Object.java:502)
	at com.zptioning.code.chapter04.A03_ThreadState$Waiting.run(A03_ThreadState.java:44)
	- locked <0x00000007955fbca0> (a java.lang.Class for com.zptioning.code.chapter04.A03_ThreadState$Waiting)
	at java.lang.Thread.run(Thread.java:748)

"TimeWaitingThread" #9 prio=5 os_prio=31 tid=0x00007f8983838800 nid=0x3b03 waiting on condition [0x000070000bb0a000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at java.lang.Thread.sleep(Thread.java:340)
	at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
	at com.zptioning.code.chapter04.SleepUtils.second(SleepUtils.java:11)
	at com.zptioning.code.chapter04.A03_ThreadState$TimeWaiting.run(A03_ThreadState.java:30)
	at java.lang.Thread.run(Thread.java:748)

"Service Thread" #8 daemon prio=9 os_prio=31 tid=0x00007f8983000800 nid=0x4603 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread2" #7 daemon prio=9 os_prio=31 tid=0x00007f8984021800 nid=0x4803 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #6 daemon prio=9 os_prio=31 tid=0x00007f8982852800 nid=0x4a03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #5 daemon prio=9 os_prio=31 tid=0x00007f8982849000 nid=0x4c03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007f8984017000 nid=0x3703 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007f8984016000 nid=0x3003 in Object.wait() [0x000070000b3f5000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:144)
	- locked <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:165)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:216)

"Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007f8984015800 nid=0x5203 in Object.wait() [0x000070000b2f2000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=31 tid=0x00007f898400e800 nid=0x2e03 runnable

"GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007f8982806800 nid=0x1c07 runnable

"GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007f8982807000 nid=0x2a03 runnable

"GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007f8982807800 nid=0x5403 runnable

"GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007f8982813800 nid=0x2d03 runnable

"VM Periodic Task Thread" os_prio=31 tid=0x00007f898283c000 nid=0x3903 waiting on condition

JNI global references: 5
* */