package elasticsearch;


public class Settings {
	public class Normalize {
		public static final int HI_NORM = 0;
		public static final int MED_NORM = 1;
		public static final int LO_NORM = 2;
		public static final int NO_NORM = 3;
	}
	
	public class Ngram {
		public static final int ON = 1;
		public static final int OFF = 0;
	}
	
	public static final boolean Newline = true;
	public static final boolean NoNewline = true;
}
