package elasticsearch;

public class TokenizerMode {
	private int word;
	private int keyword;
	private int datatype;
	private int javaClass;
	private int javaPackage;
	private int string;
	private int value;
	private int escape;

	public TokenizerMode() {
		word = Settings.Normalize.WORD_NORM_OFF;
		keyword = Settings.Normalize.KEYWORD_NORM_OFF;
		javaClass = Settings.Normalize.JAVACLASS_NORM_OFF;
		javaPackage = Settings.Normalize.JAVAPACKAGE_NORM_OFF;
		string = Settings.Normalize.STRING_NORM_OFF;
		value = Settings.Normalize.STRING_NORM_OFF;
		escape = Settings.Normalize.ESCAPE_OFF;
		datatype = Settings.Normalize.DATATYPE_NORM_OFF;
	}

	public TokenizerMode(int word, int keyword, int datatype, int javaClass, int javaPackage, int string, int value, int escape) {
		this.word = word;
		this.keyword = keyword;
		this.javaClass = javaClass;
		this.javaPackage = javaPackage;
		this.string = string;
		this.value = value;
		this.escape = escape;
		this.datatype = datatype;
	}

	public int getWord() {
		return word;
	}

	public void setWord(int word) {
		this.word = word;
	}

	public int getKeyword() {
		return keyword;
	}

	public void setKeyword(int keyword) {
		this.keyword = keyword;
	}

	public int getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(int javaClass) {
		this.javaClass = javaClass;
	}

	public int getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(int javaPackage) {
		this.javaPackage = javaPackage;
	}

	public int getString() {
		return string;
	}

	public void setString(int string) {
		this.string = string;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getEscape() {
		return escape;
	}

	public void setEscape(int escape) {
		this.escape = escape;
	}

	public int getDatatype() {
		return datatype;
	}

	public void setDatatype(int datatype) {
		this.datatype = datatype;
	}
}
