package com.dfdyz.lcaddon.synthesizer.audio.buffer;

import com.dfdyz.lcaddon.synthesizer.SimpleSynthesizer;
import com.dfdyz.lcaddon.synthesizer.audio.sound.SynthSoundInstance;
import net.minecraft.client.sounds.AudioStream;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.BufferUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RTTimbreStream implements AudioStream {
    //private final ClientBufferMusicSound soundInstance;
    private final AudioFormat audioFormat;
    private final SynthSoundInstance soundInstance;
    private final SimpleSynthesizer synth;
    private int offset = 0;
    private int count;

    public RTTimbreStream(SynthSoundInstance soundInstance, float sampleRate, float frameRate) {
        audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // 编码格式
                44100, // 采样率
                16, // 采样大小
                1, // 通道数
                2, // 帧大小
                44100, // 每秒帧数
                false); // 大小端顺序
        this.count = 0;
        this.synth = soundInstance.synth;
        this.soundInstance = soundInstance;
    }

    @Override
    public AudioFormat getFormat() {
        return audioFormat;
    }

    long last = 0;
    @Override
    public ByteBuffer read(int size) {
        if(offset != 0) System.out.println(System.currentTimeMillis() - last);
        last = System.currentTimeMillis();

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(size);
        //System.out.println("org: " + size);
        byte[] pcmData = synth.generateSoundByte(soundInstance, offset, offset + size);
        offset += size;

        //System.out.println(pcmData.length);
        AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(pcmData), // PCM数据输入流
                audioFormat,
                pcmData.length / audioFormat.getFrameSize());

        try {
            byte[] bytes = IOUtils.toByteArray(audioInputStream);
            //System.out.println("out: " + bytes.length);
            byteBuffer.put(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("off: " + offset);

        byteBuffer.flip();
        return byteBuffer;
    }

    @Override
    public void close() {
        soundInstance.noteOff();
    }


}
