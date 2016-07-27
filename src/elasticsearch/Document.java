package elasticsearch;

public class Document {
	private String id;
	private String file;
	private String source;
	
	public Document(String id, String file, String source) {
		this.id = id;
        this.file = file;
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
	public void setFile(String file) {
	    this.file = file;
    }

    public String getFile() { return this.file; }
	
	public String toString() {
		return id + ":" + file + ": " + source;
	}
	
}
