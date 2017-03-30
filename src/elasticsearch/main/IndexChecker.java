package elasticsearch.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import elasticsearch.document.Document;
import elasticsearch.settings.IndexSettings;
import elasticsearch.settings.Settings;
import elasticsearch.document.Method;

import elasticsearch.settings.TokenizerMode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;


public class IndexChecker {
	private static ESConnector es;
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

	public static void main(String[] args) {
		processCommandLine(args);
		// create a connector
		es = new ESConnector(server);
		// initialise the n-gram generator
		ngen = new nGramGenerator(ngramSize);
        String indexSettings = IndexSettings.DFR.getIndexSettings(IndexSettings.DFR.bmIF, IndexSettings.DFR.aeL, IndexSettings.DFR.normH1);
        String mappingStr = IndexSettings.DFR.mappingStr;

		try {
			es.startup();
            if (command.toLowerCase().equals("index")) {
                createIndex(indexSettings, mappingStr);
                boolean status = insert(inputFolder, Settings.IndexingMode.SEQUENTIAL);
                if (status) {
                    // if ok, refresh the index, then search
                    es.refresh();
                    System.out.println("Successfully creating index.");
                } else {
                    System.out.println("Indexing error: please check!");
                }
            } else if (command.toLowerCase().equals("search")) {
                search(inputFolder);
            }
			es.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private static boolean createIndex(String indexSettings, String mappingStr) {
        // String index = indexName + "_" + normMode + "_" + ngramSize;
        if (isPrint) System.out.println("INDEX," + index);

        // delete the index if it exists
        if (es.isIndexExist(index)) {
            es.deleteIndex(index);
        }
        // create index
        boolean isCreated = es.createIndex(index, type, indexSettings, mappingStr);
        // System.err.println("Created: " + index);
        if (!isCreated) {
            System.err.println("Cannot create index: " + index);
            // System.exit(-1);
        }
        return isCreated;
    }
	
	String runExperiment(String hostname, String indexName, String typeName, String inputDir
            , String[] normModes, int[] ngramSizes, boolean useNgram
            , boolean useDFS, String outputDir, boolean writeToOutputFile, String indexSettings
            , String mappingStr, boolean printLog, boolean isDeleteIndex) {
		server = hostname;
		type = typeName;
		inputFolder = inputDir;
		isNgram = useNgram;
		isDFS = useDFS;
		outputFolder = outputDir;
		writeToFile = writeToOutputFile;
		isPrint = printLog;
		// create a connector
		es = new ESConnector(server);

        String outputFile = "";
		try {
			es.startup();
			for (String normMode : normModes) {
				// reset the modes before setting it again
				modes.reset();
				// set the normalisation + tokenization mode
				setTokenizerMode(normMode.toLowerCase().toCharArray());
				for (int ngramSize : ngramSizes) {
					index = indexName + "_" + normMode + "_" + ngramSize;
					if (isPrint) System.out.println("INDEX," + index);
					
					// delete the index if it exists
					if (es.isIndexExist(index)) {
						es.deleteIndex(index);
					}
					// create index
					if (!es.createIndex(index, type, indexSettings, mappingStr)) {
						System.err.println("Cannot create index: " + index);
						System.exit(-1);
					}
					// initialise the ngram generator
					ngen = new nGramGenerator(ngramSize);
					boolean status = insert(inputFolder, Settings.IndexingMode.SEQUENTIAL);
					if (status) {
						// if ok, refresh the index, then search
						es.refresh();
						outputFile = search(inputFolder);
					} else {
						System.out.println("Indexing error: please check!");
					}
					// delete index
					if (isDeleteIndex) {
                        if (!es.deleteIndex(index)) {
                            System.err.println("Cannot delete index: " + index);
                            System.exit(-1);
                        }
                    }

					Experiment.evaluate(outputFile, index, outputDir);
				}
			}
			es.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputFile;
	}
	
	@SuppressWarnings("unchecked")
	private static boolean insert(String inputFolder, int indexMode) throws Exception {
		boolean isIndexed = true;
		ArrayList<Document> docArray = new ArrayList<>();
		File folder = new File(inputFolder);
		
		List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);

        // counter for id
        int count = 0;

		for (File file : listOfFiles) {
		    String filePath = file.getAbsolutePath().replace(Experiment.prefixToRemove, "");
			if (isPrint)
				System.out.println(count + ": " + filePath);
            // parse each file into method (if possible)
			MethodParser methodParser = new MethodParser(file.getAbsolutePath(), Experiment.prefixToRemove);
            ArrayList<Method> methodList;

            try {
                methodList = methodParser.parseMethods();
                // System.out.println("No. of methods: " + methodList.size());
                // check if there's a method
                if (methodList.size() > 0) {
                    for (Method method : methodList) {
                        // Create Document object and put in an array list
                        String src = tokenize(method.getSrc());
                        // Use file name as id
                        Document d = new Document(String.valueOf(count),
								filePath + "_" + method.getName(),
                                src);

                        // add document to array
                        docArray.add(d);
                        count++;
                    }
                } else {
                    // cannot parse, use the whole file
                    String src = tokenize(file);
                    // Use file name as id
                    Document d = new Document(String.valueOf(count),
                            filePath + "_raw",
                            src);
                    // add document to array
                    docArray.add(d);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (indexMode == Settings.IndexingMode.SEQUENTIAL) {
                try {
                    isIndexed = es.sequentialInsert(index, type, docArray);
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                    System.exit(0);
                }

                // something wrong with indexing, return false
                if (!isIndexed)
                    throw new Exception("Cannot insert docId " + count + " in sequential mode");
                else {
                    // System.out.println("Successfully indexed document(s).");
                    // reset the array list
                     docArray.clear();
                }
            } // index every 100 docs
            // doing indexing (can choose between bulk/sequential)
            else if (indexMode == Settings.IndexingMode.BULK) {
                if (docArray.size() >= Settings.BULK_SIZE) {
                    isIndexed = es.bulkInsert(index, type, docArray);

                    if (!isIndexed)
                        throw new Exception("Cannot bulk insert documents");
                    else {
                        // reset the array list
                        docArray.clear();
                    }
                }
            }
		}

		// the last batch
        if (indexMode == Settings.IndexingMode.BULK && docArray.size() != 0) {
            isIndexed = es.bulkInsert(index, type, docArray);

            if (!isIndexed)
                throw new Exception("Cannot bulk insert documents");
            else {
                // reset the array list
                docArray.clear();
            }
        }

        // successfully indexed, return true
        System.out.println("Successfully indexed documents.");

		return true;
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

    private static String tokenize(String sourcecode) throws Exception {
        String src;
        JavaTokenizer tokenizer = new JavaTokenizer(modes);

        // generate tokens
        ArrayList<String> tokens = tokenizer.getTokensFromString(sourcecode);
        src = printArray(tokens, false);
        // enter ngram mode
        if (isNgram) {
            src = printArray(ngen.generateNGramsFromJavaTokens(tokens), false);
        }
        return src;
    }
	
	@SuppressWarnings("unchecked")
	private static String search(String inputFolder) throws Exception {
        double total = 0.0;
        String outToFile = "";
        int totalMethods = 0;

        // if (writeToFile) {
        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss");
        Date dateobj = new Date();
        File outfile = new File(outputFolder + "/" + index + "_" + df.format(dateobj) + ".csv");

        // if file doesn't exists, then create it
        boolean isCreated = false;
        if (!outfile.exists()) {
            isCreated = outfile.createNewFile();
        }

        if (isCreated) {
            FileWriter fw = new FileWriter(outfile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            File folder = new File(inputFolder);
            List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);

            int count = 0;

            for (File file : listOfFiles) {
            	if (isPrint)
                	System.out.println("File: " + file.getAbsolutePath());
                // reset the output buffer
                outToFile = "";
                // parse each file into method (if possible)
                MethodParser methodParser = new MethodParser(file.getAbsolutePath(), Experiment.prefixToRemove);
                ArrayList<Method> methodList;
                String query = "";

                try {
                    methodList = methodParser.parseMethods();
                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            // write output to file
                            outToFile += method.getFile().replace(Experiment.prefixToRemove, "") + "_"
                                    + method.getName() + "," ;
                            // count the number of methods
                            totalMethods += methodList.size();
                            query = tokenize(method.getSrc());
                            // search for results
                            ArrayList<Document> results = es.search(index, type, query, isPrint, isDFS);
                            int resultCount = 0;
                            for (Document d : results) {
                                if (resultCount>0)
                                    outToFile += ","; // add comma in between

                                outToFile += d.getFile();
                                resultCount++;
                            }
                            outToFile += "\n";
                        }
                    } else {
                        query = tokenize(file);
                        // search for results
                        ArrayList<Document> results = es.search(index, type, query, isPrint, isDFS);
                        outToFile += file.getAbsolutePath().replace(Experiment.prefixToRemove, "") +
                                "_noMethod" +
                                ",";
                        int resultCount = 0;
                        for (Document d : results) {
                            if (resultCount>0)
                                outToFile += ","; // add comma in between

                            outToFile += d.getFile();
                            resultCount++;
                        }
                        outToFile += "\n";
                    }
                    count++;
                //} catch(RemoteTransportException rmtexp) {
                } catch (Exception e) {
                    System.out.println(e.getCause());
                    System.out.println("Error: query term size exceeds 4096 (too big).");
                    // e.printStackTrace();
                    // System.exit(0);
                }
                bw.write(outToFile);
            }
            bw.close();


            System.out.println("Searching done. See output at " + outfile.getAbsolutePath());

        } else {
            throw new IOException("Cannot create the output file: " + outfile.getAbsolutePath());
        }

        return outfile.getAbsolutePath();
    }

	private static int findTP(ArrayList<String> results, String query) {
		int tp = 0;
        for (String result : results) {
            // System.out.println(results.get(i));
            if (result.contains(query)) {
                tp++;
            }
        }
		return tp;
	}
	
	/***
	 * Copied from: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 */
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	private static String printArray(ArrayList<String> arr, boolean pretty) {
		String s = "";
        for (String anArr : arr) {
            if (pretty && anArr.equals("\n")) {
                System.out.print(anArr);
                continue;
            }
            s += anArr + " ";
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
		options.addOption("o", "output", true, "output file location [optional]");
        options.addOption("c", "command", true, "command to do [index, search]");
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
				setTokenizerMode(normOptions);
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

		} catch (ParseException exp) {
			System.out.println("Warning: " + exp.getMessage());
		}
	}
	
	private static void setTokenizerMode(char[] normOptions) {
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

	private static void showHelp() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("(v 0.2) java -jar checker.jar", options);
		System.exit(0);
	}

    /***
     * Read idf of each term in the query directly from Lucene index
     * @param indexName name of the Elasticsearch (Lucene) index
     * @param terms query containing search terms
     * @param selectedSize size of the selected terms
     * @return selected top-selectedSize terms
     */
	private static String getSelectedTerms(String indexName, String terms, int selectedSize) {
		String[] termsArr = terms.split(" ");
		ArrayList<SelectedTerm> selectedTermIndexes = new ArrayList<>();
		String selectedTerms = "";
		// directory where your index is stored
		String indexFile =  "/Users/Chaiyong/elasticsearch-2.2.0/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";

		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
			IndexSearcher searcher = new IndexSearcher(reader);

			for (int i=0; i<termsArr.length; i++) {
			    String term = termsArr[i];
				Term t = new Term("src", term);
				int freq = reader.docFreq(t);
				// System.out.println(term + ": " + freq);
				// first term, just insert
				if (selectedTermIndexes.size() == 0) {
                    selectedTermIndexes.add(new SelectedTerm(termsArr[i], freq));
                }
                else{
				    // if not the first one, add it according to its frequency (ascending)
                    for (int j=0; i<selectedTermIndexes.size(); j++) {
                        SelectedTerm sti = selectedTermIndexes.get(j);
                        if (freq < sti.getFrequency()) {
                            SelectedTerm newSti = new SelectedTerm(termsArr[i], freq);
                            selectedTermIndexes.add(j, newSti);
                        }
                    }
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		selectedTerms = selectedTermIndexes.get(0).getTerm();
		for (int i=1; i<selectedSize; i++) {
		    selectedTerms += " " + selectedTermIndexes.get(i).getTerm();
        }
		/* TODO: have to remove */
        // System.out.println("Selected terms: " + selectedTerms);
		// wait for keyboard
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectedTerms;
	}

	public static class SelectedTerm {
	    private String term;
	    private int frequency;

	    public SelectedTerm(String term, int frequency) {
	        this.term = term;
	        this.frequency = frequency;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}
