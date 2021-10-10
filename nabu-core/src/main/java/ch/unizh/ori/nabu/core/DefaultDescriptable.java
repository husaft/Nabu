package ch.unizh.ori.nabu.core;

import java.io.Serializable;

public class DefaultDescriptable implements Descriptable, Serializable {

	private static final long serialVersionUID = 4857995326049480999L;

	private String id;
	private String name;
	private String description;

	public DefaultDescriptable() {
	}

	public DefaultDescriptable(String id, String name) {
		setId(id);
		setName(name);
	}

	public String getDescription() {
		return this.description;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}
}