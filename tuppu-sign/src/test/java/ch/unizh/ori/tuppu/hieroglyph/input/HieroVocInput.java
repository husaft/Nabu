package ch.unizh.ori.tuppu.hieroglyph.input;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import ch.unizh.ori.tuppu.hieroglyph.HieroLabel;

public class HieroVocInput extends JFrame {

	private static final long serialVersionUID = -4652628480033327326L;

	private Action enterAction;
	private Action editAction;
	private Action saveAction;
	private JLabel jLabel4;
	private JScrollPane jScrollPane1;
	private JPanel hieroPanel;
	private JLabel jLabel1;
	private JMenu fileMenu;
	private JLabel jLabel3;
	private JLabel jLabel2;
	private JTextField translitTf;
	private JToolBar toolBar;
	private JTextField codageTf;
	private JMenuBar menuBar;
	private JTable vocTable;
	private JTextField traductTf;
	private JPanel jPanel1;
	private VocItem editVocItem;
	private PropertyChangeSupport propertyChangeSupport;
	private List<HieroVocInput.VocItem> voc;
	private File filename;
	private HieroLabel hieroLabel;

	@SuppressWarnings("unused")
	private boolean dirty;

	private int editR;
	public static final int R_NEW = -1;
	public static final int R_NULL = -2;

