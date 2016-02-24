package elasticsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Checker {
	private static ESConnector es;
	private static String server;
	private static String index;
	private static String type;
	private static String inputFolder;
	private static int mode = Settings.Normalize.HI_NORM;
	private static int ngramSize = 4;
	private static boolean isNgram = false;
	private static boolean isPrint = false;
	private static nGramGenerator ngen;
	private static Options options = new Options();

	public static void main(String[] args) {
		processCommandLine(args);
		// create a connector
		es = new ESConnector(server);
		// initialise the ngram generator
		ngen = new nGramGenerator(ngramSize);
		
		System.out.println("Checking " + server + ":9200/" + index + "/" + type + ", norm level: " + mode
				+ ", ngram = " + isNgram);
		try {
			es.startup();
			search();
			es.shutdown();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private static void search() throws Exception {
		File folder = new File(inputFolder);
		File[] listOfFiles = folder.listFiles();
		String query = "";
		for (int i = 0; i < listOfFiles.length; i++) {
			JavaTokenizer tokenizer = new JavaTokenizer(mode);

			if (mode == Settings.Normalize.ESCAPE) {
				try (BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsolutePath()))) {
					String line;
					while ((line = br.readLine()) != null) {
						ArrayList<String> tokens = tokenizer.noNormalizeAToken(escapeString(line).trim());
						query = printArray(tokens, false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// generate tokens
				ArrayList<String> tokens = tokenizer.getTokensFromFile(listOfFiles[i].getAbsolutePath());
				query = printArray(tokens, false);
				// enter ngram mode
				if (isNgram) {
					query = printArray(ngen.generateNGramsFromJavaTokens(tokens), false);
				}
			}
			if (isPrint) {
				System.out.println(listOfFiles[i].getName());
				System.out.println(query);
			}
			System.out.print(listOfFiles[i].getName() + ",");
			System.out.println(findTP(es.search(index, type, query), listOfFiles[i].getName().split("\\$")[0]));
		}
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
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
		if (args.length == 0) {
			showHelp();
			return ;
		}
		// create the command line parser
		CommandLineParser parser = new BasicParser();

		options.addOption("s", "server", true, "elasticsearch's server name (or IP)");
		options.addOption("i", "index", true, "index name");
		options.addOption("t", "type", true, "type name");
		options.addOption("d", "dir", true, "input folder of source files to search");
		options.addOption("l", "level", true, "normalisation level (hi [default]/lo)");
		options.addOption("n", "ngram", false, "convert tokens into ngram [default=no]");
		options.addOption("g", "size", true, "size of n in ngram [default = 4]");
		options.addOption("p", "print", false, "print the generated tokens");
		options.addOption("h", "help", false, "print help");

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
				if (line.getOptionValue("l").toLowerCase().equals("lo"))
					mode = Settings.Normalize.LO_NORM;
				else if (line.getOptionValue("l").toLowerCase().equals("esc"))
					mode = Settings.Normalize.ESCAPE;
				else
					mode = Settings.Normalize.HI_NORM;
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

		} catch (ParseException exp) {
			System.out.println("Warning: " + exp.getMessage());
		}
	}

	private static void showHelp() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("java -jar checker.jar", options);
		System.exit(0);
	}
}
