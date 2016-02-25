package elasticsearch;

import java.util.ArrayList;

public class JavaMethod implements Comparable {
	private int id;
	private String fileName;
	private String methodName;
	private String method;
	private int startLine;
	private int endLine;
	private String signatureHash;
	private ArrayList<String> ngrams;

	public JavaMethod(int id, String fileName, String methodName, String method, int startLine, int endLine, String sigHash) {
		this.id = id;
		this.fileName = fileName;
		this.methodName = methodName;
		this.method = method;
		this.startLine = startLine;
		this.endLine = endLine;
		this.signatureHash = sigHash;
	}
	
	public int compareTo(Object o)
	{
		JavaMethod x = (JavaMethod) o;
		if (this.fileName == x.fileName && this.startLine == x.startLine
				&& this.endLine == x.endLine) {
			return 0;
		} else if (this.fileName == x.fileName && this.startLine < x.startLine) {
			return -1;
		} else if (this.fileName == x.fileName && this.startLine > x.startLine) {
			return 1;
		} else {
			return -1;
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public String getSignatureHash() {
		return signatureHash;
	}

	public void setSignatureHash(String signatureHash) {
		this.signatureHash = signatureHash;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ArrayList<String> getNgrams() {
		return ngrams;
	}

	public void setNgrams(ArrayList<String> ngrams) {
		this.ngrams = ngrams;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
