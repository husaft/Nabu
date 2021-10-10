package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.common.text.Presentation;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.Map;

public class ModeField extends DefaultDescriptable implements Cloneable {

	private static final long serialVersionUID = -2113395225442054426L;

	private boolean asking;
	private String key;
	private String label;
	private String type;
	private Presentation presentation;
	private Mode mode;
	private Column column;
	private Map<String, Object> question;
	private Object value;

	public ModeField copy() {
		ModeField m = new ModeField();
		m.asking = this.asking;
		m.key = this.key;
		m.label = this.label;
		m.type = this.type;
		m.question = this.question;
		m.value = this.value;
		m.column = this.column;
		return m;
	}

	protected Object clone() {
		return copy();
	}

	public boolean isAsking() {
		return this.asking;
	}

	public String getKey() {
		return this.key;
	}

	public String getLabel() {
		if (this.label == null) {
			this.label = getColumn().getName();
		}
		return this.label;
	}

	public String getName() {
		String name = super.getName();
		if (name == null) {
			name = (isAsking() ? "?" : "") + getColumn().getName();
			setName(name);
		}
		return name;
	}

	public Map<String, Object> getQuestion() {
		return this.question;
	}

	public String getType() {
		return this.type;
	}

	public Object getValue() {
		return this.value;
	}

	public void setAsking(boolean b) {
		this.asking = b;
	}

	public void setKey(String string) {
		this.key = string;
	}

	public void setLabel(String string) {
		this.label = string;
	}

	public void setQuestion(Map<String, Object> map) {
		this.question = map;
		if (this.question == null) {
			this.value = null;
		} else {
			this.value = map.get(this.key);
		}
	}

	public void setType(String string) {
		this.type = string;
	}

	public Column getColumn() {
		return this.column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public Mode getMode() {
		return this.mode;
	}

	public boolean isImage() {
		if (getColumn() instanceof ImgColumn)
			return true;
		return getPresentation() instanceof ch.unizh.ori.common.text.PlotterPresentation;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Presentation getPresentation() {
		return this.presentation;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}
}