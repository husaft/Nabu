package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Descriptable;
import java.util.List;
import java.util.Map;

public interface Sotm extends Descriptable {

	String getUtterance(String string, Map<Object, Object> map);

	List<Voice> getVoices();

}