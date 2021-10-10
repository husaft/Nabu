package ch.unizh.ori.nabu.core;

import java.io.Serializable;

public interface Descriptable extends Serializable {

	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String desc);

}