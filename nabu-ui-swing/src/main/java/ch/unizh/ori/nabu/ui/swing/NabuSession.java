package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.stat.Statistics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Logger;

public class NabuSession extends JPanel {

	private static final long serialVersionUID = -1939834530974109019L;

	private static final Logger log = Logger.getLogger(NabuSession.class);

	@SuppressWarnings("unused")
	private int total;

	@SuppressWarnings("unused")
	private int asked;

	private JPanel statusPanel;

	private JPanel buttPanel;

	private JButton okButton;

	private JPanel southPanel;

	private JPanel rendererPanel;

	private JCheckBox problemsOnlyCB;

	private JCheckBox playCB;

	private JButton showSolutionButton;

	private JSpinner maxProblems;

	@SuppressWarnings("unused")
	private JButton speakButton;

	private QuestionIterator iter;

	private JPanel countPanel;

	private JPanel leftStatusPanel;

	private JLabel jLabel;

	private JLabel statLabel;

	private JLabel jLabel2;

	private JLabel problemLabel;

	private JPanel problemColorPanel;

	private JPanel restColorPanel;

	private JPanel finishedColorPanel;

	private JLabel finishedLabel;

	private JLabel problemsLabel;

	private JLabel restLabel;

	private GridBagLayout countPanelLayout;

	public NabuSession() {
		this.countPanel = null;

		this.leftStatusPanel = null;

		this.jLabel = null;

		this.statLabel = null;

		this.jLabel2 = null;

		this.problemLabel = null;

		this.problemColorPanel = null;

		this.restColorPanel = null;

		this.finishedColorPanel = null;

		this.finishedLabel = null;

		this.problemsLabel = null;

		this.restLabel = null;
		initComponents();
	}

	public void postAdd() {
		this.okButton.setDefaultCapable(true);
		SwingUtilities.getRootPane(this).setDefaultButton(this.okButton);
	}

