package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.common.text.Presentation;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.core.MappingRenderer;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Voice;
import ch.unizh.ori.nabu.core.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;

public class SwingMappingRenderer extends MappingRenderer implements SwingRenderer, ActionListener {

	private static final long serialVersionUID = -2678795517227194057L;

	private NabuSession session;
	private JPanel theComponent = new JPanel();

	private Map<String, JComponent> comps = new HashMap<String, JComponent>();

	@SuppressWarnings("unused")
	private Map<String, String> lastAnswer;

	private DoubleSpaceListener doubleSpaceListener;
	private DirtyListener dirtyListener;

	@SuppressWarnings("serial")
	protected void initTheComponent() {
		this.theComponent.setLayout(new GridBagLayout());
		GridBagConstraints gcC = new GridBagConstraints();
		gcC.weighty = 1.0D;
		gcC.weightx = 1.0D;
		gcC.gridwidth = 0;
		gcC.fill = 1;
		GridBagConstraints gcL = new GridBagConstraints();
		gcL.weighty = 1.0D;
		gcL.gridwidth = 1;
		gcL.weightx = 0.0D;
		gcL.fill = 2;
		gcL.insets = new Insets(0, 0, 0, 5);
		for (Iterator<ModeField> iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			String label = mf.getLabel();
			JLabel labelLabel = new JLabel(label + ":");
			JComponent c = null;
			Voice v = null;
			if (mf.isAsking()) {
				JTextField tf = new JTextField() {
					public void paintComponent(Graphics g) {
						Graphics2D g2D = (Graphics2D) g;
						g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						super.paintComponent(g2D);
					}
				};
				c = tf;
				tf.addActionListener(this);
				tf.addKeyListener(this.doubleSpaceListener);
				StringColumn col = (StringColumn) mf.getMode().getVoc().getColumn(mf.getKey());
				Script s = mf.getMode().getVoc().getCentral().getScript(col.getScript());
				if (s != null && s.getLocale() != null) {
					c.addFocusListener(new LocaleSettingFocusListener(s));
					c.setLocale(s.getLocale());
				}
				if (label.length() > 0)
					labelLabel.setDisplayedMnemonic(label.charAt(0));
				Font defaultFont = c.getFont().deriveFont(48.0F);
				c.setFont(defaultFont);
			} else {
				c = new JLabel() {
					public void paintComponent(Graphics g) {
						Graphics2D g2D = (Graphics2D) g;
						g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						super.paintComponent(g2D);
					}
				};
				Font defaultFont = c.getFont().deriveFont(48.0F);
				try {
					Presentation presentation = mf.getPresentation();
					if (presentation != null && presentation instanceof StringPresentation) {
						StringPresentation sp = (StringPresentation) presentation;
						if (sp.getFont() != null)
							defaultFont = new Font(sp.getFont(), 0, 48);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				c.setFont(defaultFont);
				Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
				if (sotm != null && sotm.getVoices().size() > 0)
					v = sotm.getVoices().get(0);
			}
			this.comps.put(mf.getKey(), c);
			labelLabel.setLabelFor(c);
			this.theComponent.add(labelLabel, gcL);
			if (v == null) {
				this.theComponent.add(c, gcC);
				continue;
			}
			gcC.gridwidth = -1;
			this.theComponent.add(c, gcC);
			gcC.gridwidth = 0;
			String playName = "Play";
			char mnemonic = Character.MIN_VALUE;
			if (label.length() > 0) {
				mnemonic = label.charAt(0);
				playName = playName + " " + label;
			}
			JButton b = new JButton(playName);
			if (mnemonic != '\000')
				b.setMnemonic(mnemonic);
			b.addActionListener(new PlayListener(this, mf));
			gcL.gridwidth = 0;
			this.theComponent.add(b, gcL);
			gcL.gridwidth = 1;
		}
	}

	private static final class LocaleSettingFocusListener implements FocusListener {
		private static final Logger log = Logger.getLogger(LocaleSettingFocusListener.class);
		private Script script;

		public void focusGained(FocusEvent e) {
			Locale locale = this.script.getLocale();
			log.debug(locale + " <- " + e.getComponent().getInputContext().getLocale());
			e.getComponent().getInputContext().selectInputMethod(locale);
		}

		public void focusLost(FocusEvent e) {
		}

		public LocaleSettingFocusListener(Script script) {
			this.script = script;
		}
	}

	public static class PlayListener implements ActionListener {
		private ModeField mf;
		private SwingMappingRenderer renderer;

		public PlayListener(SwingMappingRenderer renderer, ModeField mf) {
			this.renderer = renderer;
			this.mf = mf;
		}

		public void actionPerformed(ActionEvent e) {
			this.renderer.play(this.mf);
			this.renderer.focus();
		}
	}

	public SwingMappingRenderer(Mode m) {
		super(m);

		this.lastAnswer = null;

		this.doubleSpaceListener = new DoubleSpaceListener();

		this.dirtyListener = new DirtyListener();
		initTheComponent();
	}

	@SuppressWarnings("unused")
	private void answerTextFieldActionPerformed(ActionEvent evt) {
		getSession().ok();
	}

	public void setQuestion(Object q) {
		super.setQuestion(q);
		if (this.comps == null)
			return;
		for (Iterator<ModeField> iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			if (!mf.isAsking()) {
				JLabel jLabel = (JLabel) this.comps.get(mf.getKey());
				String val = "";
				if (getPresentedQuestion() != null && getPresentedQuestion().get(mf.getKey()) != null)
					val = (String) getPresentedQuestion().get(mf.getKey());
				jLabel.setText(val);
				continue;
			}
			JTextField c = (JTextField) this.comps.get(mf.getKey());
			if (q != null) {
				c.getDocument().addDocumentListener(this.dirtyListener);
			} else {
				c.getDocument().removeDocumentListener(this.dirtyListener);
			}
			c.setText("");
			c.setEditable(true);
		}
	}

	private class DoubleSpaceListener implements KeyListener {
		private long lastHit;
		private long threshold = 500L;

		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() != ' ') {
				this.lastHit = -1L;
				return;
			}
			long time = e.getWhen();
			if (time - this.lastHit > this.threshold) {
				this.lastHit = time;
			} else {
				this.lastHit = -1L;
				JTextComponent component = (JTextComponent) e.getComponent();
				try {
					component.getDocument().remove(component.getCaretPosition() - 1, 1);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				e.consume();
				SwingMappingRenderer.this.getSession().ok();
			}
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		private DoubleSpaceListener() {
		}
	}

	private class DirtyListener implements KeyListener, CaretListener, DocumentListener {
		public void listen(Object o) {
			if (o instanceof KeyEvent) {
				KeyEvent k = (KeyEvent) o;
				if (k.getKeyChar() == '\n') {
					return;
				}
			}
			SwingMappingRenderer.this.setDirty(true);
		}

		private DirtyListener() {
		}

		public void keyPressed(KeyEvent e) {
			listen(e);
		}

		public void keyReleased(KeyEvent e) {
			listen(e);
		}

		public void keyTyped(KeyEvent e) {
			listen(e);
		}

		public void caretUpdate(CaretEvent e) {
			listen(e);
		}

		public void changedUpdate(DocumentEvent e) {
			listen(e);
		}

		public void insertUpdate(DocumentEvent e) {
			listen(e);
		}

		public void removeUpdate(DocumentEvent e) {
			listen(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void play(ModeField mf) {
		if (!getSession().isPlay())
			return;
		Sotm sotm = mf.getColumn().getVoc().getSound(mf.getKey());
		if (sotm != null && sotm.getVoices().size() > 0) {
			int i = (int) (Math.random() * sotm.getVoices().size());
			Voice v = sotm.getVoices().get(i);
			String toSay = null;
			if (mf.getColumn() instanceof StringColumn) {
				StringColumn sc = (StringColumn) mf.getColumn();
				toSay = (String) getQuestion().get(sc.getId());
			}
			String name = sotm.getUtterance(toSay, (Map) getQuestion());
			name = name.replace(".wav", ".ogg");
			String prefix = v.getPrefix();

			if (name != null) {
				String s = prefix + name;
				try {
					URL url = new URL(mf.getColumn().getVoc().getBase(), s);
					AudioClip ac = new AudioClip(url);
					(new Thread(new PlayThread(ac))).start();
				} catch (MalformedURLException e) {
				}
			}
		}
	}

	public static class PlayThread implements Runnable {
		private AudioClip ac;
		private static final Object mutex = new Object();

		public PlayThread(AudioClip ac) {
			this.ac = ac;
		}

		public void run() {
			synchronized (mutex) {
				this.ac.play();
			}
		}
	}

	public void showSolution() {
		for (Iterator<String> iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = iter.next();
			JTextField c = (JTextField) this.comps.get(key);
			c.setText((String) getPresentedQuestion().get(key));
			c.setEditable(false);
		}
	}

	public void activate() {
		focus();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SwingMappingRenderer.this.playAll();
			}
		});
	}

	private void focus() {
		if (getFocusKey() != null) {
			JTextField c = (JTextField) this.comps.get(getFocusKey());
			c.selectAll();
			c.requestFocus();
		}
	}

	public void playAll() {
		if (getQuestion() == null) {
			return;
		}
		for (Iterator<ModeField> iter = getModeFields().iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			if (!mf.isAsking()) {
				play(mf);
			}
		}
	}

	public JComponent getComponent() {
		return this.theComponent;
	}

	public void setSession(NabuSession session) {
		this.session = session;
	}

	public NabuSession getSession() {
		return this.session;
	}

	public void actionPerformed(ActionEvent e) {
		getSession().ok();
	}

	public void process(boolean showSolution) {
		for (Iterator<String> iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = iter.next();
			JTextField tf = (JTextField) this.comps.get(key);
			setUserAnswerValue(key, tf.getText());
		}
		super.process(showSolution);
	}
}