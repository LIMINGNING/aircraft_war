package edu.hitsz.music;

/**
 * 音效管理器
 * 全局管理音效开关状态
 * 
 * @author hitsz
 */
public class SoundManager {
    private static boolean soundEnabled = true;

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
}