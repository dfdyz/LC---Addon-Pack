package com.dfdyz.lcaddon.utils;

import org.joml.Vector4f;
import org.ywzj.midi.util.MidiUtils;

import javax.sound.midi.*;
import java.nio.charset.StandardCharsets;

public class MidiTrackUtils {
    public static String getTrackName(Track track){
        String instrumentName = null;
        String soundbankName = null;
        for (int index = 0; index < track.size(); index++) {
            MidiMessage message = track.get(index).getMessage();
            if (message instanceof MetaMessage metaMessage) {
                int type = metaMessage.getType();
                if (type == 0x03) {
                    try {
                        instrumentName = new String(metaMessage.getData(), StandardCharsets.UTF_8);
                    } catch (Exception ignore) {}
                }
            } else if (message instanceof ShortMessage shortMessage) {
                if (shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                    int program = shortMessage.getData1();
                    try {
                        Soundbank soundbank = MidiSystem.getSynthesizer().getDefaultSoundbank();
                        if (soundbank != null) {
                            javax.sound.midi.Instrument[] instruments = soundbank.getInstruments();
                            if (program >= 0 && program < instruments.length) {
                                javax.sound.midi.Instrument instrument = instruments[program];
                                soundbankName = instrument.getName();
                            }
                        }
                    } catch (Exception ignore) {}
                }
            }
        }
        if (instrumentName != null || soundbankName != null) {
                return instrumentName == null ? soundbankName : instrumentName;
        }
        return "";
    }

    public static class LampColMsg extends MidiMessage{
        public final float partVal;
        public final ColorPart part;

        private static final int R_n = MidiUtils.notationToNote("a1");
        private static final int G_n = MidiUtils.notationToNote("as1");
        private static final int B_n = MidiUtils.notationToNote("b");
        private static final int A_n = MidiUtils.notationToNote("c1");

        public enum ColorPart{
            R, G, B, A, UNKNOWN
        }

        public LampColMsg(ColorPart p, float val) {
            super(new byte[0]);
            this.part = p;
            this.partVal = val;
        }

        public static LampColMsg fromShortMsg(ShortMessage shortMessage){
            LampColMsg lmsg = null;
            int command = shortMessage.getCommand();
            if (command == ShortMessage.NOTE_ON) {
                int note = shortMessage.getData1();
                int v = shortMessage.getData2();

                if (v > 0){
                    float val = v / 127.f;
                    switch (note){
                        case 21:
                            lmsg = new LampColMsg(ColorPart.R, val);
                            break;
                        case 22:
                            lmsg = new LampColMsg(ColorPart.G, val);
                            break;
                        case 23:
                            lmsg = new LampColMsg(ColorPart.B, val);
                            break;
                        case 24:
                            lmsg = new LampColMsg(ColorPart.A, val);
                            break;
                        default:
                            lmsg = new LampColMsg(ColorPart.UNKNOWN, 1);
                            break;
                    }
                }
                else
                    lmsg = new LampColMsg(ColorPart.UNKNOWN, 1);
            }
            return lmsg;
        }

        @Override
        public Object clone() {
            return new LampColMsg(this.part, this.partVal);
        }
    }


}

