package ch.unizh.ori.common.text;

import java.lang.reflect.Method;

public class ConvertStringPresentation extends StringPresentation {

	private static final long serialVersionUID = -77175215027504057L;

	private Class<?> clazz;
	private String methodName;
	private transient Method method;

	public String getOutText(Object value, String enc) {
		try {
			return (String) getMethod().invoke(null, (Object[]) new String[] { super.getOutText(value, enc) });
		} catch (Exception ex) {
			throw new RuntimeException("Problem invoking: value=" + value, ex);
		}
	}

	public void setClassName(String classname) {
		try {
			this.clazz = Class.forName(classname);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Problem with class: " + classname, ex);
		}
	}

	public void setMethodName(String methodname) {
		this.methodName = methodname;
	}

	private Method getMethod() {
		if (this.method == null) {
			try {
				this.method = this.clazz.getMethod(this.methodName, new Class[] { String.class });
			} catch (Exception ex) {
				throw new RuntimeException("Problem with method: " + this.methodName + " in class " + this.clazz, ex);
			}
		}
		return this.method;
	}
}