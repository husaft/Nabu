package ch.unizh.ori.common.text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionEncoding extends DefaultEncoding {

	private static final long serialVersionUID = 6451111450908368936L;

	private String convertName;
	private String createName;
	private String className;
	private Method convertMethod;
	private Method createMethod;
	private Class<?> clazz;
	private boolean inited = false;

	public Object convert(Text text) {
		if (!(text instanceof StringText)) {
			return null;
		}
		init();
		return invoke(((StringText) text).getText(), this.convertMethod);
	}

	public Text create(Object param) {
		String s = null;
		if (param != null) {
			s = param.toString();
		}
		return new StringText(getScript(), invoke(s, this.createMethod));
	}

	private String invoke(String text, Method method) {
		String[] args = { text };
		String ret = null;
		try {
			Object o = method.invoke(null, (Object[]) args);
			if (o != null) {
				ret = o.toString();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void init() {
		if (this.inited)
			return;
		try {
			this.clazz = Class.forName(getClassName());
			Class<?>[] param = { String.class };
			this.createMethod = this.clazz.getMethod(this.createName, param);
			this.convertMethod = this.clazz.getMethod(this.convertName, param);
			this.inited = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return getId() + "/" + getName() + ": " + this.className + "/" + this.createName + "/" + this.convertName;
	}

	public String getConvertName() {
		return this.convertName;
	}

	public String getCreateName() {
		return this.createName;
	}

	public void setConvertName(String string) {
		this.inited = false;
		this.convertName = string;
	}

	public void setCreateName(String string) {
		this.inited = false;
		this.createName = string;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String string) {
		this.inited = false;
		this.className = string;
	}
}