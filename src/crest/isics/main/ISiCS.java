package crest.isics.main;

import crest.isics.document.JavaTerm;
import crest.isics.helpers.*;
import crest.isics.settings.Settings;
import crest.isics.settings.TokenizerMode;
import crest.isics.document.Document;
import crest.isics.document.Method;
import crest.isics.settings.IndexSettings;
import org.elasticsearch.client.Client;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ISiCS {

    private ESConnector es;
    private String server;
    private String index;
    private String type;
    private String inputFolder;
    private String normMode;
    private TokenizerMode modes = new TokenizerMode();
    private int ngramSize = 4;
    private boolean isNgram = false;
    private boolean isPrint = false;
    private nGramGenerator ngen;
    private Options options = new Options();
    private boolean isDFS = true;
    private String outputFolder = "";
    private boolean writeToFile = false;
    private String[] extensions = { "java" };
    private int minCloneLine = 10; // default min clone line
    private int resultOffset = 0; // default result offset
    private int resultsSize = 10; // default result size
    private int totalDocuments = 100; // default number of documents (from CloPlag file-level)
    private int querySizeLimit = 100;  // default query size limit
    private boolean recreateIndexIfExists = true;
    private String methodParserMode = Settings.MethodParserType.FILE;
    private String cloneClusterFile = "resources/clone_clusters_" + this.methodParserMode + ".csv";
    private int printEvery = 10000;

    public ISiCS() {

    }

    public ISiCS(
            String server,
            String index,
            String type,
            String inputFolder,
            String normMode,
            TokenizerMode modes,
            boolean isNgram,
            int ngramSize,
            boolean isPrint,
            boolean isDFS,
            String outputFolder,
            boolean writeToFile,
            String[] extensions,
            int minCloneLine,
            int resultOffset,
            int resultsSize,
            int querySizeLimit,
            String methodParserMode) {
        // setup all parameter values
        this.server = server;
        this.index = index;
        this.type = type;
        this.inputFolder = inputFolder;
        this.normMode = normMode;
        this.modes = modes;
        this.isNgram = isNgram;
        this.ngramSize = ngramSize;
        this.isPrint = isPrint;
        this.isDFS = isDFS;
        this.outputFolder = outputFolder;
        this.writeToFile = writeToFile;
        this.extensions = extensions;
        this.minCloneLine = minCloneLine;
        this.resultOffset = resultOffset;
        this.resultsSize = resultsSize;
        this.querySizeLimit = querySizeLimit; // 0 means no limit
        this.methodParserMode = methodParserMode;
    }

    public void execute(String command, int rankingFunc) {

        // create a connector
        es = new ESConnector(server);

        // initialise the n-gram generator
        ngen = new nGramGenerator(ngramSize);

        // default similarity function is TFIDF
        String indexSettings = IndexSettings.TFIDF.getIndexSettings(IndexSettings.TFIDF.DisCountOverlap.NO);
        String mappingStr = IndexSettings.TFIDF.mappingStr;

        if (rankingFunc == Settings.RankingFunction.TFIDF) {
            indexSettings = IndexSettings.TFIDF.getIndexSettings(IndexSettings.TFIDF.DisCountOverlap.NO);
            mappingStr = IndexSettings.TFIDF.mappingStr;
        } else if (rankingFunc == Settings.RankingFunction.BM25) {
            indexSettings = IndexSettings.BM25.getDefaultIndexSettings();
            mappingStr = IndexSettings.BM25.mappingStr;
        } else if (rankingFunc == Settings.RankingFunction.DFR) {
             indexSettings = IndexSettings.DFR.getIndexSettings(
                    IndexSettings.DFR.bmIF,
                    IndexSettings.DFR.aeL,
                    IndexSettings.DFR.normH1);
            mappingStr = IndexSettings.DFR.mappingStr;
        } else if (rankingFunc == Settings.RankingFunction.IB) {
            indexSettings = IndexSettings.IB.getIndexSettings(
                    IndexSettings.IB.distributionsLL,
                    IndexSettings.IB.lambdasDF,
                    IndexSettings.IB.normH1
            );
            mappingStr = IndexSettings.IB.mappingStr;
        } else if (rankingFunc == Settings.RankingFunction.LMD) {
            indexSettings = IndexSettings.LMD.getIndexSettings("2000");
            mappingStr = IndexSettings.LMD.mappingStr;
        } else if (rankingFunc == Settings.RankingFunction.LMJ) {
            indexSettings = IndexSettings.LMJ.getIndexSettings("0.1");
            mappingStr = IndexSettings.LMJ.mappingStr;
        }

        try {
            Client isicsClient = es.startup();
            if (isicsClient != null) {
                if (command.toLowerCase().equals("index")) {

                    if (recreateIndexIfExists) {
                        createIndex(indexSettings, mappingStr);
                    }

                    int insertSize = insert(inputFolder, Settings.IndexingMode.SEQUENTIAL);

                    if (insertSize != 0) {
                        // if ok, refresh the index, then search
                        es.refresh(index);
                        System.out.println("Successfully creating index.");
                    } else {
                        System.out.println("Indexed zero file: please check!");
                    }

                } else if (command.toLowerCase().equals("search")) {
                    search(inputFolder, resultOffset, resultsSize);
                }
                es.shutdown();
            } else {
                System.out.println("ERROR: cannot create Elasticsearch client ... ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean createIndex(String indexSettings, String mappingStr) {

        if (isPrint) System.out.println("INDEX," + index);

        // delete the index if it exists
        if (es.doesIndexExist(index)) {
            es.deleteIndex(index);
        }
        // create index
        boolean isCreated = es.createIndex(index, type, indexSettings, mappingStr);
        if (!isCreated) {
            System.err.println("Cannot create index: " + index);
        }
        return isCreated;
    }

    protected EvalResult runExperiment(String hostname,
                                       String indexName,
                                       String typeName,
                                       String inputDir,
                                       String[] normModes,
                                       int[] ngramSizes,
                                       boolean useNgram,
                                       boolean useDFS,
                                       String outputDir,
                                       boolean writeToOutputFile,
                                       String indexSettings,
                                       String mappingStr,
                                       boolean printLog,
                                       boolean isDeleteIndex,
                                       String errMeasure,
                                       int resultOffset,
                                       int querySizeLimit,
                                       int minCloneLine,
                                       String methodParserMode,
                                       String cloneClusterFilePrefix
    ) {

        server = hostname;
        type = typeName;
        inputFolder = inputDir;
        isNgram = useNgram;
        isDFS = useDFS;
        outputFolder = outputDir;
        writeToFile = writeToOutputFile;
        isPrint = printLog;
        this.resultOffset = resultOffset;
        this.querySizeLimit = querySizeLimit;
        this.minCloneLine = minCloneLine;
        this.methodParserMode = methodParserMode;
        this.cloneClusterFile = "resources/" + cloneClusterFilePrefix + "_" + this.methodParserMode + ".csv";

        // create a connector
        es = new ESConnector(server);

        EvalResult bestResult = new EvalResult();

        // tries to delete the combination results of all settings if the file exist.
        // we're gonna generate this from the experiment.
        File allErrorMeasureResults = new File("all_" + errMeasure + ".csv");
        if (allErrorMeasureResults.exists())
            allErrorMeasureResults.delete();

        try {

            es.startup();
            for (String normMode : normModes) {

                // reset the modes before setting it again
                modes.reset();

                // set the normalisation + tokenization mode
                TokenizerMode tknzMode = new TokenizerMode();
                modes = tknzMode.setTokenizerMode(normMode.toLowerCase().toCharArray());

                for (int ngramSize : ngramSizes) {

                    index = indexName + "_" + normMode + "_" + ngramSize;
                    if (isPrint) System.out.println("INDEX," + index);

                    // delete the index if it exists
                    if (es.doesIndexExist(index)) {
                        es.deleteIndex(index);
                    }

                    // create index
                    if (!es.createIndex(index, type, indexSettings, mappingStr)) {
                        System.err.println("Cannot create index: " + index);
                        System.exit(-1);
                    }

                    // initialise the ngram generator
                    ngen = new nGramGenerator(ngramSize);
                    totalDocuments = insert(inputFolder, Settings.IndexingMode.SEQUENTIAL);

                    if (totalDocuments != 0) {
                        // if ok, refresh the index, then search
                        es.refresh(index);
                        EvalResult result = evaluate(index, outputDir, errMeasure, isPrint);

                        if (result.getValue() > bestResult.getValue()) {
                            bestResult = result;
                        }

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
                }
            }
            es.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bestResult;
    }

    @SuppressWarnings("unchecked")
    private int insert(String inputFolder, int indexMode) throws Exception {
        boolean isIndexed = true;
        ArrayList<Document> docArray = new ArrayList<>();
        File folder = new File(inputFolder);
        List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);
        // counter for id
        int count = 0;
        int fileCount = 0;
        System.out.println("Found " + listOfFiles.size() + " files.");
        for (File file : listOfFiles) {
            fileCount++;
            if (fileCount % printEvery == 0)
                System.out.println("Indexed " + fileCount + " documents.");
            try {
                // extract license (if any) using Ninka
                // String license = LicenseExtractor.extractLicenseWithNinka(file.getAbsolutePath()).split(";")[1];
                String license = "NONE";

                String filePath = file.getAbsolutePath().replace(Experiment.prefixToRemove, "");

                if (isPrint)
                    System.out.println(count + ": " + filePath);

                // parse each file into method (if possible)
                MethodParser methodParser = new MethodParser(
                        file.getAbsolutePath(),
                        Experiment.prefixToRemove,
                        methodParserMode);
                ArrayList<Method> methodList;

                try {
                    methodList = methodParser.parseMethods();
                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            // check minimum size
                            // TODO: should we check for size here?
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {
                                // Create Document object and put in an array list
                                String normSource = tokenize(method.getSrc());
                                // Use file name as id
                                Document d = new Document(
                                        String.valueOf(count),
                                        filePath + "_" + method.getName(),
                                        normSource,
                                        method.getSrc(),
                                        license,
                                        "");
                                // add document to array
                                docArray.add(d);
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("ERROR: error while extracting methods.");
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
                        // reset the array list
                        docArray.clear();
                    }
                }
                // index every 100 docs
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
            } catch (Exception e) {
                System.out.println("ERROR: error while indexing a file: " + file.getAbsolutePath() + ". Skip.");
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

        return count;
    }


    @SuppressWarnings("unchecked")
    private String search(String inputFolder, int offset, int size) throws Exception {
        System.out.println("Query size limit = " + querySizeLimit);
        String outToFile = "";

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
            int methodCount = 0;

            for (File file : listOfFiles) {
                if (isPrint)
                    System.out.println("File: " + file.getAbsolutePath());

                // reset the output buffer
                outToFile = "";

                // parse each file into method (if possible)
                MethodParser methodParser = new MethodParser(
                        file.getAbsolutePath(),
                        Experiment.prefixToRemove,
                        methodParserMode);
                ArrayList<Method> methodList;
                String query = "";

                try {
                    methodList = methodParser.parseMethods();
                    ArrayList<Document> results = new ArrayList<>();

                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            // check minimum size
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {
                                // write output to file
                                outToFile += method.getFile().replace(Experiment.prefixToRemove, "") + "_"
                                        + method.getName() + ",";

                                query = tokenize(method.getSrc());

                                // query size limit is enforced
                                if (querySizeLimit != 0) {
                                    // find the top-N rare terms in the query
                                    String tmpQuery = query;
                                    // clear the query
                                    query = "";
                                    ArrayList<JavaTerm> selectedTerms = getSelectedTerms(index, tmpQuery, querySizeLimit);
                                    int limit = querySizeLimit;

                                    if (selectedTerms.size() < querySizeLimit)
                                        limit = selectedTerms.size();

                                    for (int i = 0; i < limit; i++) {
                                        if (isPrint)
                                            System.out.println(selectedTerms.get(i).getFreq()
                                                    + ":"
                                                    + selectedTerms.get(i).getTerm());
                                        query += selectedTerms.get(i).getTerm() + " ";
                                    }
                                    if (isPrint)
                                        System.out.println("QUERY" + methodCount + " : " + query);
                                }

                                // search for results
                                results = es.search(index, type, query, isPrint, isDFS, offset, size);

                                int resultCount = 0;
                                for (Document d : results) {
                                    if (resultCount > 0)
                                        outToFile += ","; // add comma in between

                                    outToFile += d.getFile();
                                    resultCount++;
                                }

                                outToFile += "\n";
                                methodCount++;
                            }
                        }
                    } else {
                        // check minimum size
                        if (MyUtils.countLines(file.getAbsolutePath()) >= minCloneLine) {
                            query = tokenize(file);
                            // search for results
                            results = es.search(index, type, query, isPrint, isDFS, offset, size);
                            outToFile += file.getAbsolutePath().replace(Experiment.prefixToRemove, "") +
                                    "_noMethod" +
                                    ",";
                            int resultCount = 0;
                            for (Document d : results) {
                                if (resultCount > 0)
                                    outToFile += ","; // add comma in between

                                outToFile += d.getFile();
                                resultCount++;
                            }
                            outToFile += "\n";
                        }
                    }

                    count++;

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Error: file " + count +" generates query term size exceeds 4096 (too big).");
                }

                bw.write(outToFile);
            }

            bw.close();

            System.out.println("Searching done for " + count + " files (" + methodCount + " methods).");
            System.out.println("See output at " + outfile.getAbsolutePath());

        } else {
            throw new IOException("Cannot create the output file: " + outfile.getAbsolutePath());
        }

        return outfile.getAbsolutePath();
    }

    private String tokenize(File file) throws Exception {
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

    private String tokenize(String sourcecode) throws Exception {
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

    public ArrayList<String> tokenizeStringToArray(String sourcecode) throws Exception {
        String src;
        JavaTokenizer tokenizer = new JavaTokenizer(modes);

        // generate tokens
        ArrayList<String> tokens = tokenizer.getTokensFromString(sourcecode);

        // enter ngram mode
        if (isNgram) {
            tokens = ngen.generateNGramsFromJavaTokens(tokens);
        }
        return tokens;
    }

    /* copied from http://stackoverflow.com/questions/4640034/calculating-all-of-the-subsets-of-a-set-of-numbers */
    public Set<Set<String>> powerSet(Set<String> originalSet) {
        Set<Set<String>> sets = new HashSet<Set<String>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<String>());
            return sets;
        }
        List<String> list = new ArrayList<String>(originalSet);
        String head = list.get(0);

        Set<String> rest = new HashSet<String>(list.subList(1, list.size()));
        for (Set<String> set : powerSet(rest)) {
            Set<String> newSet = new HashSet<String>();
            newSet.add(head);
            newSet.addAll(set);
            // TODO: do we need to write to a file here?
//			Experiment.writeToFile(outputFolder
//					, "queries.txt"
//					, newSet.toString().replace("[","").replace(",","").replace("]","\n")
//					, true);
            sets.add(newSet);
            sets.add(set);
        }

        return sets;
    }

    public ArrayList<String> generate2NQuery(ArrayList<String> tokens) {
        ArrayList<String> querySet = new ArrayList<String>();

        // create a set to store query terms (removing duplicated terms)
        Set<String> queryTerms = new HashSet<String>(tokens);

        if (isPrint)
            System.out.println("Size of term (set-based): " + queryTerms.size());

        Set<Set<String>> possibleQueries = powerSet(queryTerms);

        if (isPrint)
            System.out.println("Size of sub queries: " + possibleQueries.size());

        for (Set<String> query: possibleQueries) {
            String queryStr = "";
            for (String t: query) {
                queryStr += t + " ";
            }
            querySet.add(queryStr.trim());
        }

        return querySet;
    }

    private String performSearch(String query, String outputFileLocation, String fileName, String output) throws Exception {

        String outToFile = output;
        outToFile += query + ",";

        // search for results
        ArrayList<Document> results = es.search(index, type, query, isPrint, isDFS, 0, 10);
        int resultCount = 0;

        for (Document d : results) {
            if (resultCount>0)
                outToFile += ","; // add comma in between

            outToFile += d.getFile();
            resultCount++;
        }

        outToFile += "\n";

        Experiment.writeToFile(outputFileLocation, fileName, outToFile, false);
        System.out.println("Searching done. See output at " + outputFileLocation + "/" + fileName);

        return outputFileLocation + "/" + fileName;
    }

    /***
     * Read idf of each term in the query directly from Lucene index
     * @param terms query containing search terms
     * @param selectedSize size of the selected terms
     * @return selected top-selectedSize terms
     */
    private ArrayList<JavaTerm> getSelectedTerms(String indexName, String terms, int selectedSize) {

        String indexFile = "/Users/Chaiyong/elasticsearch-2.2.0/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        ArrayList<JavaTerm> selectedTermsArray = new ArrayList<>();

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            String[] termsArr = terms.split(" ");
            for (String term: termsArr) {
                // TODO: get rid of the blank term (why it's blank?)
                if (!term.equals("")) {
                    Term t = new Term("src", term);
                    int freq = reader.docFreq(t);
                    JavaTerm newTerm = new JavaTerm(term, freq);
                    if (!selectedTermsArray.contains(newTerm))
                        selectedTermsArray.add(newTerm);
                }
            }

            /* copied from https://stackoverflow.com/questions/18441846/how-to-sort-an-arraylist-in-java */
            Collections.sort(selectedTermsArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return selectedTermsArray;
    }

    /***
     * Evaluate the search results by either r-precision or mean average precision (MAP)
     * @param mode parameter settings
     * @param workingDir location of the results
     * @param errMeasure type of error measure
     * @return A pair of the best performance (either ARP or MAP) and its value
     */
    private EvalResult evaluate(String mode,
                                String workingDir,
                                String errMeasure,
                                boolean isPrint) throws Exception {

        // default is method-level evaluator
        Evaluator evaluator = new MethodLevelEvaluator(
                this.cloneClusterFile,
                mode,
                workingDir,
                isPrint);

        // if file-level is specified, switch to file-level evaluator
        if (methodParserMode.equals(Settings.MethodParserType.FILE))
            evaluator = new FileLevelEvaluator(
                    this.cloneClusterFile,
                    mode,
                    workingDir,
                    isPrint);

        // generate a search key and retrieve result size (if MAP)
        int searchKeySize = evaluator.generateSearchKey();
        EvalResult result = new EvalResult();
        String outputFile = "";

        if (errMeasure.equals(Settings.ErrorMeasure.ARP)) {
            outputFile = search(inputFolder, resultOffset, resultsSize);
            double arp = evaluator.evaluateARP(outputFile, resultsSize);
            if (isPrint)
                System.out.println(Settings.ErrorMeasure.ARP + ": " + arp);
            // update the max ARP value
            if (result.getValue() < arp) {
                result.setValue(arp);
                result.setSetting(outputFile);
            }
        } else if (errMeasure.equals(Settings.ErrorMeasure.MAP)) {
            outputFile = search(inputFolder, resultOffset, totalDocuments);
            double map = evaluator.evaluateMAP(outputFile, totalDocuments);
            if (isPrint)
                System.out.println(Settings.ErrorMeasure.MAP + ": " + map);
            // update the max MAP value
            if (result.getValue() < map) {
                result.setValue(map);
                result.setSetting(outputFile);
            }
        } else {
            System.out.println("ERROR: Invalid evaluation method.");
        }

        Experiment.writeToFile(workingDir,
                "all_" + errMeasure + ".csv",
                result.toString().replace(Experiment.prefixToRemove, "") + "\n",
                true);

        return result;
    }

    private int findTP(ArrayList<String> results, String query) {
        int tp = 0;
        for (String result : results) {
            if (result.contains(query)) {
                tp++;
            }
        }
        return tp;
    }

    /***
     * Copied from: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     */
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private String printArray(ArrayList<String> arr, boolean pretty) {
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

    private String escapeString(String input) {
        String output = "";
        output += input.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/").replace("\b", "\\b")
                .replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        return output;
    }

    public void setIsPrint(boolean isPrint) {
        this.isPrint = isPrint;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public void setNormMode(String normMode) {
        this.normMode = normMode;
    }
}
