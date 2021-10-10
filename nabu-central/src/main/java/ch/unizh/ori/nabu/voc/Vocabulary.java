package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.core.ListQuestionProducer;
import ch.unizh.ori.nabu.core.QuestionProducer;
import ch.unizh.ori.nabu.core.Utilities;
import ch.unizh.ori.nabu.ui.DefaultRendererChooser;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.ui.RendererChooser;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Vocabulary extends DefaultDescriptable {

	private static final long serialVersionUID = -3556885433515758321L;

	private Central central;
	private URL base;
	private List<FieldStream> lections;
	private Map<String, Mode> modes;
	private int count;
	private Map<String, SndColumn> sounds = new HashMap<String, SndColumn>();

	private List<Column> columns;

	private Map<String, Column> columnsMap;

	private Source<?> src;

	private boolean decorate = false;

	public Vocabulary() {
		this.modes = new TreeMap<String, Mode>();
		this.columns = new ArrayList<Column>();
		this.columnsMap = new HashMap<String, Column>();
	}

	@SuppressWarnings("unchecked")
	public List<FieldStream> getLections() {
		if (this.lections == null) {
			try {
				this.lections = (List<FieldStream>) getSrc().readLections(this.base);

				int total = 0;
				for (Iterator<FieldStream> iter = this.lections.iterator(); iter.hasNext();) {
					FieldStream fs = iter.next();
					total += fs.getCount();
				}
				setCount(total);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.lections;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setLections(List<FieldStream> list) {
		this.lections = list;
	}

	public Map<String, Mode> getModes() {
		return this.modes;
	}

	public void setModes(Map<String, Mode> map) {
		this.modes = map;
	}

	public void addMode(Mode m) {
		this.modes.put(m.getId(), m);
	}

	public void addCol(Column col) {
		if (col.getColumn() < 0) {
			int prevIndex;
			if (this.columns.size() >= 1) {
				prevIndex = ((Column) this.columns.get(this.columns.size() - 1)).getColumn();
			} else {
				prevIndex = -1;
			}
			col.setColumn(prevIndex + 2 + col.getColumn());
		}
		col.setCentral(this.central);
		this.columns.add(col);
		this.columnsMap.put(col.getId(), col);
	}

	public void addSnd(SndColumn sc) {
		addCol(sc);
		this.sounds.put(sc.getAd(), sc);
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> createQuestion(String[] arr, String lection, int id) {
		Map<String, Object> ret = new HashMap<String, Object>(this.columns.size());
		if (this.decorate) {
			ret.put("id", new Integer(id));
			ret.put("lesson", lection);
		}
		for (Iterator<Column> iter = this.columns.iterator(); iter.hasNext();) {
			Column col = iter.next();
			ret.put(col.getId(), col.map(arr));
		}
		return ret;
	}

	public QuestionProducer createProducer(List<FieldStream> lections, Filter filter, Mode mode) {
		return createProducer(lections, filter, mode, true);
	}

	@SuppressWarnings("unchecked")
	public QuestionProducer createProducer(List<FieldStream> lections, Filter filter, Mode mode, boolean shuffle) {
		List<Map<String, Object>> voc = createVocList(lections);
		if (filter != null) {
			for (Iterator<Map<String, Object>> iter = voc.iterator(); iter.hasNext();) {
				Map<String, Object> v = iter.next();
				if (!filter.accept((Map<String, String>) (Object) v)) {
					iter.remove();
				}
			}
		}
		if (mode != null) {
			voc = mode.unify(voc);
		}
		if (shuffle)
			Collections.shuffle(voc);
		ListQuestionProducer qp = new ListQuestionProducer(voc);
		return (QuestionProducer) qp;
	}

	public List<Map<String, Object>> createVocList(List<FieldStream> lections) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		for (Iterator<FieldStream> iter = lections.iterator(); iter.hasNext();) {
			FieldStream fs = iter.next();
			try {
				createVoc(fs, ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public void createVoc(FieldStream fs, Collection<Map<String, Object>> voc) throws Exception {
		Object param = fs.start();

		int id = 0;
		String[] arr;
		while ((arr = fs.next(param)) != null) {
			Map<String, Object> m = createQuestion(arr, fs.getId(), ++id);
			voc.add(m);
		}
		fs.stop(param);
	}

	public Central getCentral() {
		return this.central;
	}

	public void setCentral(Central central) {
		this.central = central;
	}

	public List<Column> getColumns() {
		if (this.columns == null && getSrc().getHeader() != null) {
			List<String> cols = Utilities.split(getSrc().getHeader(), "\t");
			this.columns = new ArrayList<Column>(cols.size());
			for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
				String colString = iter.next();

				String[] data = colString.split(":");
				int i = 0;
				if (i >= data.length)
					continue;
				StringColumn col = new StringColumn();
				col.setId(data[i++]);
				this.columns.add(col);
				if (i >= data.length)
					continue;
				col.setName(data[i++]);
				if (i >= data.length)
					continue;
				col.setScript(data[i++]);
			}
		}
		return this.columns;
	}

	public Column getColumn(String key) {
		return (Column) this.columnsMap.get(key);
	}

	public Sotm getSound(String key) {
		Sotm sotm = (Sotm) this.sounds.get(key);
		if (sotm == null && getColumn(key) instanceof StringColumn) {
			StringColumn col = (StringColumn) getColumn(key);
			String script = col.getScript();
			if (script != null) {
				sotm = getCentral().getSotm(script);
			}
		}
		return sotm;
	}

	public Source<?> getSrc() {
		return this.src;
	}

	public void setColumns(List<Column> list) {
		this.columns = list;
	}

	public void setSrc(Source<?> source) {
		this.src = source;
	}

	public URL getBase() {
		return this.base;
	}

	public void setBase(URL url) {
		this.base = url;
	}

	public DefaultQuestionIterator createIter(List<FieldStream> lections, Renderer r) {
		return createIter(lections, r, null, null);
	}

	public DefaultQuestionIterator createIter(List<FieldStream> lections, Renderer r, Filter filter, Mode mode) {
		QuestionProducer prod = createProducer(lections, filter, mode);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(prod, r);
		DefaultRendererChooser chooser = new DefaultRendererChooser();
		chooser.setRendererMap(map);

		DefaultQuestionIterator iter = new DefaultQuestionIterator();
		iter.addProducer(prod);
		iter.setRendererChooser((RendererChooser) chooser);

		return iter;
	}

	public Map<String, Column> getColumnsMap() {
		return this.columnsMap;
	}

	public Map<String, SndColumn> getSounds() {
		return this.sounds;
	}

	public String toString() {
		return getName();
	}

	public boolean isDecorate() {
		return this.decorate;
	}

	public void setDecorate(boolean decorate) {
		this.decorate = decorate;
	}
}