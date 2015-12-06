package Base;


import javax.sound.sampled.*;

import java.net.URL;

class PlayAudio{
    static Clip line;
    PlayAudio(){}
    public void stop()
    {
        line.stop();
        line.close();
    }
    public void play(URL url1){
        Clip line = null;

        try{

          //  URL url1 = this.getClass().getResource("/sound/2.wav");
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(url1);
            AudioFormat af = aff.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, af) ;
            if (!AudioSystem.isLineSupported(info)){
                System.out.println("Line is not supported");
                System.exit(0);
            }
            line = (Clip)AudioSystem.getLine(info);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url1);
            line.open(ais);
        }catch(Exception e){
            System.err.println(e);
        }
        line.start();
    }
}

