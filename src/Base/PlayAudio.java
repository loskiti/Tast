package Base;
/**
 * Воспроизведение музыки
 */

import javax.sound.sampled.*;
import java.net.URL;

class PlayAudio {
	static private Clip line;

	PlayAudio() {
	}

	public void stop() {
		line.stop();
		line.close();
	}

	public void play(URL url1) {
		Clip line = null;
		try {
			AudioFileFormat aff = AudioSystem.getAudioFileFormat(url1);
			AudioFormat af = aff.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, af);
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line is not supported");
				System.exit(0);
			}
			line = (Clip) AudioSystem.getLine(info);
			AudioInputStream ais = AudioSystem.getAudioInputStream(url1);
			line.open(ais);
		} catch (Exception e) {
			System.err.println(e);
		}
		line.start();
	}
}
