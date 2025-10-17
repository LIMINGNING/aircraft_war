package edu.hitsz.music;

import javax.sound.sampled.*;
import java.io.*;

/**
 * 统一音效线程类
 * 支持循环播放和单次播放两种模式
 * 
 * @author hitsz
 */
public class SoundThread extends Thread {

    private final String filename;
    private volatile boolean running = true;
    private final boolean isLoop; // 是否循环播放

    private AudioFormat audioFormat;
    private byte[] samples;

    /**
     * 构造函数 - 单次播放
     * 
     * @param filename 音频文件路径
     */
    public SoundThread(String filename) {
        this(filename, false);
    }

    /**
     * 构造函数 - 指定播放模式
     * 
     * @param filename 音频文件路径
     * @param isLoop   是否循环播放
     */
    public SoundThread(String filename, boolean isLoop) {
        this.filename = filename;
        this.isLoop = isLoop;
        setName("SoundThread-" + System.currentTimeMillis() + (isLoop ? "-Loop" : "-Once"));
        setDaemon(true);
        init();
    }

    private void init() {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename))) {
            audioFormat = stream.getFormat();
            samples = readAllSamples(stream, audioFormat);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            running = false;
        }
    }

    private static byte[] readAllSamples(AudioInputStream stream, AudioFormat format) throws IOException {
        int size = (int) (stream.getFrameLength() * format.getFrameSize());
        byte[] data = new byte[size];
        DataInputStream dis = new DataInputStream(stream);
        dis.readFully(data);
        return data;
    }

    private void playOnce(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];

        SourceDataLine dataLine = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
            dataLine.start();

            int numBytesRead = 0;
            while (running && numBytesRead != -1) {
                numBytesRead = source.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                    dataLine.write(buffer, 0, numBytesRead);
                }
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        } finally {
            if (dataLine != null) {
                try {
                    dataLine.drain();
                } catch (Exception ignored) {
                }
                try {
                    dataLine.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * 停止播放（主要用于循环模式）
     */
    public void stopPlay() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        if (samples == null || samples.length == 0)
            return;

        if (isLoop) {
            // 循环播放模式
            while (running) {
                InputStream in = new ByteArrayInputStream(samples);
                playOnce(in);
            }
        } else {
            // 单次播放模式
            InputStream in = new ByteArrayInputStream(samples);
            playOnce(in);
        }
    }

    /**
     * 创建循环播放的音效线程
     * 
     * @param filename 音频文件路径
     * @return 循环播放线程
     */
    public static SoundThread createLoopSound(String filename) {
        return new SoundThread(filename, true);
    }

    /**
     * 创建单次播放的音效线程
     * 
     * @param filename 音频文件路径
     * @return 单次播放线程
     */
    public static SoundThread createOnceSound(String filename) {
        return new SoundThread(filename, false);
    }

    /**
     * 播放单次音效的便捷方法
     * 
     * @param filename 音频文件路径
     */
    public static void playOnceSound(String filename) {
        createOnceSound(filename).start();
    }
}