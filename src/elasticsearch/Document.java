package elasticsearch;

public class Document {
	private String id;
	private String source;
	
	public Document(String id, String source) {
		this.id = id;
		this.source = source;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String toString() {
		return id + ": " + source;
	}
	
}
