package elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Main {
	private static ESConnector es;
	private static String server;
	private static String index;
	private static String type;
	private static String inputFolder;
	private static TokenizerMode modes = new TokenizerMode();
	private static int ngramSize = 4;
	private static boolean isNgram = false;
	private static boolean isPrint = false;
	private static nGramGenerator ngen;
	private static Options options = new Options();
	private static boolean isDFS = false;

	public static void main(String[] args) {
		processCommandLine(args);
		// create a connector
		es = new ESConnector(server);
		// initialise the ngram generator
		ngen = new nGramGenerator(ngramSize);

		try {
			es.startup();
			boolean status = insert(inputFolder, Settings.IndexingMode.SEQUENTIAL);
			if (status) {
				// if ok, search
				search();
			} else {
				System.out.println("Indexing error: please check!");
			}
			es.shutdown();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static boolean insert(String inputFolder, int indexMode) throws Exception {
		boolean indexResult = true;
		ArrayList<Document> docArray = new ArrayList<Document>();
		File folder = new File(inputFolder);
		
		List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : listOfFiles) {
			// Create Document object and put in an array list
			String src = tokenize(file);
			// System.out.println(file.getName() + ":" + src);
			// Use file name as id
			Document d = new Document(file.getName(), src);
			// add document to array
			docArray.add(d);
		}

		// doing indexing (can choose between bulk/sequential)
		if (indexMode == Settings.IndexingMode.BULK)
			es.bulkInsert(index, type, docArray);
		else if (indexMode == Settings.IndexingMode.SEQUENTIAL)
			es.sequentialInsert(index, type, docArray);
		else
			// wrong mode
			return false;
		
		return indexResult;
	}
	
	private static String tokenize(File file) throws Exception {
		String src = "";
		JavaTokenizer tokenizer = new JavaTokenizer(modes);

		if (modes.getEscape() == Settings.Normalize.ESCAPE_ON) {
			try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
				String line;
				while ((line = br.readLine()) != null) {
					ArrayList<String> tokens = tokenizer.noNormalizeAToken(escapeString(line).trim());
					src += printArray(tokens, false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// generate tokens
			ArrayList<String> tokens = tokenizer.getTokensFromFile(file.getAbsolutePath());
			src = printArray(tokens, false);
			// enter ngram mode
			if (isNgram) {
				src = printArray(ngen.generateNGramsFromJavaTokens(tokens), false);
			}
		}
		return src;
	}
	
	@SuppressWarnings("unchecked")
	private static void search() throws Exception {
		int total = 0;
		File folder = new File(inputFolder);
		List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		for (File file : listOfFiles) {
			String query = tokenize(file);
			
			if (isPrint) {
				System.out.println(file.getName());
				System.out.println(query);
			}
			int tp = findTP(es.search(index, type, query, isPrint, isDFS), file.getName().split("\\$")[0]);
			// System.out.println(round((tp * 0.1), 2));
			total += round((tp * 0.1), 2);
		}
		
		System.out.println(total/listOfFiles.size());
	}

	private static int findTP(ArrayList<String> results, String query) {
		int tp = 0;
		for (int i = 0; i < results.size(); i++) {
			// System.out.println(results.get(i));
			if (results.get(i).contains(query)) {
				tp++;
			}
		}
		return tp;
	}
	
	/***
	 * Copied from: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	public static String printArray(ArrayList<String> arr, boolean pretty) {
		String s = "";
		for (int i = 0; i < arr.size(); i++) {
			if (pretty && arr.get(i).equals("\n")) {
				System.out.print(arr.get(i));
				continue;
			}
			s += arr.get(i) + " ";
		}
		return s;
	}

	private static String escapeString(String input) {
		String output = "";
		output += input.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/").replace("\b", "\\b")
				.replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
		return output;
	}

	private static void processCommandLine(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		options.addOption("s", "server", true, "elasticsearch's server name (or IP)");
		options.addOption("i", "index", true, "index name");
		options.addOption("t", "type", true, "type name");
		options.addOption("d", "dir", true, "input folder of source files to search");
		options.addOption("l", "level", true, "normalisation. It can be a combination of x (none), w (words), d (datatypes), "
				+ "j (Java classes), p (Java packages), k (keywords), v (values), s (strings), e (escape). For example: wkvs");
		options.addOption("n", "ngram", false, "convert tokens into ngram [default=no]");
		options.addOption("g", "size", true, "size of n in ngram [default = 4]");
		options.addOption("p", "print", false, "print the generated tokens");
		options.addOption("f", "dfs", false, "use DFS mode [default=no]");
		options.addOption("h", "help", false, "print help");

		// check if no parameter given, print help and quit
		if (args.length == 0) {
			showHelp();
			System.exit(0);
		}

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("h")) {
				showHelp();
			}
			// validate that line count has been set
			if (line.hasOption("s")) {
				server = line.getOptionValue("s");
			} else {
				throw new ParseException("No server name provided.");
			}

			if (line.hasOption("i")) {
				index = line.getOptionValue("i");
			} else {
				throw new ParseException("No index provided.");
			}

			if (line.hasOption("t")) {
				type = line.getOptionValue("t");
			} else {
				throw new ParseException("No type provided.");
			}

			if (line.hasOption("d")) {
				inputFolder = line.getOptionValue("d");
			} else {
				throw new ParseException("No input folder provided.");
			}

			if (line.hasOption("l")) {
				char[] normOptions = line.getOptionValue("l").toLowerCase().toCharArray();
				for (char c : normOptions) {
					// setting all normalisation options: w, d, j, p, k, v, s
					if (c == 'w')
						modes.setWord(Settings.Normalize.WORD_NORM_ON);
					else if (c == 'd')
						modes.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
					else if (c == 'j')
						modes.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
					else if (c == 'p')
						modes.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
					else if (c == 'k')
						modes.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
					else if (c == 'v')
						modes.setValue(Settings.Normalize.VALUE_NORM_ON);
					else if (c == 's')
						modes.setString(Settings.Normalize.STRING_NORM_ON);
					else if (c == 'x') {
						modes.setWord(Settings.Normalize.WORD_NORM_OFF);
						modes.setDatatype(Settings.Normalize.DATATYPE_NORM_OFF);
						modes.setJavaClass(Settings.Normalize.JAVACLASS_NORM_OFF);
						modes.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_OFF);
						modes.setKeyword(Settings.Normalize.KEYWORD_NORM_OFF);
						modes.setValue(Settings.Normalize.VALUE_NORM_OFF);
						modes.setValue(Settings.Normalize.STRING_NORM_OFF);
					} else if (c == 'e') {
						modes.setEscape(Settings.Normalize.ESCAPE_ON);
					}
				}
			}

			if (line.hasOption("n")) {
				isNgram = true;
			}

			if (line.hasOption("g")) {
				ngramSize = Integer.valueOf(line.getOptionValue("g"));
			}

			if (line.hasOption("p")) {
				isPrint = true;
			}

			if (line.hasOption("f")) {
				isDFS = true;
			}

		} catch (ParseException exp) {
			System.out.println("Warning: " + exp.getMessage());
		}
	}

	private static void showHelp() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("(v 0.2) java -jar checker.jar", options);
		System.exit(0);
	}
}
