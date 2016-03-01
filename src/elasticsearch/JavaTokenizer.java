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
import java.util.Scanner;

public class JavaTokenizer {
	private HashMap<String, Integer> keywordMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> datatypeMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> javaClassMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> javaPackagesMap = new HashMap<String, Integer>();
	private ArrayList<String> wordList = new ArrayList<String>();
	private TokenizerMode modes;
	private ArrayList<String> tokens = new ArrayList<String>();
	private String prevChar = "";
	private String prevDatatype = "";
	private StreamTokenizer tokenizer;

	public JavaTokenizer() {
		modes = new TokenizerMode();
		setUpKeywordMap();
		setUpDatatypeMap();
	}

	public JavaTokenizer(TokenizerMode modes) {
		setUpKeywordMap();
		setUpDatatypeMap();
		this.modes = modes;
	}

	public ArrayList<String> tokenize(Reader reader) throws Exception {
		tokenizer = new StreamTokenizer(reader);
		tokenizer.parseNumbers();
		// Don't parse slash as part of numbers.
		tokenizer.ordinaryChar('/');
		tokenizer.wordChars('_', '_');
		tokenizer.eolIsSignificant(false);
		tokenizer.ordinaryChars(0, ' ');
		tokenizer.slashSlashComments(true);
		tokenizer.slashStarComments(true);
		int tok;

		while ((tok = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
			normalizeAToken(tok);
		}
		reader.close();

		return tokens;
	}

	private void hiNormalizeAToken(int tok) {
		switch (tok) {
		case StreamTokenizer.TT_NUMBER:
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			tokens.add("V");
			break;
		case StreamTokenizer.TT_WORD:
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}

			tokens.add("W");

			break;
		case '"':
			// String doublequote = tokenizer.sval;
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			tokens.add("S");
			break;
		case '\'':
			// String singlequote = tokenizer.sval;
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			tokens.add("C");
			break;
		case StreamTokenizer.TT_EOL:
			// if (newline == Settings.Newline)
			// tokens.add("\n");
			break;
		case StreamTokenizer.TT_EOF:
			break;
		default:
			char character = (char) tokenizer.ttype;
			String cStr = String.valueOf(character);
			if (!Character.isWhitespace(character) && character != '\n' && character != '\r') {
				if (cStr.equals("+") || cStr.equals("-") || cStr.equals("*") || cStr.equals("/") || cStr.equals("%")) {
					// nothing found before this
					if (prevChar.equals(""))
						prevChar = cStr;
					else if (prevChar.equals("+")) {
						tokens.add("++");
						prevChar = "";
					} else if (prevChar.equals("-")) {
						tokens.add("--");
						prevChar = "";
					}
				} else if (cStr.equals(">") || cStr.equals("<")) {
					prevChar = cStr;
				} else if (cStr.equals("&") || cStr.equals("|")) {
					if (prevChar.equals("&") || prevChar.equals("|")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else {
						prevChar = cStr;
					}
				} else if (cStr.equals("=")) {
					if (prevChar.equals("=") || prevChar.equals("!")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else if (prevChar.equals(">") || prevChar.equals("<")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*")
							|| prevChar.equals("/") || prevChar.equals("%")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else {
						prevChar = "=";
					}
				} else if (cStr.equals("!")) {
					prevChar = cStr;
				} else if (prevChar.equals("=")) {
					tokens.add("=");
					tokens.add(cStr);
					prevChar = "";
				} else {
					tokens.add(cStr);
					prevChar = "";
				}
			}
			break;
		}
	}

	public void normalizeAToken(int tok) throws Exception {
		switch (tok) {
		case StreamTokenizer.TT_NUMBER:
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			/*** TODO: FIX THIS PROBLEM OF INT vs. DOUBLE **/
			/*
			 * if (modes.getValue() == Settings.Normalize.VALUE_NORM_ON)
			 * tokens.add("V"); else { if (!prevDatatype.equals("")) { if
			 * (prevDatatype.equals("int"))
			 * tokens.add(String.valueOf((int)tokenizer.nval)); else if
			 * (prevDatatype.equals("long"))
			 * tokens.add(String.valueOf((long)tokenizer.nval)); else if
			 * (prevDatatype.equals("byte"))
			 * tokens.add(String.valueOf((byte)tokenizer.nval)); else if
			 * (prevDatatype.equals("short"))
			 * tokens.add(String.valueOf((short)tokenizer.nval)); else
			 * tokens.add(String.valueOf(tokenizer.nval)); // if it has comma in
			 * the declaration, still use the same data type. if
			 * (!prevChar.equals(",")) prevDatatype = ""; } else {
			 * tokens.add(String.valueOf(tokenizer.nval)); } }
			 */
			tokens.add("V");
			break;
		case StreamTokenizer.TT_WORD:
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}

			String word = tokenizer.sval;
			// System.out.println("|" + word + "|");
			// Convert keywords to "K"
			if (datatypeMap.containsKey(word)) {
				if (modes.getDatatype() == Settings.Normalize.DATATYPE_NORM_ON) {
					tokens.add("D");
				} else
					tokens.add(word);
				// set the value of the latest seen data type (for converting to
				// the right value)
				prevDatatype = word;
			} else if (keywordMap.containsKey(word)) {
				if (modes.getKeyword() == Settings.Normalize.KEYWORD_NORM_ON)
					tokens.add("K");
				else
					tokens.add(word);
			} else if (/* Character.isUpperCase(word.charAt(0)) || */javaClassMap.containsKey(word)) {
				if (modes.getJavaClass() == Settings.Normalize.JAVACLASS_NORM_ON)
					tokens.add("J");
				else
					tokens.add(word);
			} else if (word.contains(".")) {
				//System.out.println("|" + word + "|");
				String[] splitWord = word.split("\\.");
				for (String s : splitWord) {
					//System.out.println("s=" + s);
					if (javaPackagesMap.containsKey(s) && modes.getJavaPackage() == Settings.Normalize.JAVAPACKAGE_NORM_ON) {
							tokens.add("P");
					} else {
						tokens.add(s);
					}
					tokens.add(".");
				}
			} else if (modes.getWord() == Settings.Normalize.WORD_NORM_ON) {
				tokens.add("W");
			} else {
				tokens.add(word);
				/*
				 * if (!wordMap.containsKey(word)) { wordMap.put(word, 1);
				 * wordList.add(word); }
				 */
			} /*
				 * else { tokens.add(word.trim()); }
				 */

			break;
		case '"':
			// String doublequote = tokenizer.sval;
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			
			if (modes.getString() == Settings.Normalize.STRING_NORM_ON)
				tokens.add("S");
			else
				tokens.add("\"" + tokenizer.sval + "\"");
			break;
		case '\'':
			// String singlequote = tokenizer.sval;
			if (prevChar.equals("=")) {
				tokens.add("=");
				prevChar = "";
			} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
					|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">")) {
				tokens.add(prevChar);
				prevChar = "";
			}
			if (modes.getString() == Settings.Normalize.STRING_NORM_ON)
				tokens.add("C");
			else
				tokens.add(tokenizer.sval);
			break;
		case StreamTokenizer.TT_EOL:
			// if (newline == Settings.Newline)
			tokens.add("\n");
			break;
		case StreamTokenizer.TT_EOF:
			break;
		default:
			char character = (char) tokenizer.ttype;
			String cStr = String.valueOf(character);
			if (!Character.isWhitespace(character) && character != '\n' && character != '\r') {
				if (cStr.equals("+") || cStr.equals("-") || cStr.equals("*") || cStr.equals("/") || cStr.equals("%")) {
					// nothing found before this
					if (prevChar.equals(""))
						prevChar = cStr;
					else if (prevChar.equals("+")) {
						tokens.add("++");
						prevChar = "";
					} else if (prevChar.equals("-")) {
						tokens.add("--");
						prevChar = "";
					}
				} else if (cStr.equals(">") || cStr.equals("<")) {
					prevChar = cStr;
				} else if (cStr.equals("(") && (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*") || prevChar.equals("/")
						|| prevChar.equals("%") || prevChar.equals("<") || prevChar.equals(">"))) {
					tokens.add(prevChar);
					tokens.add("(");
					prevChar = "";
				} else if (cStr.equals("&") || cStr.equals("|")) {
					if (prevChar.equals("&") || prevChar.equals("|")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else {
						prevChar = cStr;
					}
				} else if (cStr.equals("=")) {
					if (prevChar.equals("=") || prevChar.equals("!")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else if (prevChar.equals(">") || prevChar.equals("<")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else if (prevChar.equals("+") || prevChar.equals("-") || prevChar.equals("*")
							|| prevChar.equals("/") || prevChar.equals("%")) {
						tokens.add(prevChar + cStr);
						prevChar = "";
					} else {
						prevChar = "=";
					}
				} else if (cStr.equals("!")) {
					prevChar = cStr;
				} else if (prevChar.equals("=")) {
					tokens.add("=");
					tokens.add(cStr);
					prevChar = "";
				} else if (cStr.equals(";")) {
					if (prevChar.equals("*")) {
						tokens.add("*");
						prevChar = "";
					}
					tokens.add(";");
				} else if (cStr.equals(",")) {
					tokens.add(",");
					prevChar = ",";
				} else {
					tokens.add(cStr);
					prevChar = "";
				}
			}
			break;
		}
	}

	public ArrayList<String> noNormalizeAToken(String input) throws Exception {
		ArrayList<String> tokens = new ArrayList<String>();
		Scanner fi = new Scanner(input);
		// fi.useDelimiter("\\s*;");

		while (fi.hasNext()) {
			if (fi.hasNextInt()) {
				tokens.add(String.valueOf(fi.nextInt()));
			} else if (fi.hasNextDouble()) {
				tokens.add(String.valueOf(fi.nextDouble()));
			} else {
				String s = fi.next();
				if (s.contains(";")) {
					tokens.add(s.replace(";", ""));
					tokens.add(";");
				} else if (s.contains("++")) {
					tokens.add(s.replace("++", ""));
					tokens.add("++");
				} else if (s.contains("--")) {
					tokens.add(s.replace("--", ""));
					tokens.add("--");
				} else {
					tokens.add(s);
				}
			}
		}
		fi.close();

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