package ch.unizh.ori.nabu.voc;

import java.beans.Expression;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class ConvertSupport implements Serializable {

	private static final long serialVersionUID = -9075643805236373836L;

	private String className;
	private Class<?> clazz;
	private String methodName;

	public Object map(Object obj) throws InvocationTargetException, Exception {
		Expression ex = new Expression(this.clazz, this.methodName, new Object[] { obj });
		Object value = ex.getValue();
		return value;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setClassName(String string) throws SecurityException, NoSuchMethodException {
		this.className = string;
		this.clazz = null;
		if (this.className != null) {
			try {
				this.clazz = Class.forName(this.className);
				updateMethod();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void setMethodName(String string) throws SecurityException, NoSuchMethodException {
		this.methodName = string;
		updateMethod();
	}

	private void updateMethod() throws SecurityException, NoSuchMethodException {
	}
}