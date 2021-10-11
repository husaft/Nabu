package ch.unizh.ori.nabu.core;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

public final class AudioClip {

	private final URL url;

	public AudioClip(URL url) {
		this.url = url;
	}

	public void play() {
		try (AudioInputStream in = getAudioInputStream(url)) {
			AudioFormat outFormat = getOutFormat(in.getFormat());
			Info info = new Info(SourceDataLine.class, outFormat);
			try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
				if (line != null) {
					line.open(outFormat);
					line.start();
					stream(getAudioInputStream(outFormat, in), line);
					line.drain();
					line.stop();
				}
			}
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private AudioFormat getOutFormat(AudioFormat inFormat) {
		int ch = inFormat.getChannels();
		float rate = inFormat.getSampleRate();
		return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}

	private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
		byte[] buffer = new byte[65536];
		for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length))
			line.write(buffer, 0, n);
	}
}