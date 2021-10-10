package ch.unizh.ori.common.sotm.core;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.EngineModeDesc;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public abstract class Speak {
	private static Speak _default;
	private static boolean doSpeak = false;

	public static Speak getDefault() {
		if (_default == null) {
			if (doSpeak()) {
				_default = new SpeakImpl();
			} else {
				_default = new NullSpeak();
			}
		}
		return _default;
	}

	static {
		doSpeak = Boolean.getBoolean("nabu.sotm.doSpeak");
		System.err.println("doSpeak=" + doSpeak);
		try {
			Class.forName("javax.speech.Engine");
		} catch (Throwable t) {
			System.err.println("No Speech classes!");
			doSpeak = false;
		}
		System.err.println("doSpeak=" + doSpeak);
	}

	public static boolean doSpeak() {
		return doSpeak;
	}

	public abstract void speak(String paramString);

	private static class NullSpeak extends Speak {
		private NullSpeak() {
		}

		public void speak(String str) {
		}
	}

	private static class SpeakImpl extends Speak {
		Synthesizer synth;

		private SpeakImpl() {
			try {
				String synthesizerName = System.getProperty("nabu.sotm.synthesizerName",
						"Unlimited domain FreeTTS Speech Synthesizer from Sun Labs");

				SynthesizerModeDesc desc = new SynthesizerModeDesc(synthesizerName, null, Locale.US, Boolean.FALSE,
						null);

				this.synth = Central.createSynthesizer((EngineModeDesc) desc);

				String voiceName = System.getProperty("voiceName", "kevin16");
				Voice voice = new Voice(voiceName, 65535, 65535, null);

				this.synth.allocate();
				this.synth.resume();
				this.synth.getSynthesizerProperties().setVoice(voice);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public void speak(String str) {
			System.err.println("speaking: " + str);

			try {
				this.synth.speakPlainText(str, null);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@SuppressWarnings("unused")
		private void deallocat() {
			try {
				this.synth.deallocate();
				this.synth = null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}