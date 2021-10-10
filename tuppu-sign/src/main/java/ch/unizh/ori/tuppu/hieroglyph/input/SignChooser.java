package ch.unizh.ori.tuppu.hieroglyph.input;

import ch.unizh.ori.tuppu.hieroglyph.FontMapper;
import ch.unizh.ori.tuppu.hieroglyph.HieroLabel;
import ch.unizh.ori.tuppu.hieroglyph.HieroglyphicSigns;
import ch.unizh.ori.tuppu.hieroglyph.HieroglyphicSigns.Donne;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

public class SignChooser extends JPanel {

	private static final long serialVersionUID = -3866742466500350339L;

	public static final int INDEX_CLASSES = 0;
	public static final int INDEX_SHAPES = 1;
	public static final int INDEX_FIND = 2;
	private List<JComponent> classesFocusers;
	private List<JComponent> shapesFocusers;
	private MyListSelectionListener listSelectionListener;
	private MyMouseListener mouseListener;
	private MyKeyListener keyListener;
	private HieroListRenderer cellRenderer;
	private JLabel jLabel4;
	private JPanel findTab;
	private JScrollPane jScrollPane1;
	private JLabel jLabel1;
	private JPanel jPanel3;
	private JTabbedPane selectionPane;
	private JLabel jLabel2;
	private JTabbedPane classesTab;
	private JPanel jPanel2;
	private JPanel signRendererPane;
	private JTextField findTf;
	private JList<Donne> findList;
	private JTextField nameTf;
	private JPanel jPanel7;
	private JTextField phonTf;
	private JTextField codesTf;
	private JLabel jLabel6;
	private JTabbedPane formsTab;
	private JPanel signPane;
	private HieroglyphicSigns.Donne selectedSign;
	private PropertyChangeSupport propertyChangeSupport;
	private HieroLabel signLabel;
	private EventListenerList listenerList;
	private FontMapper listFontMapper;
	private JComponent back;

	public SignChooser() {
		this.listSelectionListener = new MyListSelectionListener();

		this.mouseListener = new MyMouseListener();

		this.keyListener = new MyKeyListener();

		this.cellRenderer = new HieroListRenderer();

		this.propertyChangeSupport = new PropertyChangeSupport(this);

		this.listenerList = null;
		initComponents();
		myInitComponents();
	}

