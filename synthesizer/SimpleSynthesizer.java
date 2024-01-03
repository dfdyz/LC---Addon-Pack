package com.dfdyz.lcaddon.synthesizer;

import com.dfdyz.lcaddon.synthesizer.audio.sound.SynthSoundInstance;

public class SimpleSynthesizer {
    public byte[] generateSoundByte(SynthSoundInstance soundInstance, int from, int to){
        return generatePCMData(soundInstance, from, to);
    }

    public boolean shouldNoteStop(SynthSoundInstance soundInstance){
        return System.currentTimeMillis() - soundInstance.offtime > 1000L;
    }

    //from: https://blog.51cto.com/u_16175518/7007760
    private static byte[] generatePCMData(SynthSoundInstance soundInstance, int from, int to) {// 音频时长（秒）
        // 在这里生成你的PCM音频数据
        // 这里只是一个示例，生成一个简单的音频数据（440Hz的正弦波）

        int sampleRate = 44100;
        double timePerSample = 1. / sampleRate;
        int start = from / 8;
        int end = to / 8;

        double startTime = from / (8.f * sampleRate);
        double endTime = to / (8.f * sampleRate);
        double duration = endTime - startTime;
        int numSamples = (to - from) / 8;
        double[] buffer = new double[numSamples];
        double amplitude = 0.5;
        double frequency = 440.0;

        //System.out.println(numSamples);

        if(soundInstance.isOnExit()){
            float exitStartTime = (soundInstance.offtime) / 20.f;
            System.out.println("Exit: " + exitStartTime + " " + startTime + " " + timePerSample + " " + (startTime + timePerSample * numSamples));
            //System.out.println();
            for (int i = 0; i < numSamples; ++i) {
                double amp = (amplitude * (1 - (startTime + timePerSample*(i) - exitStartTime)));
                //System.out.println(" amp: " + amp);
                amp = Math.max(amp, 0);

                buffer[i] = amp * Math.sin(2 * Math.PI * frequency * (start + i) / sampleRate);
            }
        }
        else {
            for (int i = 0; i < numSamples; ++i) {
                buffer[i] = amplitude * Math.sin(2 * Math.PI * frequency * (start + i) / sampleRate);
            }
        }

        byte[] pcmData = new byte[numSamples*2]; // 16位采样大小为2字节
        int index = 0;

        for (double sample : buffer) {
            short shortSample = (short) (sample * Short.MAX_VALUE);
            pcmData[index++] = (byte) (shortSample & 0xFF);
            pcmData[index++] = (byte) ((shortSample >> 8) & 0xFF);
        }

        return pcmData;
    }


}