	@SuppressWarnings("serial")
	private void initComponents() {
		this.jPanel1 = new JPanel();
		this.jLabel1 = new JLabel();
		this.codageTf = new JTextField();
		this.jLabel2 = new JLabel();
		this.hieroPanel = new JPanel();
		this.jLabel3 = new JLabel();
		this.translitTf = new JTextField();
		this.jLabel4 = new JLabel();
		this.traductTf = new JTextField();
		this.jScrollPane1 = new JScrollPane();
		this.vocTable = new JTable();
		this.toolBar = new JToolBar();
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu();
		setTitle("HieroVocInput");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				HieroVocInput.this.exitForm(evt);
			}
		});
		this.jLabel1.setText("Codage:");
		this.jPanel1.add(this.jLabel1);
		this.codageTf.setColumns(15);
		this.jPanel1.add(this.codageTf);
		this.jLabel2.setText("Hiero:");
		this.jPanel1.add(this.jLabel2);
		this.hieroPanel.setLayout(new BorderLayout());
		this.jPanel1.add(this.hieroPanel);
		this.jLabel3.setText("Translit.:");
		this.jPanel1.add(this.jLabel3);
		this.translitTf.setColumns(10);
		this.jPanel1.add(this.translitTf);
		this.jLabel4.setText("Traduction:");
		this.jPanel1.add(this.jLabel4);
		this.traductTf.setColumns(20);
		this.jPanel1.add(this.traductTf);
		getContentPane().add(this.jPanel1, "South");
		this.jScrollPane1.setVerticalScrollBarPolicy(22);
		this.vocTable.setModel(new DefaultTableModel(
				new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
						{ null, null, null, null }, },
				(Object[]) new String[] { "Codage", "Hiero", "Translit", "Traduction" }) {
			Class<?>[] types = new Class[] { String.class, String.class, String.class, String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public Class<?> getColumnClass(int columnIndex) {
				return this.types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return this.canEdit[columnIndex];
			}
		});
		this.jScrollPane1.setViewportView(this.vocTable);
		getContentPane().add(this.jScrollPane1, "Center");
		getContentPane().add(this.toolBar, "North");
		this.fileMenu.setText("File");
		this.menuBar.add(this.fileMenu);
		setJMenuBar(this.menuBar);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(892, 394));
		setLocation((screenSize.width - 892) / 2, (screenSize.height - 394) / 2);
	}

	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	public void open(File filename) {
		this.voc = new ArrayList<VocItem>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = in.readLine()) != null)
				this.voc.add(new VocItem(line));
			this.filename = filename;
			this.dirty = false;
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}
		this.vocTable.setModel(new MyTableModel());
		getTableModel().fireTableRowsUpdated(0, this.voc.size() - 1);
	}

	public void addVocItem(VocItem vi) {
		this.voc.add(vi);
		getTableModel().fireTableRowsInserted(this.voc.size() - 1, this.voc.size() - 1);
		this.dirty = true;
	}

	public MyTableModel getTableModel() {
		return (MyTableModel) this.vocTable.getModel();
	}

	@SuppressWarnings("serial")
	public HieroVocInput() {
		this.enterAction = new AbstractAction("Enter") {
			public void actionPerformed(ActionEvent e) {
				HieroVocInput.VocItem vi = HieroVocInput.this.getEditVocItem();
				if (vi != null) {
					vi.code = HieroVocInput.this.codageTf.getText();
					vi.translit = HieroVocInput.this.translitTf.getText();
					vi.traduct = HieroVocInput.this.traductTf.getText();
					if (HieroVocInput.this.editR == -1) {
						HieroVocInput.this.addVocItem(vi);
					} else {
						HieroVocInput.this.dirty = true;
						HieroVocInput.this.getTableModel().fireTableRowsUpdated(HieroVocInput.this.editR,
								HieroVocInput.this.editR);
					}
					HieroVocInput.this.editR = -2;
				}
				HieroVocInput.this.setEditVocItem(new HieroVocInput.VocItem());
				HieroVocInput.this.editR = -1;
			}
		};

		this.editAction = new AbstractAction("Edit") {
			public void actionPerformed(ActionEvent e) {
				int r = HieroVocInput.this.vocTable.getSelectedRow();
				if (r < 0)
					return;
				HieroVocInput.this.editR = r;
				HieroVocInput.this.setEditVocItem(HieroVocInput.this.voc.get(r));
			}
		};

		this.saveAction = new AbstractAction("Save") {
			public void actionPerformed(ActionEvent e) {
				if (HieroVocInput.this.filename == null) {
					JFileChooser fc = new JFileChooser();
					if (fc.showSaveDialog(null) != 0) {
						return;
					}
					HieroVocInput.this.filename = fc.getSelectedFile();
				}
				PrintWriter out = null;
				try {
					out = new PrintWriter(new FileWriter(HieroVocInput.this.filename));
					Iterator<HieroVocInput.VocItem> iter = HieroVocInput.this.voc.iterator();
					while (iter.hasNext()) {
						HieroVocInput.VocItem vi = iter.next();
						out.println(vi);
					}
					HieroVocInput.this.dirty = true;
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					out.close();
				}
			}
		};

		this.propertyChangeSupport = new PropertyChangeSupport(this);

		this.filename = null;

		this.dirty = false;
		this.editR = -2;
		initComponents();
		this.hieroLabel = new HieroLabel();
		this.hieroPanel.add((Component) this.hieroLabel);
		this.hieroLabel.addTextField(this.codageTf);
		SignChooser.showSupport(this.codageTf);
		this.toolBar.add(this.saveAction);
		this.toolBar.add(this.editAction);
		this.toolBar.add(this.enterAction);
	}

	public static void main(String[] args) {
		HieroVocInput hv = new HieroVocInput();
		hv.setVisible(true);
		hv.open(new File(args[0]));
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		this.propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		this.propertyChangeSupport.removePropertyChangeListener(l);
	}

	public VocItem getEditVocItem() {
		return this.editVocItem;
	}

	public void setEditVocItem(VocItem editVocItem) {
		VocItem oldEditVocItem = this.editVocItem;
		this.editVocItem = editVocItem;
		this.propertyChangeSupport.firePropertyChange("editVocItem", oldEditVocItem, editVocItem);
		if (editVocItem != null) {
			this.codageTf.setText(editVocItem.code);
			this.translitTf.setText(editVocItem.translit);
			this.traductTf.setText(editVocItem.traduct);
			this.codageTf.selectAll();
			this.codageTf.requestFocus();
		} else {
			this.codageTf.setText("");
			this.translitTf.setText("");
			this.traductTf.setText("");
		}
	}

	public class VocItem {
		public String code;
		public String translit;

		public VocItem(String code, String translit, String traduct) {
			this.code = code;
			this.translit = translit;
			this.traduct = traduct;
		}

		public String traduct;

		public VocItem() {
		}

		public VocItem(String line) {
			String[] s = line.split("\\t");
			this.code = s[0];
			this.translit = s[1];
			this.traduct = s[2];
		}

		public String toString() {
			return this.code + "\t" + this.translit + "\t" + this.traduct;
		}
	}

	public class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 6576951883659457763L;

		public int getColumnCount() {
			return 4;
		}

		public int getRowCount() {
			return HieroVocInput.this.voc.size();
		}

		public Object getValueAt(int r, int c) {
			HieroVocInput.VocItem vi = HieroVocInput.this.voc.get(r);
			switch (c) {
			case 0:
				return vi.code;
			case 1:
				return vi.code;
			case 2:
				return vi.translit;
			case 3:
				return vi.traduct;
			}
			return null;
		}
	}
}