package ch.unizh.ori.nabu.core.api;

public interface HttpSession {

	Object getAttribute(String name);

	void setAttribute(String name, Object value);

}