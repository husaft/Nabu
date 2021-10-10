package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.Map;
import java.util.regex.Pattern;

public class Filter extends DefaultDescriptable {

	private static final long serialVersionUID = -4833954444816542585L;

	private Mode mode;
	private String field;
	private String starts = null;
	private String ends = null;
	private String contains = null;
	private String pattern = null;
	private Pattern thePattern = null;
	private String except = null;
	private Pattern theExceptPattern = null;

	public boolean accept(Map<String, String> voc) {
		if (this.field == null) {
			throw new NullPointerException("Field is null!");
		}
		String m = voc.get(this.field);
		if (this.starts != null && !m.startsWith(this.starts)) {
			return false;
		}

		if (this.ends != null && !m.endsWith(this.ends)) {
			return false;
		}

		if (this.contains != null && m.indexOf(this.contains) < 0) {
			return false;
		}

		if (this.thePattern != null && !this.thePattern.matcher(m).matches()) {
			return false;
		}

		if (this.theExceptPattern != null && this.theExceptPattern.matcher(m).matches()) {
			return false;
		}

		return true;
	}

	public String getContains() {
		return this.contains;
	}

	public void setContains(String contains) {
		this.contains = contains;
	}

	public String getEnds() {
		return this.ends;
	}

	public void setEnds(String ends) {
		this.ends = ends;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String getStarts() {
		return this.starts;
	}

	public void setStarts(String starts) {
		this.starts = starts;
	}

	public String getPattern() {
		return this.pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		if (pattern != null) {
			this.thePattern = Pattern.compile(pattern);
		} else {
			this.thePattern = null;
		}
	}

	public String getExcept() {
		return this.except;
	}

	public void setExcept(String except) {
		this.except = except;
		if (except != null) {
			this.theExceptPattern = Pattern.compile(except);
		} else {
			this.theExceptPattern = null;
		}
	}
}