package ch.unizh.ori.judit;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Enumeration;
import java.util.Vector;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileFilter;

public class Sound extends JPanel implements ActionListener {

	private static final long serialVersionUID = -2102495769804558958L;

	private FormatControls formatControls = new FormatControls();
	private Capture capture = new Capture();
	private Playback playback = new Playback();
	private AudioInputStream audioInputStream;
	private SamplingGraph samplingGraph;
	private JButton playB;
	private JButton captB;
	private JButton pausB;
	private JButton loadB;
	private JButton auB;
	private JButton aiffB;
	private JButton waveB;
	private JTextField textField;
	private String fileName = "untitled";
	private String errStr;
	private double duration;
	private double seconds;
	private File file;
	private Vector<Shape> lines = new Vector<Shape>();

	public Sound() {
		setLayout(new BorderLayout());
		EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
		SoftBevelBorder sbb = new SoftBevelBorder(1);
		setBorder(eb);

		JPanel p2 = new JPanel();
		p2.setBorder(sbb);
		p2.setLayout(new BoxLayout(p2, 1));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new EmptyBorder(10, 0, 5, 0));
		this.playB = addButton("Play", buttonsPanel, false);
		this.captB = addButton("Record", buttonsPanel, true);
		this.pausB = addButton("Pause", buttonsPanel, false);
		this.loadB = addButton("Load...", buttonsPanel, true);
		p2.add(buttonsPanel);

		JPanel samplingPanel = new JPanel(new BorderLayout());
		EmptyBorder esb = new EmptyBorder(10, 20, 20, 20);
		samplingPanel.setBorder(new CompoundBorder(esb, sbb));
		samplingPanel.add(this.samplingGraph = new SamplingGraph());
		p2.add(samplingPanel);

