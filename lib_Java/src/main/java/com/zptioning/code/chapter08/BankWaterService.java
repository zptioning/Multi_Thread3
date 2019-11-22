package com.zptioning.code.chapter08;

import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BankWaterService implements Runnable {

    /**
     * 创建4个屏障，处理完之后执行当前类的run方法
     */
    private CyclicBarrier mCyclicBarrier = new CyclicBarrier(4, this);

    /**
     * 假设只有4个 sheet，所以只启动4个线程
     */
    private Executor mExecutor = Executors.newFixedThreadPool(4);

    /**
     * 保存每个sheet计算出的银行流水结果
     */
    private ConcurrentHashMap<String, Integer> sheetBankWaterCount =
            new ConcurrentHashMap<>();


    /**
     * 计算
     */
    private void count() {
        for (int i = 0; i < 4; i++) {
            final int finalI = i;
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // 计算当前sheet的银行流水数据，
                    sheetBankWaterCount.put(Thread.currentThread().getName(), finalI);
                    System.out.println(Thread.currentThread().getName() + " ： " + finalI);
                    // 银行流水计算完成，插入一个屏障
                    try {
                        mCyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override

    public void run() {
        System.out.println("begin running!!!!");
        int result = 0;
        // 汇总每个sheet计算出的结果
        for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
            result += sheet.getValue();
        }
        // 将结果输出
        sheetBankWaterCount.put("result", result);
        System.out.println(result);

    }


    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}









