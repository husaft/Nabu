package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

public class Voice extends DefaultDescriptable {

	private static final long serialVersionUID = -6803092143483286429L;

	private String prefix;
	private String type;

	public String getPrefix() {
		return this.prefix;
	}

	public String getType() {
		return this.type;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setType(String type) {
		this.type = type;
	}
}