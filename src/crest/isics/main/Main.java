package crest.isics.main;

import crest.isics.helpers.nGramGenerator;
import crest.isics.settings.Settings;
import crest.isics.settings.TokenizerMode;
import org.apache.commons.cli.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	private static String server;
	private static String index;
	private static String type;
	private static String inputFolder;
	private static String normMode;
	private static TokenizerMode modes = new TokenizerMode();
	private static int ngramSize = 4;
	private static boolean isNgram = false;
	private static boolean isPrint = false;
	private static nGramGenerator ngen;
	private static Options options = new Options();
	private static boolean isDFS = true;
	private static String outputFolder = "";
	private static boolean writeToFile = false;
    private static String[] extensions = { "java" };
    private static String command = "index";
    private static int minCloneLine = 10;

	public static void main(String[] args) {

        int resultOffset = 0;
        int resultsSize = 100;
        int querySizeLimit = 100;

        processCommandLine(args);

        Date startDate = getCurrentTime();

        ISiCS isics = new ISiCS(
                server,
                index,
                type,
                inputFolder,
                normMode,
                modes,
                isNgram,
                ngramSize,
                isPrint,
                isDFS,
                outputFolder,
                writeToFile,
                extensions,
                minCloneLine,
                resultOffset,
                resultsSize,
                querySizeLimit,
				Settings.MethodParserType.METHOD);

        isics.execute(command, Settings.RankingFunction.TFIDF);

        Date endDate = getCurrentTime();
        System.out.println("Elapse time (ms): " + (endDate.getTime() - startDate.getTime()));
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
		options.addOption("o", "output", true, "output file location [optional]");
		options.addOption("c", "minline", true, "minimum clone size (lines)");
        options.addOption("z", "command", true, "command to do [index, search]");
		options.addOption("h", "help", false, "print help");

		// check if no parameter given, print help and quit
		if (args.length == 0) {
			showHelp();
			System.exit(0);
		}

		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("h")) { showHelp(); }
			// validate that line count has been set
			if (line.hasOption("s")) { server = line.getOptionValue("s"); }
			else {
				showHelp();
				throw new ParseException("No server name provided.");
			}

			if (line.hasOption("i")) { index = line.getOptionValue("i"); }
			else {
				showHelp();
				throw new ParseException("No index provided.");
			}

			if (line.hasOption("t")) { type = line.getOptionValue("t"); }
			else {
				showHelp();
				throw new ParseException("No type provided.");
			}

			if (line.hasOption("d")) {
				inputFolder = line.getOptionValue("d");
			} else {
				showHelp();
				throw new ParseException("No input folder provided.");
			}

			if (line.hasOption("l")) {
				normMode = line.getOptionValue("l").toLowerCase();
				char[] normOptions = line.getOptionValue("l").toLowerCase().toCharArray();
				TokenizerMode tknzMode = new TokenizerMode();
				modes = tknzMode.setTokenizerMode(normOptions);
			}

			if (line.hasOption("n")) { isNgram = true; }

			if (line.hasOption("g")) { ngramSize = Integer.valueOf(line.getOptionValue("g")); }

			if (line.hasOption("o")) {
				writeToFile = true;
				outputFolder = line.getOptionValue("o");
			}

			if (line.hasOption("c")) {
				command = line.getOptionValue("c");
			} else {
				showHelp();
				throw new ParseException("Wrong command.");
			}

			if (line.hasOption("p")) { isPrint = true; }

			if (line.hasOption("f")) { isDFS = true; }

            if (line.hasOption("z")) { minCloneLine = Integer.valueOf(line.getOptionValue("z")); }

		} catch (ParseException exp) {
			System.out.println("Warning: " + exp.getMessage());
		}
	}

	private static void showHelp() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("(v 0.3) $java -jar isics.jar", options);
		System.exit(0);
	}

	private static Date getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        return date;
	}

}
