package com.vhviet.videocallpc;

import com.vhviet.videocallpc.view.HomeUI;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class Main {
    static SourceDataLine line;
    public static void main(String[] args) throws Exception {
        HomeUI home = new HomeUI();
        home.setVisible(true);
//
//
//        AudioFormat format = new AudioFormat(8000, 8, 1, true, false);
//
//        DataLine.Info info  = new DataLine.Info(SourceDataLine.class, format);
//        try {
//            line = (SourceDataLine) AudioSystem.getLine(info);
//            line.open(format);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//        line.start();
//
//        initMic();
    }

    public static class Main2 {
        public static void main(String[] args) {
            HomeUI home = new HomeUI();
            home.setVisible(true);
        }
    }

    static void initMic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TargetDataLine line = null;
                    Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
                    for (int i = 0; i < mixerInfo.length; i++) {
                        Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
                        Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
                        if (targetLineInfo.length > 0) {
                            line = (TargetDataLine) mixer.getLine(targetLineInfo[0]);
                            break;
                        }
                    }
                    if (line == null)
                        throw new UnsupportedOperationException("No recording device found");
                    AudioFormat af = new AudioFormat(8000, 8, 1, true, false);
                    line.open(af);
                    boolean isf = true;
                    while (true) {
                        if (isf) {
                            Thread.sleep(2000);
                            isf = false;
                        }
                        line.start();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buf = new byte[(int)af.getSampleRate() * af.getFrameSize()];
                        baos.write(buf, 0, line.read(buf, 0, buf.length));
                        playAudio(baos.toByteArray());
                        line.stop();
                        baos.close();
                    }
//                    line.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static void playAudio(byte[] data) {
        line.write(data, 0, data.length);
//        line.flush();
    }
}
