package ch.unizh.ori.common.text;

import java.util.List;

public interface TextScript extends Script {

	Object convert(String paramString, Text paramText);

	Text create(String paramString, Object paramObject);

	List<String> getEncodings();

}