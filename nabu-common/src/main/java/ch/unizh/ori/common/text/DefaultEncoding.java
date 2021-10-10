package ch.unizh.ori.common.text;

public abstract class DefaultEncoding implements Encoding {

	private static final long serialVersionUID = -6464496893068163944L;

	private String id;
	private String name;
	private String description;
	private Script script;

	public String getDescription() {
		return this.description;
	}

	public String getId() {
		return this.id;
	}

	public Script getScript() {
		return this.script;
	}

	public void setDescription(String string) {
		this.description = string;
	}

	public void setId(String string) {
		this.id = string;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public abstract Object convert(Text paramText);

	public abstract Text create(Object paramObject);

	public String getName() {
		return this.name;
	}

	public void setName(String string) {
		this.name = string;
	}
}