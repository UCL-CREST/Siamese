package crest.isics.document;

public class Document {
	private String id;
	private String file;
	private int startline;
	private int endline;
	private String source;
	private String tokenizedSource;
	private String originalSource;
	private String license;
	private String url;

	public Document() {
	    super();
    }

	public Document(
	        String id,
            String file,
            int startline,
            int endline,
            String source,
            String tokenizedSource,
            String originalSource,
            String license,
            String url) {
		this.id = id;
        this.file = file;
        this.startline = startline;
        this.endline = endline;
		this.source = source;
		this.tokenizedSource = tokenizedSource;
		this.originalSource = originalSource;
		this.license = license;
		this.url = url;
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
    public String getOriginalSource() { return  originalSource; }
    public String getLicense() { return license; }
    public void setOriginalSource(String originalSource) { this.originalSource = originalSource; }
    public void setLicense(String license) { this.license = license; }
    public String getFile() { return this.file; }
    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }
    public int getStartLine() { return this.startline; }
    public int getEndLine() { return this.endline; }

	public int getStartline() {
		return startline;
	}

	public int getEndline() {
		return endline;
	}

	public String getTokenizedSource() {
		return tokenizedSource;
	}

	public void setTokenizedSource(String tokenizedSource) {
		this.tokenizedSource = tokenizedSource;
	}

	public void setStartline(int startline) {
		this.startline = startline;
	}

	public void setEndline(int endline) {
		this.endline = endline;
	}

	public String toString() {
		return id + ":" + file + ": " + source;
	}

	public String getLocationString() {
	    return file + "(" + startline + "," + endline + ")";
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
