package ch.unizh.ori.nabu.voc;

public class ImgColumn extends AbstractColumn {

	private static final long serialVersionUID = 334889814267376193L;

	private String prefix = "";

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isImage() {
		return true;
	}

	public Object map(String[] arr) {
		if (getColumn() >= arr.length) {
			return null;
		}
		return arr[getColumn()];
	}
}