package ch.unizh.ori.nabu.ui.http.sotm;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.util.Utilities;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SotmServlet extends HttpServlet {

	private static final long serialVersionUID = 7259806759255777507L;

	private Voice voice8k;
	private String voice8kName = Utilities.getProperty("voice8kName", "kevin");

	private Voice voice16k;

	private String voice16kName = Utilities.getProperty("voice16kName", "kevin16");

	public void init() throws ServletException {
		try {
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
			VoiceManager voiceManager = VoiceManager.getInstance();
			this.voice8k = voiceManager.getVoice(this.voice8kName);
			this.voice16k = voiceManager.getVoice(this.voice16kName);
			this.voice8k.allocate();
			this.voice16k.allocate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Voice voice;
		String text = request.getParameter("text");
		response.setContentType("audio/wav");

		int sampleRate = 16000;

		if (sampleRate == 8000) {
			voice = this.voice8k;
		} else if (sampleRate == 16000) {
			voice = this.voice16k;
		} else {
			return;
		}

		StreamAudioPlayer audioPlayer = new StreamAudioPlayer((OutputStream) response.getOutputStream());
		voice.setAudioPlayer(audioPlayer);
		voice.speak(text);
		audioPlayer.close();
	}

	public void destroy() {
		super.destroy();
		this.voice8k.deallocate();
		this.voice16k.deallocate();
	}
}