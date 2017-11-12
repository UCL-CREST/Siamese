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

	private static Options options = new Options();
	private static String configFile;

	public static void main(String[] args) {

        processCommandLine(args);

        if (configFile == null) {
            System.out.println("Couldn't find the config file. Use the default one at ./config.properties");
            configFile = "config.properties";
        }

        Date startDate = getCurrentTime();

        ISiCS isics = new ISiCS(configFile);
        isics.startup();

        try {
			isics.execute();
		} catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Elasticsearch is not running. Please execute the following command:\n" +
					"./elasticsearch-2.2.0/bin/elasticsearch -d");
		}

		isics.shutdown();
        Date endDate = getCurrentTime();
        System.out.println("Elapse time (ms): " + (endDate.getTime() - startDate.getTime()));
    }

	private static void processCommandLine(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
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
            }
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
