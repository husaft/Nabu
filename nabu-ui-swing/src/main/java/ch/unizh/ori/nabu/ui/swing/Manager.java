package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.nabu.catalog.QuestionProducerDescription;
import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.Vocabulary;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class Manager extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 2560410450707054385L;

	private SwingCentral central;
	private JList<Vocabulary> vocList;
	private JList<Mode> modesList;
	private JList<FieldStream> lessonsList;
	private JButton startButton;
	private JButton openFolderButton = null;
	private List<File> addedDirs;

	private void initComponents() {
		setLayout(new BorderLayout());

		this.vocList = new JList<Vocabulary>();
		this.vocList.setSelectionMode(0);
		this.vocList.addListSelectionListener(this);
		JScrollPane sp = new JScrollPane(this.vocList);
		decorateWithBorder(sp, "Vocabularies:");
		add(sp, "Before");

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, 2));

		this.modesList = new JList<Mode>();
		this.modesList.setSelectionMode(0);

		sp = new JScrollPane(this.modesList);
		decorateWithBorder(sp, "Modes:");
		p.add(sp);

		this.lessonsList = new JList<FieldStream>();
		this.lessonsList.setSelectionMode(2);

		sp = new JScrollPane(this.lessonsList);
		decorateWithBorder(sp, "Lections:");
		p.add(sp);

		add(p, "Center");

		p = new JPanel(new FlowLayout(1));

		this.startButton = new JButton("Start Session");
		this.startButton.setMnemonic('s');
		p.add(this.startButton);
		this.startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Manager.this.startSession();
			}
		});

		JButton reloadButton = new JButton("Reload");
		reloadButton.setMnemonic('r');
		p.add(reloadButton);
		p.add(getOpenFolderButton(), (Object) null);
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Manager.this.reload();
			}
		});

		add(p, "South");
	}

	private void decorateWithBorder(JScrollPane sp, String title) {
		TitledBorder b = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
		sp.setBorder(b);
	}

	public void addAll(DefaultMutableTreeNode node, List<QuestionProducerDescription> l) {
		if (l == null)
			return;
		for (Iterator<QuestionProducerDescription> i = l.iterator(); i.hasNext();) {
			QuestionProducerDescription qpd = i.next();

			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(qpd);
			node.add(node2);
			addAll(node2, qpd.getSubQuestionProducerDescriptions());
		}
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public void startSession() {
		Vocabulary voc = this.vocList.getSelectedValue();
		Mode m = this.modesList.getSelectedValue();
		List lections = Arrays.asList(this.lessonsList.getSelectedValues());
		if (voc != null && m != null && lections != null && lections.size() > 0)
			createNabuSession(voc, m, lections);
	}

	public Manager() {
		this.addedDirs = new ArrayList<File>();
		initComponents();
		this.central = new SwingCentral();
		reload();
	}

	public void reload() {
		this.central.clear();
		this.central.readResource();
		this.central.readDir(System.getProperty("user.home") + "/vocs/");

		if (System.getProperty("vocs") != null) {
			this.central.readDirs(System.getProperty("vocs"));
		}
		if (System.getenv("vocs") != null) {
			this.central.readDirs(System.getenv("vocs"));
		}
		for (Iterator<File> iter = this.addedDirs.iterator(); iter.hasNext();) {
			File dir = iter.next();
			this.central.readDir(dir.getAbsolutePath());
		}
		updateLists();
	}

	private void updateLists() {
		DefaultListModel<Vocabulary> listModel = new DefaultListModel<Vocabulary>();
		for (Iterator<String> iter = this.central.getVocs().keySet().iterator(); iter.hasNext();) {
			String voc = iter.next();
			listModel.addElement(this.central.getVocs().get(voc));
		}
		this.vocList.setModel(listModel);
		revalidate();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == this.vocList) {
			Vocabulary v = this.vocList.getSelectedValue();
			if (v != null) {
				DefaultListModel<Mode> mListModel = new DefaultListModel<Mode>();
				Map<String, Mode> modes = v.getModes();
				for (Iterator<String> iter = modes.keySet().iterator(); iter.hasNext();) {
					String m = iter.next();
					mListModel.addElement(v.getModes().get(m));
				}
				this.modesList.setModel(mListModel);
				if (mListModel.size() > 0) {
					this.modesList.setSelectedIndex(0);
				}

				DefaultListModel<FieldStream> lListModel = new DefaultListModel<FieldStream>();
				List<FieldStream> lections = v.getLections();
				for (Iterator<FieldStream> iter = lections.iterator(); iter.hasNext();) {
					lListModel.addElement(iter.next());
				}
				this.lessonsList.setModel(lListModel);
				if (lListModel.size() > 0) {
					this.lessonsList.setSelectedIndex(0);
				}
			} else {
				((DefaultListModel<?>) this.modesList.getModel()).removeAllElements();
				((DefaultListModel<?>) this.lessonsList.getModel()).removeAllElements();
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void createNabuSession(Vocabulary voc, Mode m, List<String> lections) {
		SwingMappingRenderer r = new SwingMappingRenderer(m);
		DefaultQuestionIterator iter = voc.createIter((List) lections, r, m.getFilter(), m);
		createNabuSession(iter);
	}

	public static void createNabuSession(QuestionIterator iter) {
		JFrame f = new AppFrame("Nabu Session", "nabu.session", new Dimension(500, 300));
		NabuSession n = new NabuSession();
		n.setIter(iter);
		f.getContentPane().add(n, "Center");
		f.setDefaultCloseOperation(2);
		n.postAdd();

		n.init();
		iter.next();
		n.updateRenderer();

		f.setVisible(true);
		((SwingRenderer) iter.getRenderer()).activate();
	}

	private JButton getOpenFolderButton() {
		if (this.openFolderButton == null) {
			this.openFolderButton = new JButton();
			this.openFolderButton.setText("Open Folder");
			this.openFolderButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Manager.this.openFolder();
				}
			});
		}
		return this.openFolderButton;
	}

	protected void openFolder() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(1);
		if (fc.showOpenDialog(this) == 0) {
			File dir = fc.getSelectedFile();
			this.addedDirs.add(dir);
			this.central.readDir(dir.getAbsolutePath());
			updateLists();
		}
	}

	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("-test")) {
			test();
		} else {
			Manager m = new Manager();
			m.createFrame();
		}
	}

	private void createFrame() {
		JFrame f = new AppFrame("Nabu", "nabu.manager", new Dimension(500, 300));
		f.getContentPane().add(this, "Center");
		f.setVisible(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void test() {
		Central c = new Central();
		try {
			c.digestXML("etc/voc.xml");
			Vocabulary voc = c.getVocs().get("sw");
			Mode m = voc.getModes().get("1");
			List<String> lections = (List) Collections.singletonList(voc.getLections().get(0));
			createNabuSession(voc, m, lections);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}