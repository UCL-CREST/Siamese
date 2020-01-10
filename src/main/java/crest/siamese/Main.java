/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese;

import crest.siamese.helpers.MyUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import java.util.Date;

public class Main {

	private static Options options = new Options();
	private static String configFile;

	public static void main(String[] args) {
        String[] overridingParams = processCommandLine(args);
        if (configFile == null) {
            System.out.println("Couldn't find the config file. Use the default one at ./config.properties");
            configFile = "config.properties";
        }
        Date startDate = MyUtils.getCurrentTime();
        Siamese siamese = new Siamese(configFile, overridingParams);
        siamese.startup();
        try {
			siamese.execute();
		} catch(NoNodeAvailableException nne) {
			System.out.println("Elasticsearch is not running. Please execute the following command:\n" +
					"./elasticsearch-2.2.0/bin/elasticsearch -d");
		} catch (Exception e) {
            System.out.println(e.getMessage());
		}

		siamese.shutdown();
        Date endDate = MyUtils.getCurrentTime();
        System.out.println("Elapse time (ms): " + (endDate.getTime() - startDate.getTime()));
    }

	/**
	 * Parse the command line argument(s). Mainly to get the name of the configuration file.
	 * Moroever, the user can override the parameters in the config files by giving them as command line parameters
	 * @param args the command line arguments
	 * @return an 3-dim array of overriding parameters [input, output, command]
	 */
	private static String[] processCommandLine(String[] args) {
		String[] overridingParams = {"", "", ""};
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
		Option configOption = new Option("cf", "configFile", true,
				"[* requried *] a configuration file");
		Option iOption = new Option("i", "inputFolder", true,
				"[optional] location of the input files (for index or query). " +
						"This will override the configuration file.");
		Option oOption = new Option("o", "outputFolder", true,
				"[optional] location of the search result file. " +
						"This will override the configuration file.");
		Option cOption = new Option("c", "command", true,
				"[optional] command to execute [index, search]. This will override the configuration file.");
		Option hOption = new Option("h", "help", false, "<optional> print help");
		configOption.setRequired(true);
		iOption.setRequired(false);
		oOption.setRequired(false);
		cOption.setRequired(false);
		options.addOption(configOption);
		options.addOption(iOption);
		options.addOption(oOption);
		options.addOption(cOption);
		options.addOption(hOption);
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
			if (line.hasOption("i")) {
				overridingParams[0] = line.getOptionValue("i");
			}
			if (line.hasOption("o")) {
				overridingParams[1] = line.getOptionValue("o");
			}
			if (line.hasOption("c")) {
				overridingParams[2] = line.getOptionValue("c");
			}
		} catch (ParseException exp) {
			showHelp();
			System.exit(0);
		}
		return overridingParams;
	}

	private static void showHelp() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("(v 0.6) $java -jar siamese.jar -cf <config file> [-i input] [-o output] [-c command] [-h help]\n" +
				"Example: java -jar siamese.jar -cf config.properties\n" +
				"Example: java -jar siamese.jar -cf config.properties -i /my/input/dir -o /my/output/dir -c index", options);
		System.exit(0);
	}
}
