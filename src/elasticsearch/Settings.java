package elasticsearch;

public class Settings {
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
	
	public static final boolean Newline = true;
	public static final boolean NoNewline = true;
}