		JPanel savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, 1));

		JPanel saveTFpanel = new JPanel();
		saveTFpanel.add(new JLabel("File to save:  "));
		saveTFpanel.add(this.textField = new JTextField(this.fileName));
		this.textField.setPreferredSize(new Dimension(140, 25));
		savePanel.add(saveTFpanel);

		JPanel saveBpanel = new JPanel();
		this.auB = addButton("Save AU", saveBpanel, false);
		this.aiffB = addButton("Save AIFF", saveBpanel, false);
		this.waveB = addButton("Save WAVE", saveBpanel, false);
		savePanel.add(saveBpanel);

		p2.add(savePanel);

		add(p2);
	}

	public void open() {
	}

	public void close() {
		if (this.playback.thread != null) {
			this.playB.doClick(0);
		}
		if (this.capture.thread != null) {
			this.captB.doClick(0);
		}
	}

	private JButton addButton(String name, JPanel p, boolean state) {
		JButton b = new JButton(name);
		b.addActionListener(this);
		b.setEnabled(state);
		p.add(b);
		return b;
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(this.auB)) {
			saveToFile(this.textField.getText().trim(), AudioFileFormat.Type.AU);
		} else if (obj.equals(this.aiffB)) {
			saveToFile(this.textField.getText().trim(), AudioFileFormat.Type.AIFF);
		} else if (obj.equals(this.waveB)) {
			saveToFile(this.textField.getText().trim(), AudioFileFormat.Type.WAVE);
		} else if (obj.equals(this.playB)) {
			if (this.playB.getText().startsWith("Play")) {
				this.playback.start();
				this.samplingGraph.start();
				this.captB.setEnabled(false);
				this.pausB.setEnabled(true);
				this.playB.setText("Stop");
			} else {
				this.playback.stop();
				this.samplingGraph.stop();
				this.captB.setEnabled(true);
				this.pausB.setEnabled(false);
				this.playB.setText("Play");
			}
		} else if (obj.equals(this.captB)) {
			if (this.captB.getText().startsWith("Record")) {
				this.file = null;
				this.capture.start();
				this.fileName = "untitled";
				this.samplingGraph.start();
				this.loadB.setEnabled(false);
				this.playB.setEnabled(false);
				this.pausB.setEnabled(true);
				this.auB.setEnabled(false);
				this.aiffB.setEnabled(false);
				this.waveB.setEnabled(false);
				this.captB.setText("Stop");
			} else {
				this.lines.removeAllElements();
				this.capture.stop();
				this.samplingGraph.stop();
				this.loadB.setEnabled(true);
				this.playB.setEnabled(true);
				this.pausB.setEnabled(false);
				this.auB.setEnabled(true);
				this.aiffB.setEnabled(true);
				this.waveB.setEnabled(true);
				this.captB.setText("Record");
			}
		} else if (obj.equals(this.pausB)) {
			if (this.pausB.getText().startsWith("Pause")) {
				if (this.capture.thread != null) {
					this.capture.line.stop();
				} else if (this.playback.thread != null) {
					this.playback.line.stop();
				}
				this.pausB.setText("Resume");
			} else {
				if (this.capture.thread != null) {
					this.capture.line.start();
				} else if (this.playback.thread != null) {
					this.playback.line.start();
				}
				this.pausB.setText("Pause");
			}
		} else if (obj.equals(this.loadB)) {
			try {
				File file = new File(System.getProperty("user.dir"));
				JFileChooser fc = new JFileChooser(file);
				fc.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						String name = f.getName();
						if (name.endsWith(".au") || name.endsWith(".wav") || name.endsWith(".aiff")
								|| name.endsWith(".aif")) {
							return true;
						}
						return false;
					}

					public String getDescription() {
						return ".au, .wav, .aif";
					}
				});

				if (fc.showOpenDialog(null) == 0) {
					createAudioInputStream(fc.getSelectedFile(), true);
				}
			} catch (SecurityException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void createAudioInputStream(File file, boolean updateComponents) {
		if (file != null && file.isFile()) {
			try {
				this.file = file;
				this.errStr = null;
				this.audioInputStream = AudioSystem.getAudioInputStream(file);
				this.playB.setEnabled(true);
				this.fileName = file.getName();
				long milliseconds = (long) ((float) (this.audioInputStream.getFrameLength() * 1000L)
						/ this.audioInputStream.getFormat().getFrameRate());
				this.duration = milliseconds / 1000.0D;
				this.auB.setEnabled(true);
				this.aiffB.setEnabled(true);
				this.waveB.setEnabled(true);
				if (updateComponents) {
					this.formatControls.setFormat(this.audioInputStream.getFormat());
					this.samplingGraph.createWaveForm((byte[]) null);
				}
			} catch (Exception ex) {
				reportStatus(ex.toString());
			}
		} else {
			reportStatus("Audio file required.");
		}
	}

	public void saveToFile(String name, AudioFileFormat.Type fileType) {
		if (this.audioInputStream == null) {
			reportStatus("No loaded audio to save");
			return;
		}
		if (this.file != null) {
			createAudioInputStream(this.file, false);
		}

		try {
			this.audioInputStream.reset();
		} catch (Exception e) {
			reportStatus("Unable to reset stream " + e);

			return;
		}
		File file = new File(this.fileName = name);
		try {
			if (AudioSystem.write(this.audioInputStream, fileType, file) == -1)
				throw new IOException("Problems writing to file");
		} catch (Exception ex) {
			reportStatus(ex.toString());
		}
		this.samplingGraph.repaint();
	}

	private void reportStatus(String msg) {
		if ((this.errStr = msg) != null) {
			System.out.println(this.errStr);
			this.samplingGraph.repaint();
		}
	}

	public class Playback implements Runnable {
		SourceDataLine line;

		Thread thread;

		public void start() {
			Sound.this.errStr = null;
			this.thread = new Thread(this);
			this.thread.setName("Playback");
			this.thread.start();
		}

		public void stop() {
			this.thread = null;
		}

		private void shutDown(String message) {
			if ((Sound.this.errStr = message) != null) {
				System.err.println(Sound.this.errStr);
				Sound.this.samplingGraph.repaint();
			}
			if (this.thread != null) {
				this.thread = null;
				Sound.this.samplingGraph.stop();
				Sound.this.captB.setEnabled(true);
				Sound.this.pausB.setEnabled(false);
				Sound.this.playB.setText("Play");
			}
		}

		public void run() {
			if (Sound.this.file != null) {
				Sound.this.createAudioInputStream(Sound.this.file, false);
			}

			if (Sound.this.audioInputStream == null) {
				shutDown("No loaded audio to play back");

				return;
			}
			try {
				Sound.this.audioInputStream.reset();
			} catch (Exception e) {
				shutDown("Unable to reset the stream\n" + e);

				return;
			}

			AudioFormat format = Sound.this.formatControls.getFormat();
			AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, Sound.this.audioInputStream);

			if (playbackInputStream == null) {
				shutDown("Unable to convert stream of format " + Sound.this.audioInputStream + " to format " + format);

				return;
			}

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				shutDown("Line matching " + info + " not supported.");

				return;
			}

			try {
				this.line = (SourceDataLine) AudioSystem.getLine(info);
				this.line.open(format, 16384);
			} catch (LineUnavailableException ex) {
				shutDown("Unable to open the line: " + ex);

				return;
			}

			int frameSizeInBytes = format.getFrameSize();
			int bufferLengthInFrames = this.line.getBufferSize() / 8;
			int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
			byte[] data = new byte[bufferLengthInBytes];
			int numBytesRead = 0;

			this.line.start();

			while (this.thread != null) {
				try {
					if ((numBytesRead = playbackInputStream.read(data)) == -1) {
						break;
					}
					int numBytesRemaining = numBytesRead;
					while (numBytesRemaining > 0) {
						numBytesRemaining -= this.line.write(data, 0, numBytesRemaining);
					}
				} catch (Exception e) {
					shutDown("Error during playback: " + e);

					break;
				}
			}

			if (this.thread != null) {
				this.line.drain();
			}
			this.line.stop();
			this.line.close();
			this.line = null;
			shutDown(null);
		}
	}

	class Capture implements Runnable {
		TargetDataLine line;

		Thread thread;

		public void start() {
			Sound.this.errStr = null;
			this.thread = new Thread(this);
			this.thread.setName("Capture");
			this.thread.start();
		}

		public void stop() {
			this.thread = null;
		}

		private void shutDown(String message) {
			if ((Sound.this.errStr = message) != null && this.thread != null) {
				this.thread = null;
				Sound.this.samplingGraph.stop();
				Sound.this.loadB.setEnabled(true);
				Sound.this.playB.setEnabled(true);
				Sound.this.pausB.setEnabled(false);
				Sound.this.auB.setEnabled(true);
				Sound.this.aiffB.setEnabled(true);
				Sound.this.waveB.setEnabled(true);
				Sound.this.captB.setText("Record");
				System.err.println(Sound.this.errStr);
				Sound.this.samplingGraph.repaint();
			}
		}

		public void run() {
			Sound.this.duration = 0.0D;
			Sound.this.audioInputStream = null;

			AudioFormat format = Sound.this.formatControls.getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				shutDown("Line matching " + info + " not supported.");

				return;
			}

			try {
				this.line = (TargetDataLine) AudioSystem.getLine(info);
				this.line.open(format, this.line.getBufferSize());
			} catch (LineUnavailableException ex) {
				shutDown("Unable to open the line: " + ex);
				return;
			} catch (SecurityException ex) {
				shutDown(ex.toString());
				return;
			} catch (Exception ex) {
				shutDown(ex.toString());

				return;
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int frameSizeInBytes = format.getFrameSize();
			int bufferLengthInFrames = this.line.getBufferSize() / 8;
			int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
			byte[] data = new byte[bufferLengthInBytes];

			this.line.start();
			int numBytesRead;
			while (this.thread != null && (numBytesRead = this.line.read(data, 0, bufferLengthInBytes)) != -1) {

				out.write(data, 0, numBytesRead);
			}

			this.line.stop();
			this.line.close();
			this.line = null;

			try {
				out.flush();
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			byte[] audioBytes = out.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
			Sound.this.audioInputStream = new AudioInputStream(bais, format, (audioBytes.length / frameSizeInBytes));

			long milliseconds = (long) ((float) (Sound.this.audioInputStream.getFrameLength() * 1000L)
					/ format.getFrameRate());
			Sound.this.duration = milliseconds / 1000.0D;

			try {
				Sound.this.audioInputStream.reset();
			} catch (Exception ex) {
				ex.printStackTrace();

				return;
			}
			Sound.this.samplingGraph.createWaveForm(audioBytes);
		}
	}

	class FormatControls extends JPanel {

		private static final long serialVersionUID = 2334048277999498231L;

		Vector<ButtonGroup> groups = new Vector<ButtonGroup>();
		JToggleButton linrB;
		JToggleButton ulawB;
		JToggleButton alawB;
		JToggleButton rate8B;
		JToggleButton rate11B;
		JToggleButton rate16B;
		JToggleButton rate22B;
		JToggleButton rate44B;
		JToggleButton size8B;
		JToggleButton size16B;
		JToggleButton signB;
		JToggleButton unsignB;
		JToggleButton litB;
		JToggleButton bigB;
		JToggleButton monoB;
		JToggleButton sterB;

		public FormatControls() {
			setLayout(new GridLayout(0, 1));
			EmptyBorder eb = new EmptyBorder(0, 0, 0, 5);
			BevelBorder bb = new BevelBorder(1);
			CompoundBorder cb = new CompoundBorder(eb, bb);
			setBorder(new CompoundBorder(cb, new EmptyBorder(8, 5, 5, 5)));
			JPanel p1 = new JPanel();
			ButtonGroup encodingGroup = new ButtonGroup();
			this.linrB = addToggleButton(p1, encodingGroup, "linear", true);
			this.ulawB = addToggleButton(p1, encodingGroup, "ulaw", false);
			this.alawB = addToggleButton(p1, encodingGroup, "alaw", false);
			add(p1);
			this.groups.addElement(encodingGroup);

			JPanel p2 = new JPanel();
			JPanel p2b = new JPanel();
			ButtonGroup sampleRateGroup = new ButtonGroup();
			this.rate8B = addToggleButton(p2, sampleRateGroup, "8000", false);
			this.rate11B = addToggleButton(p2, sampleRateGroup, "11025", false);
			this.rate16B = addToggleButton(p2b, sampleRateGroup, "16000", false);
			this.rate22B = addToggleButton(p2b, sampleRateGroup, "22050", false);
			this.rate44B = addToggleButton(p2b, sampleRateGroup, "44100", true);
			add(p2);
			add(p2b);
			this.groups.addElement(sampleRateGroup);

			JPanel p3 = new JPanel();
			ButtonGroup sampleSizeInBitsGroup = new ButtonGroup();
			this.size8B = addToggleButton(p3, sampleSizeInBitsGroup, "8", false);
			this.size16B = addToggleButton(p3, sampleSizeInBitsGroup, "16", true);
			add(p3);
			this.groups.addElement(sampleSizeInBitsGroup);

			JPanel p4 = new JPanel();
			ButtonGroup signGroup = new ButtonGroup();
			this.signB = addToggleButton(p4, signGroup, "signed", true);
			this.unsignB = addToggleButton(p4, signGroup, "unsigned", false);
			add(p4);
			this.groups.addElement(signGroup);

			JPanel p5 = new JPanel();
			ButtonGroup endianGroup = new ButtonGroup();
			this.litB = addToggleButton(p5, endianGroup, "little endian", false);
			this.bigB = addToggleButton(p5, endianGroup, "big endian", true);
			add(p5);
			this.groups.addElement(endianGroup);

			JPanel p6 = new JPanel();
			ButtonGroup channelsGroup = new ButtonGroup();
			this.monoB = addToggleButton(p6, channelsGroup, "mono", false);
			this.sterB = addToggleButton(p6, channelsGroup, "stereo", true);
			add(p6);
			this.groups.addElement(channelsGroup);
		}

		private JToggleButton addToggleButton(JPanel p, ButtonGroup g, String name, boolean state) {
			JToggleButton b = new JToggleButton(name, state);
			p.add(b);
			g.add(b);
			return b;
		}

		public AudioFormat getFormat() {
			Vector<String> v = new Vector<String>(this.groups.size());
			for (int i = 0; i < this.groups.size(); i++) {
				ButtonGroup g = this.groups.get(i);
				for (Enumeration<AbstractButton> e = g.getElements(); e.hasMoreElements();) {
					AbstractButton b = e.nextElement();
					if (b.isSelected()) {
						v.add(b.getText());

						break;
					}
				}
			}
			AudioFormat.Encoding encoding = AudioFormat.Encoding.ULAW;
			String encString = v.get(0);
			float rate = Float.valueOf(v.get(1)).floatValue();
			int sampleSize = Integer.valueOf(v.get(2)).intValue();
			String signedString = v.get(3);
			boolean bigEndian = ((String) v.get(4)).startsWith("big");
			int channels = ((String) v.get(5)).equals("mono") ? 1 : 2;

			if (encString.equals("linear")) {
				if (signedString.equals("signed")) {
					encoding = AudioFormat.Encoding.PCM_SIGNED;
				} else {
					encoding = AudioFormat.Encoding.PCM_UNSIGNED;
				}
			} else if (encString.equals("alaw")) {
				encoding = AudioFormat.Encoding.ALAW;
			}
			return new AudioFormat(encoding, rate, sampleSize, channels, sampleSize / 8 * channels, rate, bigEndian);
		}

		public void setFormat(AudioFormat format) {
			AudioFormat.Encoding type = format.getEncoding();
			if (type == AudioFormat.Encoding.ULAW) {
				this.ulawB.doClick();
			} else if (type == AudioFormat.Encoding.ALAW) {
				this.alawB.doClick();
			} else if (type == AudioFormat.Encoding.PCM_SIGNED) {
				this.linrB.doClick();
				this.signB.doClick();
			} else if (type == AudioFormat.Encoding.PCM_UNSIGNED) {
				this.linrB.doClick();
				this.unsignB.doClick();
			}
			float rate = format.getFrameRate();
			if (rate == 8000.0F) {
				this.rate8B.doClick();
			} else if (rate == 11025.0F) {
				this.rate11B.doClick();
			} else if (rate == 16000.0F) {
				this.rate16B.doClick();
			} else if (rate == 22050.0F) {
				this.rate22B.doClick();
			} else if (rate == 44100.0F) {
				this.rate44B.doClick();
			}
			switch (format.getSampleSizeInBits()) {
			case 8:
				this.size8B.doClick();
				break;
			case 16:
				this.size16B.doClick();
				break;
			}

			if (format.isBigEndian()) {
				this.bigB.doClick();
			} else {
				this.litB.doClick();
			}
			if (format.getChannels() == 1) {
				this.monoB.doClick();
			} else {
				this.sterB.doClick();
			}
		}
	}

	class SamplingGraph extends JPanel implements Runnable {

		private static final long serialVersionUID = -1925936970453248618L;

		private Thread thread;

		private Font font12 = new Font("serif", 0, 12);
		Color jfcBlue = new Color(204, 204, 255);
		Color pink = new Color(255, 175, 175);

		public SamplingGraph() {
			setBackground(new Color(20, 20, 20));
		}

		public void createWaveForm(byte[] audioBytes) {
			Sound.this.lines.removeAllElements();

			AudioFormat format = Sound.this.audioInputStream.getFormat();
			if (audioBytes == null) {
				try {
					audioBytes = new byte[(int) (Sound.this.audioInputStream.getFrameLength() * format.getFrameSize())];

					Sound.this.audioInputStream.read(audioBytes);
				} catch (Exception ex) {
					Sound.this.reportStatus(ex.toString());

					return;
				}
			}
			Dimension d = getSize();
			int w = d.width;
			int h = d.height - 15;
			int[] audioData = null;
			if (format.getSampleSizeInBits() == 16) {
				int nlengthInSamples = audioBytes.length / 2;
				audioData = new int[nlengthInSamples];
				if (format.isBigEndian()) {
					for (int i = 0; i < nlengthInSamples; i++) {
						int MSB = audioBytes[2 * i];
						int LSB = audioBytes[2 * i + 1];
						audioData[i] = MSB << 8 | 0xFF & LSB;
					}
				} else {
					for (int i = 0; i < nlengthInSamples; i++) {
						int LSB = audioBytes[2 * i];
						int MSB = audioBytes[2 * i + 1];
						audioData[i] = MSB << 8 | 0xFF & LSB;
					}
				}
			} else if (format.getSampleSizeInBits() == 8) {
				int nlengthInSamples = audioBytes.length;
				audioData = new int[nlengthInSamples];
				if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
					for (int i = 0; i < audioBytes.length; i++) {
						audioData[i] = audioBytes[i];
					}
				} else {
					for (int i = 0; i < audioBytes.length; i++) {
						audioData[i] = audioBytes[i] - 128;
					}
				}
			}

			int frames_per_pixel = audioBytes.length / format.getFrameSize() / w;
			byte my_byte = 0;
			double y_last = 0.0D;
			int numChannels = format.getChannels();
			double x;
			for (x = 0.0D; x < w && audioData != null; x++) {
				int idx = (int) ((frames_per_pixel * numChannels) * x);
				if (format.getSampleSizeInBits() == 8) {
					my_byte = (byte) audioData[idx];
				} else {
					my_byte = (byte) (128 * audioData[idx] / 32768);
				}
				double y_new = (h * (128 - my_byte) / 256);
				Sound.this.lines.add(new Line2D.Double(x, y_last, x, y_new));
				y_last = y_new;
			}

			repaint();
		}

		public void paint(Graphics g) {
			Dimension d = getSize();
			int w = d.width;
			int h = d.height;
			int INFOPAD = 15;

			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(getBackground());
			g2.clearRect(0, 0, w, h);
			g2.setColor(Color.white);
			g2.fillRect(0, h - INFOPAD, w, INFOPAD);

			if (Sound.this.errStr != null) {
				g2.setColor(this.jfcBlue);
				g2.setFont(new Font("serif", 1, 18));
				g2.drawString("ERROR", 5, 20);
				AttributedString as = new AttributedString(Sound.this.errStr);
				as.addAttribute(TextAttribute.FONT, this.font12, 0, Sound.this.errStr.length());
				AttributedCharacterIterator aci = as.getIterator();
				FontRenderContext frc = g2.getFontRenderContext();
				LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
				float x = 5.0F, y = 25.0F;
				lbm.setPosition(0);
				while (lbm.getPosition() < Sound.this.errStr.length()) {
					TextLayout tl = lbm.nextLayout(w - x - 5.0F);
					if (!tl.isLeftToRight()) {
						x = w - tl.getAdvance();
					}
					tl.draw(g2, x, y += tl.getAscent());
					y += tl.getDescent() + tl.getLeading();
				}
			} else if (Sound.this.capture.thread != null) {
				g2.setColor(Color.black);
				g2.setFont(this.font12);
				g2.drawString("Length: " + String.valueOf(Sound.this.seconds), 3, h - 4);
			} else {
				g2.setColor(Color.black);
				g2.setFont(this.font12);
				g2.drawString("File: " + Sound.this.fileName + "  Length: " + String.valueOf(Sound.this.duration)
						+ "  Position: " + String.valueOf(Sound.this.seconds), 3, h - 4);

				if (Sound.this.audioInputStream != null) {

					g2.setColor(this.jfcBlue);
					for (int i = 1; i < Sound.this.lines.size(); i++) {
						g2.draw(Sound.this.lines.get(i));
					}

					if (Sound.this.seconds != 0.0D) {
						double loc = Sound.this.seconds / Sound.this.duration * w;
						g2.setColor(this.pink);
						g2.setStroke(new BasicStroke(3.0F));
						g2.draw(new Line2D.Double(loc, 0.0D, loc, (h - INFOPAD - 2)));
					}
				}
			}
		}

		public void start() {
			this.thread = new Thread(this);
			this.thread.setName("SamplingGraph");
			this.thread.start();
			Sound.this.seconds = 0.0D;
		}

		public void stop() {
			if (this.thread != null) {
				this.thread.interrupt();
			}
			this.thread = null;
		}

		public void run() {
			Sound.this.seconds = 0.0D;

			while (this.thread != null) {
				long milliseconds;
				if (Sound.this.playback.line != null && Sound.this.playback.line.isOpen()) {
					milliseconds = Sound.this.playback.line.getMicrosecondPosition() / 1000L;
					Sound.this.seconds = (double) milliseconds / 1000.0D;
				} else if (Sound.this.capture.line != null && Sound.this.capture.line.isActive()) {
					milliseconds = Sound.this.capture.line.getMicrosecondPosition() / 1000L;
					Sound.this.seconds = (double) milliseconds / 1000.0D;
				}

				try {
					Thread.sleep(100L);
				} catch (Exception var4) {
					break;
				}

				this.repaint();

				while (Sound.this.capture.line != null && !Sound.this.capture.line.isActive()
						|| Sound.this.playback.line != null && !Sound.this.playback.line.isOpen()) {
					try {
						Thread.sleep(10L);
					} catch (Exception var3) {
						break;
					}
				}
			}

			Sound.this.seconds = 0.0D;
			this.repaint();
		}
	}

	public static void main(String[] s) {
		Sound capturePlayback = new Sound();
		capturePlayback.open();
		JFrame f = new JFrame("Capture/Playback");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.getContentPane().add("Center", capturePlayback);
		f.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = 720;
		int h = 340;
		f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
		f.setSize(w, h);
		f.setVisible(true);
	}
}