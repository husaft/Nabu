package ch.unizh.ori.judit;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class Judit extends JFrame {

	private static final long serialVersionUID = -3464234648775898643L;

	private File file;
	private JScrollPane scrollPane;

	public Judit() {
		this.file = new File(System.getProperty("user.dir"));
		initComponents();
	}

	private JMenuItem quitItem;
	private JMenuItem openItem;
	private JMenu fileMenu;
	private JTextArea textArea;
	private JMenuItem saveAsItem;
	private JMenuBar menuBar;

	private void initComponents() {
		this.scrollPane = new JScrollPane();
		this.textArea = new JTextArea();
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu();
		this.openItem = new JMenuItem();
		this.saveAsItem = new JMenuItem();
		this.quitItem = new JMenuItem();

		setTitle("Judit");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Judit.this.exitForm(evt);
			}
		});

		this.scrollPane.setVerticalScrollBarPolicy(22);
		this.textArea.setFont(new Font("Dialog", 0, 18));
		this.textArea.setLineWrap(true);
		this.scrollPane.setViewportView(this.textArea);

		getContentPane().add(this.scrollPane, "Center");

		this.fileMenu.setMnemonic('F');
		this.fileMenu.setText("File");
		this.openItem.setAccelerator(KeyStroke.getKeyStroke(79, 2));
		this.openItem.setMnemonic('O');
		this.openItem.setText("Open");
		this.openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Judit.this.openItemActionPerformed(evt);
			}
		});

		this.fileMenu.add(this.openItem);
		this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(83, 2));
		this.saveAsItem.setMnemonic('S');
		this.saveAsItem.setText("Save");
		this.saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Judit.this.saveAsItemActionPerformed(evt);
			}
		});

		this.fileMenu.add(this.saveAsItem);
		this.quitItem.setAccelerator(KeyStroke.getKeyStroke(81, 2));
		this.quitItem.setMnemonic('Q');
		this.quitItem.setText("Quit");
		this.quitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Judit.this.quitItemActionPerformed(evt);
			}
		});

		this.fileMenu.add(this.quitItem);
		this.menuBar.add(this.fileMenu);
		setJMenuBar(this.menuBar);

		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(435, 326));
		setLocation((screenSize.width - 435) / 2, (screenSize.height - 326) / 2);
	}

	private void quitItemActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	private void saveAsItemActionPerformed(ActionEvent evt) {
		JFileChooser fd = new JFileChooser();
		fd.setSelectedFile(this.file);
		if (fd.showSaveDialog(this) == 0) {
			this.file = fd.getSelectedFile();
			try {
				BufferedWriter out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8"));
				out.write(this.textArea.getText());
				out.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void openItemActionPerformed(ActionEvent evt) {
		JFileChooser fd = new JFileChooser();
		fd.setSelectedFile(this.file);
		if (fd.showOpenDialog(this) == 0) {
			this.file = fd.getSelectedFile();
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), "UTF-8"));
				this.textArea.setText("");
				String line;
				while ((line = in.readLine()) != null) {
					this.textArea.append(line + "\n");
				}
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	public static void main(String[] args) {
		(new Judit()).setVisible(true);
	}
}