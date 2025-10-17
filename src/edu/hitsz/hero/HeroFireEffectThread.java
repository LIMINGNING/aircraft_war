package edu.hitsz.hero;

/**
 * 英雄机临时火力线程类
 * 用于两种火力道具的"应用+定时恢复"
 * 
 * @author hitsz
 */
public class HeroFireEffectThread extends Thread {

    private final Runnable applyNow;
    private final Runnable revert;
    private final long durationMs;

    public HeroFireEffectThread(String name, long durationMs, Runnable applyNow, Runnable revert) {
        this.durationMs = durationMs;
        this.applyNow = applyNow;
        this.revert = revert;
        setName(name);
        setDaemon(true);
    }

    @Override
    public void run() {
        boolean interrupted = false;
        if (applyNow != null)
            applyNow.run();

        try {
            Thread.sleep(durationMs);
        } catch (InterruptedException e) {
            interrupted = true;
        }

        // 被新效果中断时不回退，由新效果负责接管
        if (!interrupted && revert != null) {
            revert.run();
        }
    }
}