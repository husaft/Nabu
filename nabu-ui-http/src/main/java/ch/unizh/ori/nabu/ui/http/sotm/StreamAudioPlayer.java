package ch.unizh.ori.nabu.ui.http.sotm;

import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.util.Utilities;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Vector;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class StreamAudioPlayer implements AudioPlayer {
	private OutputStream out;
	private boolean debug = false;
	private AudioFormat currentFormat = null;
	private byte[] outputData;
	private int curIndex = 0;
	private int totBytes = 0;

	private AudioFileFormat.Type outputType;

	@SuppressWarnings("rawtypes")
	private Vector outputList;

	@SuppressWarnings("rawtypes")
	public StreamAudioPlayer(OutputStream out, AudioFileFormat.Type type) {
		this.out = out;
		this.outputType = type;
		this.debug = Utilities.getBoolean("com.sun.speech.freetts.audio.AudioPlayer.debug");

		this.outputList = new Vector();
	}

	public StreamAudioPlayer(OutputStream out) {
		this(out, AudioFileFormat.Type.WAVE);
	}

	public synchronized void setAudioFormat(AudioFormat format) {
		this.currentFormat = format;
	}

	public AudioFormat getAudioFormat() {
		return this.currentFormat;
	}

	public void pause() {
	}

	public synchronized void resume() {
	}

	public synchronized void cancel() {
	}

	public synchronized void reset() {
	}

	public void startFirstSampleTimer() {
	}

	@SuppressWarnings("unchecked")
	public synchronized void close() {
		try {
			InputStream is = new SequenceInputStream(this.outputList.elements());
			AudioInputStream ais = new AudioInputStream(is, this.currentFormat,
					(this.totBytes / this.currentFormat.getFrameSize()));

			AudioSystem.write(ais, this.outputType, this.out);
		} catch (IOException ioe) {
			System.err.println("Can't write audio to out");
		} catch (IllegalArgumentException iae) {
			System.err.println("Can't write audio type " + this.outputType);
		}
	}

	public float getVolume() {
		return 1.0F;
	}

	public void setVolume(float volume) {
	}

	public void begin(int size) {
		this.outputData = new byte[size];
		this.curIndex = 0;
	}

	@SuppressWarnings("unchecked")
	public boolean end() {
		this.outputList.add(new ByteArrayInputStream(this.outputData));
		this.totBytes += this.outputData.length;
		return true;
	}

	public boolean drain() {
		return true;
	}

	public synchronized long getTime() {
		return -1L;
	}

	public synchronized void resetTime() {
	}

	public boolean write(byte[] audioData) {
		return write(audioData, 0, audioData.length);
	}

	public boolean write(byte[] bytes, int offset, int size) {
		System.arraycopy(bytes, offset, this.outputData, this.curIndex, size);
		this.curIndex += size;
		return true;
	}

	@SuppressWarnings("unused")
	private synchronized boolean waitResume() {
		return true;
	}

	public String toString() {
		return "StreamAudioPlayer";
	}

	@SuppressWarnings("unused")
	private void debugPrint(String msg) {
		if (this.debug)
			System.out.println(toString() + ": " + msg);
	}

	public void showMetrics() {
	}
}