	@SuppressWarnings("serial")
	private void myInitComponents() {
		this.signLabel = new HieroLabel();
		this.signLabel.setFontSize(80);
		this.signLabel.setBackground(Color.white);
		this.signRendererPane.add((Component) this.signLabel, "Center");
		handleJList(this.findList);
		initClassnames();
		initShapes();
		this.selectionPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (SignChooser.this.selectionPane.getSelectedIndex()) {
				case 0:
					((JComponent) SignChooser.this.classesFocusers.get(SignChooser.this.classesTab.getSelectedIndex()))
							.requestFocus();
					break;
				case 1:
					((JComponent) SignChooser.this.shapesFocusers.get(SignChooser.this.formsTab.getSelectedIndex()))
							.requestFocus();
					break;
				case 2:
					SignChooser.this.findTf.selectAll();
					SignChooser.this.findTf.requestFocus();
					break;
				}
			}
		});
		putActionName(this, "F2", "focusClasses");
		putActionName(this, "F3", "focusShapes");
		putActionName(this, "F4", "focusFind");
		putActionName(this, "ESCAPE", "back");
		getActionMap().put("focusClasses", new FocusTopAction(0));
		getActionMap().put("focusShapes", new FocusTopAction(1));
		getActionMap().put("focusFind", new FocusTopAction(2));
		getActionMap().put("back", new AbstractAction("back") {
			public void actionPerformed(ActionEvent e) {
				if (SignChooser.this.back != null)
					SignChooser.this.back.requestFocus();
			}
		});
		addInsertListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("inserting " + e.getActionCommand());
			}
		});
	}

	public static void putActionName(JComponent comp, String keyStroke, String actionName) {
		comp.getInputMap(1).put(KeyStroke.getKeyStroke(keyStroke), actionName);
	}

	private void initComponents() {
		this.selectionPane = new JTabbedPane();
		this.classesTab = new JTabbedPane();
		this.jPanel3 = new JPanel();
		this.formsTab = new JTabbedPane();
		this.findTab = new JPanel();
		this.jPanel7 = new JPanel();
		this.jLabel1 = new JLabel();
		this.findTf = new JTextField();
		this.jScrollPane1 = new JScrollPane();
		this.findList = new JList<Donne>();
		this.signPane = new JPanel();
		this.jPanel2 = new JPanel();
		this.jLabel2 = new JLabel();
		this.nameTf = new JTextField();
		this.jLabel4 = new JLabel();
		this.phonTf = new JTextField();
		this.jLabel6 = new JLabel();
		this.codesTf = new JTextField();
		this.signRendererPane = new JPanel();
		setLayout(new BorderLayout());
		this.selectionPane.setBorder(new TitledBorder("Select"));
		this.classesTab.setTabPlacement(3);
		this.selectionPane.addTab("Classes", this.classesTab);
		this.jPanel3.setLayout(new BorderLayout());
		this.formsTab.setTabPlacement(3);
		this.jPanel3.add(this.formsTab, "Center");
		this.selectionPane.addTab("Forms", this.jPanel3);
		this.findTab.setLayout(new BorderLayout());
		this.jPanel7.setLayout(new GridBagLayout());
		this.jLabel1.setText("Phon:");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 5);
		this.jPanel7.add(this.jLabel1, gridBagConstraints);
		this.findTf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SignChooser.this.findTfActionPerformed(evt);
			}
		});
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = 1;
		gridBagConstraints.weightx = 1.0D;
		this.jPanel7.add(this.findTf, gridBagConstraints);
		this.findTab.add(this.jPanel7, "North");
		this.jScrollPane1.setVerticalScrollBarPolicy(22);
		this.jScrollPane1.setViewportView(this.findList);
		this.findTab.add(this.jScrollPane1, "Center");
		this.selectionPane.addTab("Find", this.findTab);
		add(this.selectionPane, "Center");
		this.signPane.setLayout(new BorderLayout());
		this.signPane.setBorder(new TitledBorder("Selected Sign"));
		this.jPanel2.setLayout(new GridBagLayout());
		this.jLabel2.setText("Name:");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = 17;
		gridBagConstraints.weightx = 1.0D;
		this.jPanel2.add(this.jLabel2, gridBagConstraints);
		this.nameTf.setColumns(10);
		this.nameTf.setEditable(false);
		this.nameTf.setText("V17");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.fill = 1;
		this.jPanel2.add(this.nameTf, gridBagConstraints);
		this.jLabel4.setText("Phon:");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = 17;
		this.jPanel2.add(this.jLabel4, gridBagConstraints);
		this.phonTf.setColumns(10);
		this.phonTf.setEditable(false);
		this.phonTf.setText("sDm");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.fill = 1;
		this.jPanel2.add(this.phonTf, gridBagConstraints);
		this.jLabel6.setText("Codes:");
		this.jLabel6.setToolTipText("null");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = 17;
		this.jPanel2.add(this.jLabel6, gridBagConstraints);
		this.codesTf.setColumns(10);
		this.codesTf.setEditable(false);
		this.codesTf.setText("sDm, V17");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridwidth = 0;
		gridBagConstraints.fill = 1;
		this.jPanel2.add(this.codesTf, gridBagConstraints);
		this.signPane.add(this.jPanel2, "North");
		this.signRendererPane.setLayout(new BorderLayout());
		this.signPane.add(this.signRendererPane, "Center");
		add(this.signPane, "East");
	}

	private void findTfActionPerformed(ActionEvent evt) {
		find();
	}

	private void initClassnames() {
		readClassnames();
	}

	protected void readClassnames() {
		String filename = "classnames.txt";
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(SignChooser.class.getResourceAsStream(filename)));
			char ch = 'A';
			int index = 0;
			List<JComponent> focusers = new ArrayList<JComponent>();
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() >= 1) {
					String c = String.valueOf(ch);
					String mnemonic = c;
					if (ch == '[') {
						c = "Aa";
						mnemonic = "J";
					}
					Donne[] array = HieroglyphicSigns.getDefault().getClasses(c).toArray(new Donne[0]);
					JList<Donne> list = new JList<Donne>(array);
					handleJList(list);
					focusers.add(list);
					this.classesTab.add(c + ": " + line, new JScrollPane(list));
					this.classesTab.setMnemonicAt(index, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
					index++;
				}
				ch = (char) (ch + 1);
			}
			this.classesTab.addChangeListener(new MyChangeListener(focusers));
			this.classesFocusers = focusers;
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initShapes() {
		readShapes();
	}

	protected void readShapes() {
		String filename = "shapes.txt";
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(SignChooser.class.getResourceAsStream(filename)));
			int index = 0;
			List<JComponent> focusers = new ArrayList<JComponent>();
			String line;
			while ((line = in.readLine()) != null) {
				String s = in.readLine();
				if (s != null) {
					String[] ss = s.split("\\s+");
					List<Donne> l = new ArrayList<Donne>(ss.length);
					for (int i = 0; i < ss.length; i++) {
						Donne o = HieroglyphicSigns.getDefault().getDonne(ss[i]);
						if (o != null)
							l.add(o);
					}
					JList<Donne> list = new JList<Donne>(l.toArray(new Donne[0]));
					handleJList(list);
					focusers.add(list);
					this.formsTab.add(line, new JScrollPane(list));
					String mnemonic = String.valueOf(line.charAt(0)).toUpperCase();
					this.formsTab.setMnemonicAt(index, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
					index++;
				}
			}
			this.formsTab.addChangeListener(new MyChangeListener(focusers));
			this.shapesFocusers = focusers;
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void handleJList(JList<Donne> l) {
		l.setCellRenderer(this.cellRenderer);
		l.setSelectionMode(0);
		l.addListSelectionListener(this.listSelectionListener);
		l.addMouseListener(this.mouseListener);
		l.addKeyListener(this.keyListener);
	}

	public static class HieroListRenderer extends HieroLabel implements ListCellRenderer<HieroglyphicSigns.Donne> {

		private static final long serialVersionUID = 3521828556318824633L;

		protected Border noFocusBorder;

		public HieroListRenderer() {
			this.noFocusBorder = new EmptyBorder(1, 1, 1, 1);
			setOpaque(true);
			setBorder(this.noFocusBorder);
		}

		public Component getListCellRendererComponent(JList<? extends Donne> list, HieroglyphicSigns.Donne value,
				int index, boolean isSelected, boolean cellHasFocus) {
			HieroglyphicSigns.Donne d = value;
			String text = (d.entry == null) ? "" : d.entry;
			setText(text);
			setComponentOrientation(list.getComponentOrientation());
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setBorder(cellHasFocus ? UIManager.getBorder("List.focusCellHighlightBorder") : this.noFocusBorder);
			return (Component) this;
		}
	}

	public class MyListSelectionListener implements ListSelectionListener {
		@SuppressWarnings("unchecked")
		public void valueChanged(ListSelectionEvent e) {
			SignChooser.this.setSelectedSign(((JList<HieroglyphicSigns.Donne>) e.getSource()).getSelectedValue());
		}
	}

	public class MyMouseListener extends MouseAdapter {
		@SuppressWarnings("unchecked")
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				HieroglyphicSigns.Donne d = ((JList<HieroglyphicSigns.Donne>) e.getSource()).getSelectedValue();
				SignChooser.this.setSelectedSign(d);
				SignChooser.this.insertDonne(d);
			}
		}
	}

	public class MyKeyListener extends KeyAdapter {
		@SuppressWarnings("unchecked")
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 10) {
				HieroglyphicSigns.Donne d = ((JList<HieroglyphicSigns.Donne>) e.getSource()).getSelectedValue();
				SignChooser.this.setSelectedSign(d);
				SignChooser.this.insertDonne(d);
			}
		}
	}

	public class MyChangeListener implements ChangeListener {
		private List<JComponent> focusers;

		public MyChangeListener(List<JComponent> focusers) {
			this.focusers = focusers;
		}

		public void stateChanged(ChangeEvent e) {
			JTabbedPane p = (JTabbedPane) e.getSource();
			JComponent c = this.focusers.get(p.getSelectedIndex());
			c.requestFocus();
		}
	}

	public class FocusTopAction extends AbstractAction {

		private static final long serialVersionUID = -2705562093852702259L;

		private int index;

		public FocusTopAction(int index) {
			this.index = index;
		}

		public void actionPerformed(ActionEvent actionEvent) {
			SignChooser.this.selectionPane.setSelectedIndex(this.index);
			if (this.index == 0) {
				((JComponent) SignChooser.this.classesFocusers.get(SignChooser.this.classesTab.getSelectedIndex()))
						.requestFocus();
			} else if (this.index == 1) {
				((JComponent) SignChooser.this.shapesFocusers.get(SignChooser.this.formsTab.getSelectedIndex()))
						.requestFocus();
			}
		}
	}

	public class TextAction implements ActionListener {
		private JTextComponent tc;

		public TextAction(JTextComponent tc) {
			this.tc = tc;
		}

		public void actionPerformed(ActionEvent actionEvent) {
			this.tc.replaceSelection(actionEvent.getActionCommand());
			this.tc.requestFocus();
		}
	}

	protected FontMapper getListFontMapper() {
		if (this.listFontMapper == null) {
			FontRenderContext frc = ((Graphics2D) getGraphics()).getFontRenderContext();
			this.listFontMapper = new FontMapper(frc, 30, false, HieroglyphicSigns.getDefault().getBase());
		}
		return this.listFontMapper;
	}

	public void find() {
		List<Donne> l = HieroglyphicSigns.getDefault().getPhon(this.findTf.getText());
		if (l != null) {
			ListListModel model = new ListListModel(l);
			this.findList.setModel(model);
			this.findList.requestFocus();
		}
	}

	public void insertDonne(HieroglyphicSigns.Donne d) {
		fireInsertListenerActionPerformed(new ActionEvent(this, 1001, (getSelectedSign()).entry));
	}

	public static class ListListModel extends AbstractListModel<Donne> {

		private static final long serialVersionUID = 7602029991249777142L;

		private List<Donne> list;

		public ListListModel(List<Donne> list) {
			this.list = list;
		}

		public int getSize() {
			return this.list.size();
		}

		public Donne getElementAt(int i) {
			return this.list.get(i);
		}
	}

	public void addTextField(JTextComponent tc) {
		addInsertListener(new TextAction(tc));
		tc.getInputMap(0).put(KeyStroke.getKeyStroke("F2"), "focusClasses");
		tc.getInputMap(0).put(KeyStroke.getKeyStroke("F3"), "focusShapes");
		tc.getInputMap(0).put(KeyStroke.getKeyStroke("F4"), "focusFind");
		tc.getActionMap().put("focusClasses", new FocusTopAction(0));
		tc.getActionMap().put("focusShapes", new FocusTopAction(1));
		tc.getActionMap().put("focusFind", new FocusTopAction(2));
		this.back = tc;
	}

	public static void showSupport(JTextComponent tf) {
		JFrame f = new JFrame("SignChooser");
		SignChooser sg = new SignChooser();
		sg.addTextField(tf);
		f.getContentPane().add(sg);
		f.setSize(800, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((screenSize.width - 800) / 2, (screenSize.height - 800) / 2);
		f.setDefaultCloseOperation(3);
		f.setVisible(true);
	}

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		JFrame f = new JFrame("SignChooser");
		JPanel p = new JPanel(new GridLayout(1, 2, 5, 0));
		SignChooser sg = new SignChooser();
		final JTextField tf = new JTextField();
		HieroLabel l = new HieroLabel();
		l.setFontSize(40);
		sg.addTextField(tf);
		l.addTextField(tf);
		p.add(tf);
		p.add((Component) l);
		f.getContentPane().add(sg);
		f.getContentPane().add(p, "North");
		f.setSize(800, 800);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((screenSize.width - 800) / 2, (screenSize.height - 800) / 2);
		f.setDefaultCloseOperation(3);
		tf.getInputMap().put(KeyStroke.getKeyStroke("control S"), "save");
		tf.getActionMap().put("save", new AbstractAction("save") {
			public void actionPerformed(ActionEvent e) {
				tf.selectAll();
				tf.copy();
			}
		});
		f.setVisible(true);
		tf.requestFocus();
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		this.propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		this.propertyChangeSupport.removePropertyChangeListener(l);
	}

	public HieroglyphicSigns.Donne getSelectedSign() {
		return this.selectedSign;
	}

	public void setSelectedSign(HieroglyphicSigns.Donne selectedSign) {
		HieroglyphicSigns.Donne oldSelectedSign = this.selectedSign;
		this.selectedSign = selectedSign;
		this.propertyChangeSupport.firePropertyChange("selectedSign", oldSelectedSign, selectedSign);
		if (selectedSign != null) {
			this.signLabel.setText(selectedSign.entry);
			this.nameTf.setText(selectedSign.entry);
			this.phonTf.setText(selectedSign.phone);
			List<HieroglyphicSigns.Donne> codes = HieroglyphicSigns.getDefault().getCodes(selectedSign);
			Iterator<HieroglyphicSigns.Donne> iter = codes.iterator();
			StringBuffer s = new StringBuffer();
			while (iter.hasNext()) {
				HieroglyphicSigns.Donne d = iter.next();
				s.append(d.entry).append(" ");
			}
			this.codesTf.setText(s.toString().trim());
		} else {
			this.signLabel.setText("");
			this.nameTf.setText("");
			this.phonTf.setText("");
			this.codesTf.setText("");
		}
	}

	public synchronized void addInsertListener(ActionListener listener) {
		if (this.listenerList == null)
			this.listenerList = new EventListenerList();
		this.listenerList.add(ActionListener.class, listener);
	}

	public synchronized void removeInsertListener(ActionListener listener) {
		this.listenerList.remove(ActionListener.class, listener);
	}

	private void fireInsertListenerActionPerformed(ActionEvent event) {
		if (this.listenerList == null)
			return;
		Object[] listeners = this.listenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class)
				((ActionListener) listeners[i + 1]).actionPerformed(event);
		}
	}
}