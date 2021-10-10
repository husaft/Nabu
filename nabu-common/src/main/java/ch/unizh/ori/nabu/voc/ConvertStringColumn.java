package ch.unizh.ori.nabu.voc;

import java.lang.reflect.InvocationTargetException;

public class ConvertStringColumn extends StringColumn {

	private static final long serialVersionUID = 7804781764112134016L;

	private ConvertSupport convertSupport = new ConvertSupport();

	public Object map(String[] arr) {
		Object ret = super.map(arr);
		try {
			Object value = this.convertSupport.map(ret);
			return value;
		} catch (InvocationTargetException e) {
			System.err.println("ConvertStringColumn.map(): InvocationTargetException. Cause");
			e.getCause().printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String getClassName() {
		return this.convertSupport.getClassName();
	}

	public String getMethodName() {
		return this.convertSupport.getMethodName();
	}

	public void setClassName(String string) throws SecurityException, NoSuchMethodException {
		this.convertSupport.setClassName(string);
	}

	public void setMethodName(String string) throws SecurityException, NoSuchMethodException {
		this.convertSupport.setMethodName(string);
	}
}