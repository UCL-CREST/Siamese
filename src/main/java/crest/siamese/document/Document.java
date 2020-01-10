/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.document;

public class Document {
	private long id;
	private String file;
	private int startline;
	private int endline;
	private String source;
	private String t2Source;
	private String t1Source;
	private String tokenizedSource;
	private String originalSource;
	private String license;
	private String url;

	public Document() {
	    super();
    }

	public Document(
	        long id,
            String file,
            int startline,
            int endline,
            String source,
            String t2Source,
            String t1Source,
            String tokenizedSource,
            String originalSource,
            String license,
            String url) {
		this.id = id;
        this.file = file;
        this.startline = startline;
        this.endline = endline;
		this.source = source;
		this.t2Source = t2Source;
		this.t1Source = t1Source;
		this.tokenizedSource = tokenizedSource;
		this.originalSource = originalSource;
		this.license = license;
		this.url = url;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public String getT2Source() {
		return t2Source;
	}

	public void setT2Source(String t2Source) {
		this.t2Source = t2Source;
	}

	public String getT1Source() {
		return t1Source;
	}

	public void setT1Source(String t1Source) {
		this.t1Source = t1Source;
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

        if (this.id != other.id) {
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
