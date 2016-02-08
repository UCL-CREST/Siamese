package elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaTokenizer {
	private HashMap<String, Integer> keywordMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> datatypeMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> javaClassMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> javaPackagesMap = new HashMap<String, Integer>();
	private ArrayList<String> wordList = new ArrayList<String>();
	private int MODE = Settings.Normalize.MED_NORM;
	private boolean newline = Settings.Newline;

	public JavaTokenizer() {
		setUpKeywordMap();
		setUpDatatypeMap();
	}

	public JavaTokenizer(int mode) {
		setUpKeywordMap();
		setUpDatatypeMap();
		MODE = mode;
	}

	public ArrayList<String> tokenize(Reader reader) throws Exception {
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.parseNumbers();
		// Don't parse slash as part of numbers.
		tokenizer.ordinaryChar('/');
		// if low normalisation, all the Java packages
		// and its sub packages (having .) are not normalised
		// System.out.println(MODE);
		// if (MODE == Settings.Mode.LO_NORM) {
		// tokenizer.ordinaryChar('.');
		// }
		tokenizer.wordChars('_', '_');
		tokenizer.eolIsSignificant(false);
		tokenizer.ordinaryChars(0, ' ');
		tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		int tok;
		ArrayList<String> tokens = new ArrayList<String>();

		while ((tok = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
			switch (tok) {
			case StreamTokenizer.TT_NUMBER:
				tokens.add("V");
				break;
			case StreamTokenizer.TT_WORD:
				String word = tokenizer.sval;
				// System.out.println("W = " + word);
				if (MODE == Settings.Normalize.HI_NORM) {
					tokens.add("W");
				} else if (MODE == Settings.Normalize.MED_NORM) {
					if (!keywordMap.containsKey(word) && !datatypeMap.containsKey(word)) {
						tokens.add("W");
						if (!wordMap.containsKey(word)) {
							wordMap.put(word, 1);
							wordList.add(word);
						}
					} else {
						tokens.add(word.trim());
					}
				} else if (MODE == Settings.Normalize.LO_NORM) {
					if (!Character.isUpperCase(word.charAt(0)) && !keywordMap.containsKey(word)
							&& !datatypeMap.containsKey(word) && !javaClassMap.containsKey(word)
							&& !javaPackagesMap.containsKey(word)) {
						// System.out.println("W = " + word);
						tokens.add("W");
						if (!wordMap.containsKey(word)) {
							wordMap.put(word, 1);
							wordList.add(word);
						}
					} else {
						tokens.add(word.trim());
					}
				} else {
					throw new Exception("Wrong mode");
				}
				break;
			case '"':
				// String doublequote = tokenizer.sval;
				tokens.add("S");
				break;
			case '\'':
				// String singlequote = tokenizer.sval;
				tokens.add("C");
				break;
			case StreamTokenizer.TT_EOL:
				// if (newline = Settings.Newline)
				// 	tokens.add("\n");
				break;
			case StreamTokenizer.TT_EOF:
				break;
			default:
				char character = (char) tokenizer.ttype;
				if (!Character.isWhitespace(character) && character != '\n' && character != '\r') {
					tokens.add(String.valueOf(character));
				}
				break;
			}
		}

		reader.close();

		return tokens;
	}

	public ArrayList<String> getTokensFromFile(String file) throws Exception {
		// reset wordMap
		wordMap = new HashMap<String, Integer>();

		FileReader fileReader = new FileReader(file);
		readJavaClassNames("JavaClass.txt");
		readJavaPackages("JavaPackages.txt");
		return tokenize(fileReader);
	}

	public ArrayList<String> getTokensFromString(String input) throws Exception {
		// reset wordMap
		wordMap = new HashMap<String, Integer>();
		readJavaClassNames("JavaClass.txt");
		readJavaPackages("JavaPackages.txt");
		return tokenize(new StringReader(input));
	}

	public void readJavaClassNames(String filepath) {
		File file = new File(filepath);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				javaClassMap.put(line, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readJavaPackages(String filepath) {
		File file = new File(filepath);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				javaPackagesMap.put(line, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUpKeywordMap() {
		String[] keywords = { "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package",
				"synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements",
				"protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
				"return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface",
				"static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native",
				"super", "while" };
		for (int i = 0; i < keywords.length; i++) {
			keywordMap.put(keywords[i], 1);
		}
	}

	public void setUpDatatypeMap() {
		datatypeMap.put("byte", 1);
		datatypeMap.put("short", 1);
		datatypeMap.put("int", 1);
		datatypeMap.put("long", 1);
		datatypeMap.put("float", 1);
		datatypeMap.put("double", 1);
		datatypeMap.put("boolean", 1);
		datatypeMap.put("char", 1);
	}

	public boolean isKeyword(String x) {
		// System.out.println(x);
		if (keywordMap.get(x) != null)
			return true;
		else
			return false;
	}
}