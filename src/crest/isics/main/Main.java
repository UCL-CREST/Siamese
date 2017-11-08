package crest.isics.main;

import crest.isics.helpers.nGramGenerator;
import crest.isics.settings.Settings;
import crest.isics.settings.TokenizerMode;
import org.apache.commons.cli.*;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Main {

//	private static String server;
//	private static String index;
//	private static String type;
//	private static String inputFolder;
//	private static String normMode;
//	private static TokenizerMode modes = new TokenizerMode();
//	private static int ngramSize;
//	private static boolean isNgram;
//	private static boolean isPrint;
//	private static nGramGenerator ngen;
	private static Options options = new Options();
	private static String configFile;
//	private static boolean isDFS;
//	private static String outputFolder;
//	private static boolean writeToFile;
//    private static String extension;
//    private static String command;
//    private static int minCloneLine;

	public static void main(String[] args) {

//        int resultOffset = 0;
//        int resultsSize = 100;
//        int querySizeLimit = 100;
        processCommandLine(args);

        Date startDate = getCurrentTime();

        ISiCS isics = new ISiCS(configFile);

        try {
			isics.execute();
		} catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Elasticsearch is not running. Please execute the following command:\n" +
					"./elasticsearch-2.2.0/bin/elasticsearch -d");
		}

        Date endDate = getCurrentTime();
        System.out.println("Elapse time (ms): " + (endDate.getTime() - startDate.getTime()));
    }

	private static void processCommandLine(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
//
//		options.addOption("s", "server", true, "elasticsearch's server name (or IP)");
//		options.addOption("i", "index", true, "index name");
//		options.addOption("t", "type", true, "type name");
//		options.addOption("d", "dir", true, "input folder of source files to search");
//		options.addOption("l", "level", true, "normalisation. It can be a combination of x (none), w (words), d (datatypes), "
//				+ "j (Java classes), p (Java packages), k (keywords), v (values), s (strings), e (escape). For example: wkvs");
//		options.addOption("n", "ngram", false, "convert tokens into ngram [default=no]");
//		options.addOption("g", "size", true, "size of n in ngram [default = 4]");
//		options.addOption("p", "print", false, "print the generated tokens");
//		options.addOption("f", "dfs", false, "use DFS mode [default=no]");
//		options.addOption("o", "output", true, "output file location [optional]");
//		options.addOption("z", "minline", true, "minimum clone size (lines)");
//        options.addOption("c", "command", true, "command to do [index, search]");
		options.addOption("cf", "configFile", true, "read from a configuration file");
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

            if (line.hasOption("cf")) {
               configFile = line.getOptionValue("cf");
//            } else {
//                // validate that line count has been set
//                if (line.hasOption("s")) {
//                    server = line.getOptionValue("s");
//                } else {
//                    showHelp();
//                    throw new ParseException("No server name provided.");
//                }
//
//                if (line.hasOption("i")) {
//                    index = line.getOptionValue("i");
//                } else {
//                    showHelp();
//                    throw new ParseException("No index provided.");
//                }
//
//                if (line.hasOption("t")) {
//                    type = line.getOptionValue("t");
//                } else {
//                    showHelp();
//                    throw new ParseException("No type provided.");
//                }
//
//                if (line.hasOption("d")) {
//                    inputFolder = line.getOptionValue("d");
//                } else {
//                    showHelp();
//                    throw new ParseException("No input folder provided.");
//                }
//
//                if (line.hasOption("l")) {
//                    normMode = line.getOptionValue("l").toLowerCase();
//                    char[] normOptions = line.getOptionValue("l").toLowerCase().toCharArray();
//                    TokenizerMode tknzMode = new TokenizerMode();
//                    modes = tknzMode.setTokenizerMode(normOptions);
//                }
//
//                if (line.hasOption("n")) {
//                    isNgram = true;
//                }
//
//                if (line.hasOption("g")) {
//                    ngramSize = Integer.valueOf(line.getOptionValue("g"));
//                }
//
//                if (line.hasOption("o")) {
//                    writeToFile = true;
//                    outputFolder = line.getOptionValue("o");
//                }
//
//                if (line.hasOption("c")) {
//                    command = line.getOptionValue("c");
//                } else {
//                    showHelp();
//                    throw new ParseException("Wrong command.");
//                }
//
//                if (line.hasOption("p")) {
//                    isPrint = true;
//                }
//
//                if (line.hasOption("f")) {
//                    isDFS = true;
//                }
//
//                if (line.hasOption("z")) {
//                    minCloneLine = Integer.valueOf(line.getOptionValue("z"));
//                }
            }
		} catch (ParseException exp) {
			System.out.println("Warning: " + exp.getMessage());
		}
	}

//	private static void printConfig() {
//        System.out.println("==== Configurations ====");
//        System.out.println("server=" + server);
//        System.out.println("index=" + index);
//        System.out.println("type=" + type);
//        System.out.println("inputFolder=" + inputFolder);
//        System.out.println("outputFolder=" + outputFolder);
//        System.out.println("normalization=" + normMode);
//        System.out.println("ngramSize=" + ngramSize);
//        System.out.println("verbose=" + isPrint);
//        System.out.println("dfs=" + isDFS);
//        System.out.println("writeToFile=" + writeToFile);
//        System.out.println("extension=" + extension);
//        System.out.println("minCloneSize=" + minCloneLine);
//        System.out.println("command=" + command);
//        System.out.println("=========================");
//    }
//
//	private static void readFromConfigFile(String configFile) {
//	    /* copied from
//	    https://www.mkyong.com/java/java-properties-file-examples/
//	     */
//        Properties prop = new Properties();
//        InputStream input = null;
//
//        try {
//
//            input = new FileInputStream("config.properties");
//            // load a properties file
//            prop.load(input);
//
//            // get the property value and print it out
//            server = prop.getProperty("server");
//            index = prop.getProperty("index");
//            type = prop.getProperty("type");
//            inputFolder = prop.getProperty("inputFolder");
//            outputFolder = prop.getProperty("outputFolder");
//            normMode = prop.getProperty("normMode");
//            ngramSize = Integer.parseInt(prop.getProperty("ngramSize"));
//            isPrint = Boolean.parseBoolean(prop.getProperty("isPrint"));
//            isDFS = Boolean.parseBoolean(prop.getProperty("dfs"));
//            writeToFile = Boolean.parseBoolean(prop.getProperty("writeToFile"));
//            extension = prop.getProperty("extension");
//            minCloneLine=Integer.parseInt(prop.getProperty("minCloneSize"));
//            command = prop.getProperty("command");
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

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
