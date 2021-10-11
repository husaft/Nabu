package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.common.text.DefaultScript;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.nabu.core.Utilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Mode extends DefaultDescriptable {

	private static final long serialVersionUID = -8515324072266408883L;

	private Vocabulary voc;
	private String myShort;
	private List<ModeField> modeFields;
	private Filter filter;
	private boolean unified = true;

	public String getShort() {
		return this.myShort;
	}

	public void setShort(String string) {
		this.myShort = string;
	}

	private List<ModeField> createFromShort(String _short) {
		List<String> l = Utilities.split(_short, "=");
		List<ModeField> ret = new ArrayList<ModeField>(l.size());
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			String s = iter.next();
			ModeField mf = new ModeField();
			if (s.startsWith("?")) {
				mf.setAsking(true);
				s = s.substring(1);
			}
			String presentationId = null;
			int indexOf = s.indexOf("#");
			if (indexOf >= 0) {
				presentationId = s.substring(indexOf + 1);
				s = s.substring(0, indexOf);
			}
			mf.setMode(this);
			mf.setKey(s);
			Vocabulary voc = getVoc();
			Column col = voc.getColumn(mf.getKey());
			mf.setColumn(col);

			if (mf.getColumn() instanceof StringColumn) {
				StringColumn column = (StringColumn) mf.getColumn();
				Script script = voc.getCentral().getScript(column.getScript());
				if (script != null) {
					if ((presentationId == null || presentationId.equals("")) && script instanceof DefaultScript) {

						DefaultScript defaultScript = (DefaultScript) script;
						if (mf.isAsking()) {
							presentationId = defaultScript.getDefaultEditablePresentation();
						} else {

							presentationId = defaultScript.getDefaultViewOnlyPresentation();
						}
					}

					mf.setPresentation(script.getPresentation(presentationId));
				}
			}
			ret.add(mf);
		}
		return ret;
	}

	public List<Map<String, Object>> unify(List<Map<String, Object>> voc) {
		if (!isUnified()) {
			return voc;
		}
		Map<Map<String, Object>, Map<String, Object>> newVoc = new HashMap<Map<String, Object>, Map<String, Object>>(
				voc.size());
		for (Iterator<Map<String, Object>> iter = voc.iterator(); iter.hasNext();) {
			Map<String, Object> o = iter.next();
			putIntoMap(newVoc, o);
		}
		return new ArrayList<Map<String, Object>>(newVoc.values());
	}

	protected void putIntoMap(Map<Map<String, Object>, Map<String, Object>> voc, Map<String, Object> q) {
		Map<String, Object> key = new HashMap<String, Object>();
		for (Iterator<ModeField> iter = getModeFields0().iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			if (!mf.isAsking()) {
				key.put(mf.getKey(), q.get(mf.getKey()));
			}
		}
		Map<String, Object> q2 = voc.get(key);
		if (q2 != null) {
			for (Iterator<ModeField> iterator = getModeFields0().iterator(); iterator.hasNext();) {
				ModeField mf = iterator.next();
				if (mf.isAsking()) {
					String key2 = mf.getKey();
					Object oldVal = q2.get(key2);
					Object newVal = q.get(key2);
					String val = oldVal + " ; " + newVal;
					q2.put(key2, val);
				}
			}
			System.err.println(q2);
		} else {
			voc.put(key, q);
		}
	}

	public List<ModeField> createModeFields() {
		getModeFields0();
		List<ModeField> l = new ArrayList<ModeField>(this.modeFields.size());
		for (int i = 0; i < l.size(); i++) {
			ModeField m = l.get(i);
			l.set(i, m.copy());
		}
		return this.modeFields;
	}

	private List<ModeField> getModeFields0() {
		if (this.modeFields == null && this.myShort != null && this.voc != null) {
			setModeFields(createFromShort(this.myShort));
		}
		return this.modeFields;
	}

	public String getName() {
		String name = super.getName();
		if (name == null && getModeFields0() != null) {
			name = createNiceName();
			setName(name);
		}
		return name;
	}

	private String createNiceName() {
		StringBuffer given = new StringBuffer();
		StringBuffer var = new StringBuffer();
		for (Iterator<ModeField> iter = this.modeFields.iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			StringBuffer s = !mf.isAsking() ? given : var;
			if (s.length() > 0) {
				s.append(", ");
			}
			s.append(mf.getColumn().getName());
		}
		return given.append(" -> ").append(var).toString();
	}

	public void setModeFields(List<ModeField> list) {
		this.modeFields = list;
	}

	public Vocabulary getVoc() {
		return this.voc;
	}

	public void setVoc(Vocabulary vocabulary) {
		this.voc = vocabulary;
	}

	public Filter getFilter() {
		return this.filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public boolean isUnified() {
		return this.unified;
	}

	public void setUnified(boolean unified) {
		this.unified = unified;
	}
}