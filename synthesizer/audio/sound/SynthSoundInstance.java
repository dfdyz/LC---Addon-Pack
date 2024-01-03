package com.dfdyz.lcaddon.synthesizer.audio.sound;

import com.dfdyz.lcaddon.synthesizer.SimpleSynthesizer;
import com.dfdyz.lcaddon.synthesizer.audio.buffer.RTTimbreStream;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.CompletableFuture;

public class SynthSoundInstance extends SimpleSoundInstance implements TickableSoundInstance {
    public final SimpleSynthesizer synth;
    private final Vec3 notePose;
    private final float volumeFin;
    private boolean on;
    private static final int maxLife = 600;
    private int life = maxLife;

    public long startTimeStamp = 0;
    public long offtime = 0;
    private final int note;
    private boolean isPlaying;
    private RTTimbreStream stream;

    public SynthSoundInstance(SoundEvent event, SimpleSynthesizer synth, int note, float volume, float pitch, Vec3 pos) {
        super(event, SoundSource.BLOCKS, volume, pitch, SoundInstance.createUnseededRandom(), 0, 0, 0);
        this.synth = synth;
        this.notePose = pos;
        this.volumeFin = volume;
        this.on = true;
        this.looping = false;
        this.note = note;
        this.isPlaying = true;
        stream = new RTTimbreStream(this, 44100, 44100);
        startTimeStamp = System.currentTimeMillis();
        updateRelativePos();
        life = 20;
    }

    public void noteOff() {
        if(on){
            on = false;
            offtime = System.currentTimeMillis();
        }
    }

    public boolean isOnExit(){
        return !on;
    }


    @Override
    public boolean isStopped() {
        return !isPlaying;
    }

    @Override
    public void tick() {
        updateRelativePos();
        volume = volumeFin;

        if(!on){
            if(synth.shouldNoteStop(this)) isPlaying = false;
        }

        //limit life
        if (--life <= 0) {
            isPlaying = false;
        }
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary soundBuffers, Sound sound, boolean looping) {
        return CompletableFuture.supplyAsync(() -> stream, Util.backgroundExecutor());
    }

    private void updateRelativePos() {
        Vec3 simulatedPos = calRelativePos(notePose, 0.33d);
        this.x = simulatedPos.x;
        this.y = simulatedPos.y;
        this.z = simulatedPos.z;
    }

    public static Vec3 calRelativePos(Vec3 notePose, double scale) {
        if (Minecraft.getInstance().player == null) {
            return new Vec3(0, 0, 0);
        }
        Vec3 localPos = Minecraft.getInstance().player.position();
        double dX = notePose.x - localPos.x;
        double dY = notePose.y - localPos.y;
        double dZ = notePose.z - localPos.z;
        double xC = localPos.x + dX * scale;
        double yC = localPos.y + dY * scale;
        double zC = localPos.z + dZ * scale;
        return new Vec3(xC, yC, zC);
    }

}