	private void initComponents() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		gridLayout.setHgap(0);
		gridLayout.setVgap(0);
		this.rendererPanel = new JPanel();
		this.southPanel = new JPanel();
		this.buttPanel = new JPanel();
		this.okButton = new JButton();
		this.showSolutionButton = new JButton();
		this.problemsOnlyCB = new JCheckBox();
		this.playCB = new JCheckBox();
		this.statusPanel = new JPanel();
		this.maxProblems = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
		setLayout(new BorderLayout());
		this.rendererPanel.setLayout(new BorderLayout());
		add(this.rendererPanel, "Center");
		this.southPanel.setLayout(new BorderLayout());
		this.okButton.setText("OK");
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				NabuSession.this.ok();
			}
		});
		this.buttPanel.add(this.okButton);
		this.showSolutionButton.setMnemonic('s');
		this.showSolutionButton.setText("Show Solution");
		this.showSolutionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				NabuSession.this.process(true);
			}
		});
		this.buttPanel.add(this.showSolutionButton);
		this.problemsOnlyCB.setMnemonic('p');
		this.problemsOnlyCB.setText("Problems Only");
		this.problemsOnlyCB.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				if (NabuSession.this.getIter() != null)
					NabuSession.this.getIter().setProblemsOnly(NabuSession.this.problemsOnlyCB.isSelected());
			}
		});
		this.buttPanel.add(this.problemsOnlyCB);
		this.playCB.setMnemonic('p');
		this.playCB.setText("Play Sound");
		this.playCB.setSelected(true);
		this.buttPanel.add(this.playCB);
		this.buttPanel.add(this.maxProblems);
		this.southPanel.add(this.buttPanel, "Center");
		this.statusPanel.setLayout(gridLayout);
		this.statusPanel.add(getCountPanel(), (Object) null);
		this.statusPanel.add(getLeftStatusPanel(), (Object) null);
		this.southPanel.add(this.statusPanel, "South");
		add(this.southPanel, "South");
	}

	public void updateRenderer() {
		SwingRenderer r = (SwingRenderer) getIter().getRenderer();
		if (r == null && this.iter.isProblemsOnly()) {
			this.problemsOnlyCB.setSelected(false);
			this.iter.next();
			r = (SwingRenderer) getIter().getRenderer();
		}
		this.rendererPanel.removeAll();
		if (r != null) {
			r.setSession(this);
			this.rendererPanel.add(r.getComponent(), "Center");
			r.activate();
			speak();
		} else {
			this.okButton.setEnabled(false);
			this.showSolutionButton.setEnabled(false);
			this.problemsOnlyCB.setEnabled(false);
		}
		updateStats();
		repaint();
	}

	public void process(boolean shouldShowSolution) {
		SwingRenderer r = (SwingRenderer) getIter().getRenderer();
		r.process(shouldShowSolution);
		if (this.iter.doEvaluate()) {
			updateRenderer();
		} else if (r.isShowSolution()) {
			r.showSolution();
			this.okButton.requestFocus();
			updateStats();
		} else {
			r.activate();
		}
	}

	public void init() {
		getIter().init();
	}

	public JButton getOkButton() {
		return this.okButton;
	}

	public void ok() {
		int mp = ((Integer) this.maxProblems.getValue()).intValue();
		if (mp > 0)
			this.problemsOnlyCB.setSelected((this.iter.countProblems() >= mp));
		if (this.iter.getRenderer().isShowSolution()) {
			getIter().next();
			updateRenderer();
		} else {
			process(false);
		}
	}

	public void showSolution() {
		process(true);
	}

	public void updateStats() {
		Statistics stat = getIter().getStatistics();
		this.statLabel.setText(stat.getCompleted() + " / " + stat.getTotal() + " / " + stat.getProblems());
		log.info(stat);
		if (getIter() != null && getIter().getQuestion() != null) {
			int i = getIter().getTimesForProblem(getIter().getQuestion());
			if (i > 0) {
				this.problemLabel.setText(String.valueOf(i));
			} else {
				this.problemLabel.setText("None");
			}
			this.finishedLabel.setText(String.valueOf(stat.getCompleted()));
			this.problemsLabel.setText(String.valueOf(stat.getProblems()));
			int rest = stat.getTotal() - stat.getCompleted() - stat.getProblems();
			this.restLabel.setText(String.valueOf(rest));
			int factor = 10;
			this.countPanelLayout.columnWeights = new double[] { (factor * stat.getCompleted()),
					(factor * stat.getProblems()), (factor * rest) };
		} else {
			this.problemLabel.setText("");
			this.finishedLabel.setText("-");
			this.problemsLabel.setText("-");
			this.restLabel.setText("");
			this.countPanelLayout.columnWeights = new double[] { 1.0D, 1.0D, 1.0D };
		}
	}

	public void speak() {
	}

	public QuestionIterator getIter() {
		return this.iter;
	}

	public void setIter(QuestionIterator iter) {
		this.iter = iter;
		if (iter != null) {
			this.total = iter.countQuestions();
			this.asked = 0;
		}
	}

	public boolean isPlay() {
		return this.playCB.isSelected();
	}

	private JPanel getCountPanel() {
		GridBagConstraints gridBagConstraints = null;
		GridBagConstraints gridBagConstraints1 = null;
		if (this.countPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.gridy = 1;
			this.restLabel = new JLabel();
			this.restLabel.setText("1");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			this.problemsLabel = new JLabel();
			this.problemsLabel.setText("1");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			this.finishedLabel = new JLabel();
			this.finishedLabel.setText("1");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.weightx = 1.0D;
			gridBagConstraints11.weighty = 1.0D;
			gridBagConstraints11.fill = 1;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.fill = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.fill = 1;
			gridBagConstraints.gridy = 0;
			this.countPanel = new JPanel();
			this.countPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
					BorderFactory.createLineBorder(Color.gray, 1)));
			this.countPanelLayout = new GridBagLayout();
			this.countPanel.setLayout(this.countPanelLayout);
			this.countPanel.add(getProblemColorPanel(), gridBagConstraints);
			this.countPanel.add(getRestColorPanel(), gridBagConstraints1);
			this.countPanel.add(getFinishedColorPanel(), gridBagConstraints11);
			this.countPanel.add(this.finishedLabel, gridBagConstraints2);
			this.countPanel.add(this.problemsLabel, gridBagConstraints3);
			this.countPanel.add(this.restLabel, gridBagConstraints4);
		}
		return this.countPanel;
	}

	private JPanel getLeftStatusPanel() {
		if (this.leftStatusPanel == null) {
			this.problemLabel = new JLabel();
			this.problemLabel.setFont(new Font("Courier", 0, 14));
			this.problemLabel.setText("0");
			this.jLabel2 = new JLabel();
			this.jLabel2.setText("Repetitions to do:");
			this.statLabel = new JLabel();
			this.statLabel.setFont(new Font("Courier", 0, 14));
			this.statLabel.setText("5/7");
			this.statLabel.setVisible(false);
			this.jLabel = new JLabel();
			this.jLabel.setText("Count:");
			this.jLabel.setVisible(false);
			this.leftStatusPanel = new JPanel();
			this.leftStatusPanel.add(this.jLabel, (Object) null);
			this.leftStatusPanel.add(this.statLabel, (Object) null);
			this.leftStatusPanel.add(this.jLabel2, (Object) null);
			this.leftStatusPanel.add(this.problemLabel, (Object) null);
		}
		return this.leftStatusPanel;
	}

	private JPanel getProblemColorPanel() {
		if (this.problemColorPanel == null) {
			this.problemColorPanel = new JPanel();
			this.problemColorPanel.setBackground(Color.red);
		}
		return this.problemColorPanel;
	}

	private JPanel getRestColorPanel() {
		if (this.restColorPanel == null) {
			this.restColorPanel = new JPanel();
			this.restColorPanel.setBackground(Color.white);
		}
		return this.restColorPanel;
	}

	private JPanel getFinishedColorPanel() {
		if (this.finishedColorPanel == null) {
			this.finishedColorPanel = new JPanel();
			this.finishedColorPanel.setBackground(Color.black);
		}
		return this.finishedColorPanel;
	}
}