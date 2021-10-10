package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.core.QuestionProducer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CartesianMorphQuestionProducer extends QuestionProducer {

	private static final long serialVersionUID = -4705144623006353148L;

	public List<String> roots = new ArrayList<String>();
	public String[][] space = new String[0][];
	public String[] coordinateNames = new String[0];
	@SuppressWarnings("rawtypes")
	protected Map rootClasses;
	@SuppressWarnings("rawtypes")
	protected Map stemsMap;
	@SuppressWarnings("rawtypes")
	protected Map formatters;
	@SuppressWarnings("rawtypes")
	protected Map labels;

	public Object produceNext() {
		int r = (int) Math.floor(Math.random() * this.roots.size());
		String root = this.roots.get(r);

		String[] coords = new String[this.space.length];
		for (int i = 0; i < coords.length; i++) {
			int s = (int) Math.floor(Math.random() * (this.space[i]).length);
			coords[i] = this.space[i][s];
		}

		String form = form(root, coords);

		if (Math.random() >= 0.5D) {
			return new CartesianMorphQuestion.Analyse(root, coords, form);
		}
		return new CartesianMorphQuestion.Construct(root, coords, form);
	}

	public boolean isList() {
		return false;
	}

	public int countQuestions() {
		throw new IllegalArgumentException("Should not be called");
	}

	public void initSession() {
	}

	public String[] getCoordinateNames() {
		return this.coordinateNames;
	}

	public String[][] getSpace() {
		return this.space;
	}

	public String getCoordinateLabel(int i) {
		return (String) this.labels.get(String.valueOf(i));
	}

	public String getValueLabel(int i, int j) {
		return (String) this.labels.get(i + "," + j);
	}

	public CartesianMorphQuestionProducer() {
		this.rootClasses = new HashMap<Object, Object>();
		this.stemsMap = new HashMap<Object, Object>();

		this.labels = new HashMap<Object, Object>();
	}

	protected String form(String root, String[] coords) {
		String rootClass = rootClass(root);
		MessageFormat formatter = formatter(rootClass, coords);
		if (formatter == null) {
			return null;
		}
		return formatter.format(stems(root));
	}

	protected String rootClass(String root) {
		return (String) this.rootClasses.get(root);
	}

	protected String[] stems(String root) {
		return (String[]) this.stemsMap.get(root);
	}

	protected MessageFormat formatter(String rootClass, String[] coords) {
		StringBuffer key = new StringBuffer(rootClass);
		for (int i = 0; i < coords.length; i++)
			key.append((i == 0) ? "_" : ".").append(coords[i]);
		return (MessageFormat) this.formatters.get(key.toString());
	}

	@SuppressWarnings("unchecked")
	public void loadForms(Properties forms) {
		this.coordinateNames = forms.getProperty("product").split(",");
		forms.remove("product");
		this.space = new String[this.coordinateNames.length][];
		for (int i = 0; i < this.space.length; i++) {
			String key = "set." + this.coordinateNames[i];
			this.space[i] = forms.getProperty(key).split(",");
			forms.remove(key);
			String label = "label." + this.coordinateNames[i];
			this.labels.put(String.valueOf(i), forms.getProperty(label));
			for (int j = 0; j < (this.space[i]).length; j++) {
				String s = label + "." + this.space[i][j];
				this.labels.put(i + "," + j, forms.getProperty(s));
			}
		}

		for (Enumeration<Object> enumeration = forms.keys(); enumeration.hasMoreElements();) {
			String key = (String) enumeration.nextElement();
			String mp = forms.getProperty(key);
			forms.put(key, new MessageFormat(mp));
		}
		this.formatters = forms;
	}

	@SuppressWarnings("unchecked")
	public void loadRoots(BufferedReader in) throws IOException {
		String line;
		while ((line = in.readLine()) != null) {
			if (line.equals("") || line.startsWith("#")) {
				continue;
			}
			String[] fields = line.split("\t");

			if (fields.length < 2) {
				System.out.println("Problem with voc");
				continue;
			}
			String root = fields[0];
			this.roots.add(root);
			this.rootClasses.put(root, fields[1]);
			String[] stems = new String[fields.length - 2];
			System.arraycopy(fields, 2, stems, 0, stems.length);
			this.stemsMap.put(root, stems);
		}
	}

	public static CartesianMorphQuestionProducer load(Properties forms, BufferedReader in) throws IOException {
		CartesianMorphQuestionProducer ret = new CartesianMorphQuestionProducer();
		ret.loadForms(forms);
		ret.loadRoots(in);
		return ret;
	}

	public static CartesianMorphQuestionProducer load(String formsUrl, String rootsUrl, String encoding)
			throws IOException {
		CartesianMorphQuestionProducer ret = null;

		Properties forms = new Properties();
		InputStream formsIn = null;
		try {
			formsIn = new FileInputStream(formsUrl);
			forms.load(formsIn);
			formsIn.close();
		} catch (IOException ex) {
			if (formsIn != null)
				try {
					formsIn.close();
				} catch (Throwable t) {
				}
			throw ex;
		}

		BufferedReader rootsIn = null;
		try {
			rootsIn = new BufferedReader(new InputStreamReader(new FileInputStream(rootsUrl), encoding));
			ret = load(forms, rootsIn);
			rootsIn.close();
		} catch (IOException ex) {
			if (rootsIn != null)
				try {
					rootsIn.close();
				} catch (Throwable t) {
				}
			throw ex;
		}

		return ret;
	}

	public static void main(String[] args) {
		try {
			String formsUrl = "/home/pht/projekte/nabu/web/vocs/sanskritforms.properties";
			String rootsUrl = "/home/pht/projekte/nabu/web/vocs/sanskritroots.txt";
			CartesianMorphQuestionProducer p = load(formsUrl, rootsUrl, "UTF-8");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!in.readLine().startsWith("q")) {
				Object q = p.produceNext();
				System.out.println("q: " + q.toString());
				if (in.readLine().startsWith("q"))
					return;
				System.out.println("a: " + q.toString());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void finishSession() {
	}
}