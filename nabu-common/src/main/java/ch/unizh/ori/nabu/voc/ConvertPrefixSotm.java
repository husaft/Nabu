package ch.unizh.ori.nabu.voc;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ConvertPrefixSotm extends PrefixSotm {

	private static final long serialVersionUID = -7341837786566447787L;

	private ConvertSupport convertSupport = new ConvertSupport();

	public String getUtterance(String toSay, Map<Object, Object> question) {
		try {
			return (String) this.convertSupport.map(toSay);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return toSay;
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