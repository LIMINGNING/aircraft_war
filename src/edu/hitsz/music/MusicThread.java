package edu.hitsz.music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;

/**
 * 音乐播放线程类
 * 支持循环播放、音效控制、播放停止等功能
 */
public class MusicThread extends Thread {

    // 音频文件名
    private String filename;
    private AudioFormat audioFormat;
    private byte[] samples;

    // 控制播放状态
    private volatile boolean isPlaying = false;
    private volatile boolean shouldLoop = false;
    private volatile boolean shouldStop = false;

    // 音频播放线程控制
    private SourceDataLine dataLine;

    // 静态音效开关
    private static boolean soundEnabled = true;

    /**
     * 构造函数
     * 
     * @param filename 音频文件路径
     */
    public MusicThread(String filename) {
        this.filename = filename;
        loadMusic();
    }

    /**
     * 构造函数（支持循环播放设置）
     * 
     * @param filename 音频文件路径
     * @param loop     是否循环播放
     */
    public MusicThread(String filename, boolean loop) {
        this.filename = filename;
        this.shouldLoop = loop;
        loadMusic();
    }

    /**
     * 加载音频文件
     */
    private void loadMusic() {
        try {
            // 定义一个AudioInputStream用于接收输入的音频数据，使用AudioSystem来获取音频的音频输入流
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            // 用AudioFormat来获取AudioInputStream的格式
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
            stream.close();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("不支持的音频文件格式: " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("读取音频文件失败: " + filename);
            e.printStackTrace();
        }
    }

    /**
     * 从音频流中获取采样数据
     * 
     * @param stream 音频输入流
     * @return 音频采样数据
     */
    private byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        DataInputStream dataInputStream = new DataInputStream(stream);
        try {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            System.err.println("读取音频数据失败");
            e.printStackTrace();
        }
        return samples;
    }

    /**
     * 播放音频
     * 
     * @param source 音频数据输入流
     */
    private void play(InputStream source) {
        if (!soundEnabled || samples == null) {
            return;
        }

        int bufferSize = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[bufferSize];

        // 获取受数据行支持的音频格式DataLine.info
        Info info = new Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, bufferSize);
        } catch (LineUnavailableException e) {
            System.err.println("音频设备不可用");
            e.printStackTrace();
            return;
        }

        dataLine.start();
        isPlaying = true;

        try {
            int numBytesRead = 0;
            while (numBytesRead != -1 && !shouldStop) {
                // 从音频流读取指定的最大数量的数据字节，并将其放入缓冲区中
                numBytesRead = source.read(buffer, 0, buffer.length);
                // 通过此源数据行将数据写入混频器
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }
        } catch (IOException ex) {
            System.err.println("播放音频时发生错误");
            ex.printStackTrace();
        }

        if (dataLine != null) {
            dataLine.drain();
            dataLine.close();
        }
        isPlaying = false;
    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        shouldStop = true;
        shouldLoop = false;

        if (dataLine != null && dataLine.isOpen()) {
            dataLine.stop();
            dataLine.close();
        }

        isPlaying = false;
    }

    /**
     * 设置是否循环播放
     * 
     * @param loop 是否循环播放
     */
    public void setLoop(boolean loop) {
        this.shouldLoop = loop;
    }

    /**
     * 检查是否正在播放
     * 
     * @return 是否正在播放
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 设置全局音效开关
     * 
     * @param enabled 是否启用音效
     */
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    /**
     * 获取全局音效开关状态
     * 
     * @return 音效是否启用
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * 播放一次性音效（非阻塞）
     * 
     * @param filename 音效文件路径
     */
    public static void playSound(String filename) {
        if (!soundEnabled) {
            return;
        }

        new Thread(() -> {
            MusicThread soundEffect = new MusicThread(filename, false);
            soundEffect.run();
        }).start();
    }

    /**
     * 播放背景音乐（循环播放）
     * 
     * @param filename 背景音乐文件路径
     * @return MusicThread实例，用于控制播放
     */
    public static MusicThread playBackgroundMusic(String filename) {
        MusicThread bgMusic = new MusicThread(filename, true);
        bgMusic.start();
        return bgMusic;
    }

    /**
     * 播放Boss音乐（循环播放）
     * 
     * @param filename Boss音乐文件路径
     * @return MusicThread实例，用于控制播放
     */
    public static MusicThread playBossMusic(String filename) {
        MusicThread bossMusic = new MusicThread(filename, true);
        bossMusic.start();
        return bossMusic;
    }

    @Override
    public void run() {
        do {
            shouldStop = false;
            if (samples != null) {
                InputStream stream = new ByteArrayInputStream(samples);
                play(stream);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 如果需要循环播放且没有被要求停止，则继续播放
            if (shouldLoop && !shouldStop) {
                try {
                    Thread.sleep(100); // 短暂休息，避免无缝循环可能产生的音频问题
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } while (shouldLoop && !shouldStop);
    }
}
