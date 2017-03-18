package elasticsearch.document;

public class Document {
	private String id;
	private String file;
	private String source;

    public Document() {

    }
	
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Document.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Document other = (Document) obj;

        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }

        if ((this.file == null) ? (other.file != null) : !this.file.equals(other.file)) {
            return false;
        }

        if ((this.source == null) ? (other.source != null) : !this.source.equals(other.source)) {
            return false;
        }

        return true;
    }
}
