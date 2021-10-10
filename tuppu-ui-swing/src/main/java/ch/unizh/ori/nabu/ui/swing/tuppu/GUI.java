package ch.unizh.ori.nabu.ui.swing.tuppu;

import ch.unizh.ori.nabu.ui.swing.AppFrame;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringPlotter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends AppFrame implements ActionListener {

	private static final long serialVersionUID = 3967529887339918185L;

	private PlotLabel plotLabel = new PlotLabel();

	private JTextField plotterTextField = new JTextField(StringPlotter.class.getName());

	private JTextField textTextField = new JTextField("Hello World!", 20);

	private JTextArea paramsTextArea = new JTextArea();

	public GUI() {
		super("Plotting", GUI.class.getName(), new Dimension(200, 100));
		getContentPane().add(this.plotLabel);

		JPanel p = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new GridLayout(0, 1));
		p2.add(this.plotterTextField);
		p2.add(this.textTextField);
		p.add(p2, "West");
		p.add(new JScrollPane(this.paramsTextArea));
		JButton b = new JButton("Update");
		b.setMnemonic('u');
		p.add(b, "East");
		getContentPane().add(p, "South");

		b.addActionListener(this);
		this.plotterTextField.addActionListener(this);
		this.textTextField.addActionListener(this);

		doUpdate();
	}

	@SuppressWarnings("deprecation")
	public void doUpdate() {
		try {
			Plotter p = (Plotter) Class.forName(this.plotterTextField.getText()).newInstance();

			Properties props = new Properties();
			props.load(new ByteArrayInputStream(this.paramsTextArea.getText().getBytes()));

			this.plotLabel.setAll(p, this.textTextField.getText(), props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		(new GUI()).setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		doUpdate();
	}
}