package elasticsearch.settings;

public class Settings {
	public static int BULK_SIZE = 100;

	public class Tokenizer {
		public static final String JAVA_CLASS_FILE = "references/JavaClass.isics";
		public static final String JAVA_PACKAGES_FILE = "references/JavaPackages.isics";
	}

	public class Normalize {
		public static final int WORD_NORM_ON = 1;
		public static final int WORD_NORM_OFF = 0;
		public static final int DATATYPE_NORM_ON = 1;
		public static final int DATATYPE_NORM_OFF = 0;
		public static final int JAVACLASS_NORM_ON = 1;
		public static final int JAVACLASS_NORM_OFF = 0;
		public static final int KEYWORD_NORM_ON = 1;
		public static final int KEYWORD_NORM_OFF = 0;
		public static final int ESCAPE_ON = 1;
		public static final int ESCAPE_OFF = 0;
		public static final int JAVAPACKAGE_NORM_ON = 1;
		public static final int JAVAPACKAGE_NORM_OFF = 0;
		public static final int STRING_NORM_ON = 1;
		public static final int STRING_NORM_OFF = 0;
		public static final int VALUE_NORM_ON = 1;
		public static final int VALUE_NORM_OFF = 0;
	}
	
	public class Ngram {
		public static final int ON = 1;
		public static final int OFF = 0;
	}
	
	public class IndexingMode {
		public static final int BULK = 0;
		public static final int SEQUENTIAL = 1;
	}
	
	public static final boolean Newline = true;
	public static final boolean NoNewline = true;
}
