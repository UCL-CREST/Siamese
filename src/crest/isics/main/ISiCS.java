package crest.isics.main;

import crest.isics.document.JavaTerm;
import crest.isics.document.JavaTermComparator;
import crest.isics.helpers.*;
import crest.isics.settings.CustomSettings;
import crest.isics.settings.Settings;
import crest.isics.settings.TokenizerMode;
import crest.isics.document.Document;
import crest.isics.document.Method;
import crest.isics.settings.IndexSettings;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.elasticsearch.client.Client;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.store.FSDirectory;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    private int ngramSize;
    private boolean isNgram;
    private boolean isPrint;
    private nGramGenerator ngen;
    private boolean isDFS;
    private String outputFolder;
    private boolean writeToFile;
    private String extension;
    private int minCloneLine;
    private int resultOffset;
    private int resultsSize;
    private int totalDocuments;
    private boolean queryReduction;
    private int qrPercentileNorm;
    private int qrPercentileOrig;
    private boolean recreateIndexIfExists;
    private String parseMode;
    private String cloneClusterFile;
    private int printEvery;
    private String command;
    private int rankingFunc;
    private String errMeasure;
    private boolean deleteIndexAfterUse;
    private String prefixToRemove;
    private String elasticsearchLoc;
    private String outputFormat;
    private Client isicsClient;
    private String indexingMode;
    private int bulkSize;
    private int normBoost;
    private int origBoost;

    public ISiCS(String configFile) {
        readFromConfigFile(configFile);
        printConfig();
    }

    private void readFromConfigFile(String configFile) {
	    /* copied from
	    https://www.mkyong.com/java/java-properties-file-examples/
	     */
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(configFile);
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            server = prop.getProperty("server");
            index = prop.getProperty("index");
            type = prop.getProperty("type");
            inputFolder = prop.getProperty("inputFolder");
            outputFolder = prop.getProperty("outputFolder");

            normMode = prop.getProperty("normMode");
            // set the normalisation + tokenization mode
            TokenizerMode tknzMode = new TokenizerMode();
            modes = tknzMode.setTokenizerMode(normMode.toLowerCase().toCharArray());

            isNgram = Boolean.parseBoolean(prop.getProperty("isNgram"));
            ngramSize = Integer.parseInt(prop.getProperty("ngramSize"));
            isPrint = Boolean.parseBoolean(prop.getProperty("isPrint"));
            isDFS = Boolean.parseBoolean(prop.getProperty("dfs"));
            writeToFile = Boolean.parseBoolean(prop.getProperty("writeToFile"));
            extension = prop.getProperty("extension");
            minCloneLine=Integer.parseInt(prop.getProperty("minCloneSize"));
            command = prop.getProperty("command");

            String ranking = prop.getProperty("rankingFunction");
            if (ranking.equals("tfidf"))
                rankingFunc = Settings.RankingFunction.TFIDF;
            else if (ranking.equals("bm25"))
                rankingFunc = Settings.RankingFunction.BM25;
            else if (ranking.equals("dfr"))
                rankingFunc = Settings.RankingFunction.DFR;
            else if (ranking.equals("ib"))
                rankingFunc = Settings.RankingFunction.IB;
            else if (ranking.equals("lmd"))
                rankingFunc = Settings.RankingFunction.LMD;
            else if (ranking.equals("lmj"))
                rankingFunc = Settings.RankingFunction.LMJ;

            // get the property value and print it out
            this.resultOffset = Integer.parseInt(prop.getProperty("resultOffset"));
            this.resultsSize = Integer.parseInt(prop.getProperty("resultsSize"));
            this.totalDocuments = Integer.parseInt(prop.getProperty("totalDocuments"));
            this.queryReduction = Boolean.parseBoolean(prop.getProperty("queryReduction"));
            this.qrPercentileNorm = Integer.parseInt(prop.getProperty("QRPercentileNorm"));
            this.qrPercentileOrig = Integer.parseInt(prop.getProperty("QRPercentileOrig"));
            this.normBoost = Integer.parseInt(prop.getProperty("normBoost"));
            this.origBoost = Integer.parseInt(prop.getProperty("origBoost"));

            this.recreateIndexIfExists = Boolean.parseBoolean(prop.getProperty("recreateIndexIfExists"));

            String parseModeConfig = prop.getProperty("parseMode");
            if (parseModeConfig.equals("method"))
                this.parseMode = Settings.MethodParserType.METHOD;
            else
                this.parseMode = Settings.MethodParserType.FILE;

            this.printEvery = Integer.parseInt(prop.getProperty("printEvery"));
            this.cloneClusterFile = "resources/clone_clusters_" + this.parseMode + ".csv";
            String errMeasureConfig = prop.getProperty("errorMeasure");
            if (errMeasureConfig.equals("arp"))
                errMeasure = Settings.ErrorMeasure.ARP;
            else
                errMeasure = Settings.ErrorMeasure.MAP;

            deleteIndexAfterUse = Boolean.parseBoolean(prop.getProperty("deleteIndexAfterUse"));

            prefixToRemove = inputFolder;
            if (!prefixToRemove.endsWith("/"))
                prefixToRemove += "/"; // append / at the end

            elasticsearchLoc = prop.getProperty("elasticsearchLoc");
            outputFormat = prop.getProperty("outputFormat");
            indexingMode = prop.getProperty("indexingMode");
            bulkSize = Integer.parseInt(prop.getProperty("bulkSize"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printConfig() {
        System.out.println("====== Configurations ======");
        System.out.println("server         : " + server);
        System.out.println("index          : " + index);
        System.out.println("type           : " + type);
        System.out.println("inputFolder    : " + inputFolder);
        System.out.println("outputFolder   : " + outputFolder);
        System.out.println("normalization  : " + normMode);
        System.out.println("ngramSize      : " + ngramSize);
        System.out.println("verbose        : " + isPrint);
        System.out.println("dfs            : " + isDFS);
        System.out.println("extension      : " + extension);
        System.out.println("minCloneSize   : " + minCloneLine);
        System.out.println("command        : " + command);
        System.out.println("queryReduction : " + queryReduction);
        System.out.println("outputFormat   : " + outputFormat);
        System.out.println("indexingMode   : " + indexingMode + " (" + bulkSize + ")");
        System.out.println("============================");
    }

    private void connect() {
        // create a connector
        es = new ESConnector(server);
    }

    public void startup() {
        connect();
        try {
            isicsClient = es.startup();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        es.shutdown();
    }

    private OutputFormatter getOutputFormatter() {
        OutputFormatter formatter = new OutputFormatter();
        if (outputFormat.equals("csv")) {
            formatter.setFormat("csv");
            formatter.setAddStartEndLine(false);
        } else if (outputFormat.equals("csvfline")) {
            formatter.setFormat("csv");
            formatter.setAddStartEndLine(true);
        } else {
            System.out.println("ERROR: wrong output format");
            return null;
        }

        return formatter;
    }

    public String execute() throws Exception {
        // check if the client is already started up
        if (isicsClient == null) {
            startup();
        }

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

        String outputFile = "";

        try {
            if (isicsClient != null) {
                if (command.toLowerCase().equals("index")) {

                    if (recreateIndexIfExists) {
                        createIndex(indexSettings, mappingStr);
                    }

                    int insertSize = insert();

                    if (insertSize != 0) {
                        // if ok, refresh the index, then search
                        es.refresh(index);
                        System.out.println("Successfully creating index.");
                    } else {
                        System.out.println("ERROR: Indexed zero file. Please check!");
                    }

                } else if (command.toLowerCase().equals("search")) {
                    if (es.doesIndexExist(this.index)) {
                        OutputFormatter formatter = getOutputFormatter();
                        outputFile = search(inputFolder, resultOffset, resultsSize, queryReduction, formatter);
                    } else {
                        // index does not exist
                        throw new Exception("index " + this.index + " does not exist.");
                    }
                }
            } else {
                System.out.println("ERROR: cannot create Elasticsearch client ... ");
            }
//        } catch (NoNodeAvailableException noNodeException) {
//            throw noNodeException;
        }  catch (Exception e) {
            throw e;
        }

        return outputFile;
    }

    private boolean createIndex(String indexSettings, String mappingStr) throws NoNodeAvailableException {
        try {
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
        } catch (NoNodeAvailableException e) {
            throw e;
        }
    }

    protected ArrayList<EvalResult> runExperiment(
            String indexSettings,
            String mappingStr,
            String[] normModes,
            int[] ngramSizes,
            String cloneClusterFilePrefix) {

        this.cloneClusterFile = "resources/" + cloneClusterFilePrefix + "_" + this.parseMode + ".csv";

        // create a connector
        es = new ESConnector(server);

        ArrayList<EvalResult> resultSet = new ArrayList<>();

        // tries to delete the combination results of all settings if the file exist.
        // we're gonna generate this from the experiment.
        File allErrorMeasureResults = new File("all_" + this.errMeasure + ".csv");
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

                String indexPrefix = this.index;

                for (int ngramSize : ngramSizes) {

                    index = this.index + "_" + normMode + "_" + ngramSize;
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
                    totalDocuments = insert();

                    if (totalDocuments != 0) {
                        // if ok, refresh the index, then search
                        es.refresh(index);

                        EvalResult result = evaluate(index, outputFolder, errMeasure, queryReduction, isPrint);

                        if (resultSet.size() != 0) {
                            EvalResult bestResult = resultSet.get(0);

                            // check for best result
                            if (result.getValue() > bestResult.getValue()) {
                                resultSet.set(0, result);
                            }
                        } else {
                            // add the first result twice since it's also the best result.
                            resultSet.add(result);
                        }

                        // collect the result
                        resultSet.add(result);

                    } else {
                        System.out.println("Indexing error: please check!");
                    }

                    // delete index
                    if (deleteIndexAfterUse) {
                        if (!es.deleteIndex(index)) {
                            System.err.println("Cannot delete index: " + index);
                            System.exit(-1);
                        }
                    }

                    // restore index name
                    this.index = indexPrefix;
                }
            }
            es.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    @SuppressWarnings("unchecked")
    private int insert() throws Exception {
        boolean isIndexed = true;
        ArrayList<Document> docArray = new ArrayList<>();
        ArrayList<String> origDocArray = new ArrayList<>();
        File folder = new File(inputFolder);

        // create an array of string for extensions
        String[] extensions = new String[1];
        extensions[0] = extension;

        List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);
        // counter for id
        int count = 0;
        int fileCount = 0;
        System.out.println("Found " + listOfFiles.size() + " files.");
        for (File file : listOfFiles) {
            try {
                // extract license (if any) using Ninka
                // String license = LicenseExtractor.extractLicenseWithNinka(file.getAbsolutePath()).split(";")[1];
                String license = "NONE";

                String filePath = file.getAbsolutePath().replace(prefixToRemove, "");

                if (isPrint)
                    System.out.println(count + ": " + filePath);

                fileCount++;

                // parse each file into method (if possible)
                MethodParser methodParser = new MethodParser(
                        file.getAbsolutePath(),
                        prefixToRemove,
                        parseMode,
                        isPrint);
                ArrayList<Method> methodList;

                try {
                    methodList = methodParser.parseMethods();
                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            // check minimum size
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {

                                // Create Document object and put in an array list
                                String normSource = tokenize(method.getSrc(), modes, isNgram);

                                TokenizerMode tmode = new TokenizerMode();
                                char[] xmode = {'x'};
                                tmode.setTokenizerMode(xmode);
                                String tokenizedSource = tokenize(method.getSrc(), tmode, false);

                                // Use file name as id
                                Document d = new Document(
                                        String.valueOf(count),
                                        filePath + "_" + method.getName(),
                                        method.getStartLine(),
                                        method.getEndLine(),
                                        normSource,
                                        tokenizedSource,
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

                if (this.indexingMode.equals(Settings.IndexingMode.SEQUENTIAL)) {
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
                } else if (this.indexingMode.equals(Settings.IndexingMode.BULK)) {
                    // index every N docs (bulk insertion mode)
                    if (docArray.size() >= this.bulkSize) {
//                        System.out.println(docArray.size());
                        isIndexed = es.bulkInsert(index, type, docArray);
                        if (!isIndexed) {
                            throw new Exception("Cannot bulk insert documents");
                        }
                        else {
                            // reset the array list
                            docArray.clear();
                        }
                    }
                }

                if (fileCount % printEvery == 0) {
                    double percent = (double) fileCount * 100 / listOfFiles.size();
                    DecimalFormat df = new DecimalFormat("#.00");
                    System.out.println("Indexed " + fileCount
                            + " [" + df.format(percent) + "%] documents (" + count + " methods).");
                }
            } catch (Exception e) {
                System.out.println("ERROR: error while indexing a file: " + file.getAbsolutePath() + ". Skip.");
            }
        }

        // the last batch
        if (this.indexingMode.equals(Settings.IndexingMode.BULK) && docArray.size() != 0) {
            isIndexed = es.bulkInsert(index, type, docArray);

            if (!isIndexed)
                throw new Exception("Cannot bulk insert documents");
            else {
                // reset the array list
                docArray.clear();
            }
        }

        if (fileCount % printEvery != 0) {
            double percent = (double) fileCount * 100 / listOfFiles.size();
            DecimalFormat df = new DecimalFormat("#.00");
            System.out.println("Indexed " + fileCount
                    + " [" + df.format(percent) + "%] documents (" + count + " methods).");
        }

        // successfully indexed, return true
        System.out.println("Successfully indexed documents.");

        return count;
    }

    protected String search(
            String inputFolder,
            int offset,
            int size,
            boolean queryReduction,
            OutputFormatter formatter) throws Exception {

        String qr = "no_qr";
        if (queryReduction) {
            qr = "qr";
        }

        String outToFile = "";

        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-S");
        Date dateobj = new Date();
        File outfile = new File(outputFolder + "/" + index + "_" + qr + "_"
                + df.format(dateobj) + ".csv");

        // if file doesn't exists, then create it
        boolean isCreated = false;
        if (!outfile.exists()) {
            isCreated = outfile.createNewFile();
        }

        if (isCreated) {
            FileWriter fw = new FileWriter(outfile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            // create an array of string for extensions
            String[] extensions = new String[1];
            extensions[0] = extension;
            File folder = new File(inputFolder);
            List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);

            int count = 0;
            int methodCount = 0;

            for (File file : listOfFiles) {
//                if (isPrint)
                    System.out.println("File: " + file.getAbsolutePath());

                // reset the output buffer
                outToFile = "";

                // parse each file into method (if possible)
                MethodParser methodParser = new MethodParser(
                        file.getAbsolutePath(),
                        prefixToRemove,
                        parseMode,
                        isPrint);
                ArrayList<Method> methodList;
                String query = "";
                String origQuery = "";

                try {
                    methodList = methodParser.parseMethods();
                    ArrayList<Document> results = new ArrayList<>();

                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {

                            // check minimum size
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {
                                /* TODO: fix this some time. It's weird to have a list with only a single object. */
                                // write output to file
                                ArrayList<Document> queryList = new ArrayList<>();
                                Document d = new Document();
                                d.setFile(method.getFile() + "_" + method.getName());
                                d.setStartline(method.getStartLine());
                                d.setEndline(method.getEndLine());
                                queryList.add(d);
                                outToFile += formatter.format(queryList, prefixToRemove) + ",";

                                TokenizerMode tmode = new TokenizerMode();
                                char[] noNormMode = {'x'};
                                tmode.setTokenizerMode(noNormMode);
                                origQuery = tokenize(method.getSrc(), tmode, false);
                                query = tokenize(method.getSrc(), modes, isNgram);

                                // query size limit is enforced
                                if (queryReduction) {
                                    long docCount = getIndicesStats();
                                    query = reduceQuery(query, "src", this.qrPercentileNorm * docCount / 100);
                                    origQuery = reduceQuery(origQuery, "tokenizedsrc", this.qrPercentileOrig * docCount / 100);
                                    if (isPrint) {
                                        System.out.println("NQ" + methodCount + " : " + query);
                                        System.out.println("OQ" + methodCount + " : " + origQuery);
                                    }
                                }

                                // search for results
//                                results = es.search(index, type, query, isPrint, isDFS, offset, size);
                                results = es.search(index, type, origQuery, query, origBoost, normBoost, isPrint, isDFS, offset, size);
                                outToFile += formatter.format(results, prefixToRemove);
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
                            outToFile += file.getAbsolutePath().replace(prefixToRemove, "") +
                                    "_noMethod" +
                                    ",";
                            outToFile += formatter.format(results, prefixToRemove);
                            outToFile += "\n";
                        }
                    }

                    count++;

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    System.out.println("ERROR: file " + count +" generates query term size exceeds 4096 (too big).");
                }

                bw.write(outToFile);
            }

            bw.close();

            System.out.println("Searching done for " + count + " files (" + methodCount + " methods after clone size filtering).");
            System.out.println("See output at " + outfile.getAbsolutePath());

        } else {
            throw new IOException("Cannot create the output file: " + outfile.getAbsolutePath());
        }

        return outfile.getAbsolutePath();
    }

    private String reduceQuery(String query, String field, double limit) {
        // find the top-N rare terms in the query
        String tmpQuery = query;
        // clear the query
        query = "";
        ArrayList<JavaTerm> sortedTerms = sortTermsByFreq(index, field, tmpQuery);

        // switched to use median as a cut-off
//        double limit = percentile;
//        System.out.println("LIMIT: " + limit);

        for (int i=0; i<sortedTerms.size(); i++) {
            if (sortedTerms.get(i).getFreq() <= limit)
                query += sortedTerms.get(i).getTerm() + " ";
        }

//        System.out.println(query);

        return query;
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
                                boolean queryReduction,
                                boolean isPrint) throws Exception {

        // default is method-level evaluator
        Evaluator evaluator = new MethodLevelEvaluator(
                this.cloneClusterFile,
                mode,
                workingDir,
                isPrint);

        // if file-level is specified, switch to file-level evaluator
        if (parseMode.equals(Settings.MethodParserType.FILE))
            evaluator = new FileLevelEvaluator(
                    this.cloneClusterFile,
                    mode,
                    workingDir,
                    isPrint);

        // generate a search key and retrieve result size (if MAP)
        int searchKeySize = evaluator.generateSearchKey();
        EvalResult result = new EvalResult();
        String outputFile = "";

        // get the output formatter according to the settings
        OutputFormatter formatter = getOutputFormatter();

        if (errMeasure.equals(Settings.ErrorMeasure.ARP)) {
            outputFile = search(inputFolder, resultOffset, resultsSize, queryReduction, formatter);
            double arp = evaluator.evaluateARP(outputFile, resultsSize);
            if (isPrint)
                System.out.println(Settings.ErrorMeasure.ARP + ": " + arp);
            // update the max ARP value
            if (result.getValue() < arp) {
                result.setValue(arp);
                result.setSetting(outputFile);
            }
            deleteOutputFile(outputFile);
        } else if (errMeasure.equals(Settings.ErrorMeasure.MAP)) {
            outputFile = search(inputFolder, resultOffset, totalDocuments, queryReduction, formatter);
            double map = evaluator.evaluateMAP(outputFile, totalDocuments);
            if (isPrint)
                System.out.println(Settings.ErrorMeasure.MAP + ": " + map);
            // update the max MAP value
            if (result.getValue() < map) {
                result.setValue(map);
                result.setSetting(outputFile);
            }
            deleteOutputFile(outputFile);
        } else {
            System.out.println("ERROR: Invalid evaluation method.");
        }

        return result;
    }

    public double getValueAtPercentile(ArrayList<JavaTerm> termList, int percentile) {
        double[] data = new double[termList.size()];
        for (int i=0; i<termList.size(); i++) {
            data[i] = termList.get(i).getFreq();
        }
        /* copied from http://stackoverflow.com/questions/19700704/java-api-for-calculating-interquartile-range */
        DescriptiveStatistics da = new DescriptiveStatistics(data);
        return da.getPercentile(percentile);
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

    private String tokenize(String sourcecode, TokenizerMode modes, boolean isNgram) throws Exception {
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

        MyUtils.writeToFile(outputFileLocation, fileName, outToFile, false);
        System.out.println("Searching done. See output at " + outputFileLocation + "/" + fileName);

        return outputFileLocation + "/" + fileName;
    }

    public long getIndicesStats() {
        return es.getIndicesStats(this.index);
    }

    public void analyseTermFreq(String indexName, String field, String freqType, String outputFileName) {
        String indexFile = elasticsearchLoc + "/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        ArrayList<JavaTerm> tokFreq = new ArrayList<>();
        ClassicSimilarity similarity = new ClassicSimilarity();

        File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }

        /* adapted from
        https://stackoverflow.com/questions/28244961/lucene-4-10-2-calculate-tf-idf-for-all-terms-in-index
         */
        int count = 0;
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            int docnum = reader.numDocs();
            Fields fields = MultiFields.getFields(reader);
            Terms terms = fields.terms(field);

            TermsEnum termsEnum = terms.iterator();
            int size = 0;

            // TODO: is there a better solution?
            // iterate to get the size
            while (termsEnum.next() != null) {
                size++;
            }

            String[] termArr = new String[size];
            long[] freqArr = new long[size];

            // do the real work
            termsEnum = terms.iterator();
            while (termsEnum.next() != null) {
                String term = termsEnum.term().utf8ToString();
                long tfreq = 0;

                if (freqType.equals("tf"))
                    tfreq = termsEnum.totalTermFreq();
                else if (freqType.equals("df"))
                    tfreq = termsEnum.docFreq();
                else {
                    System.out.println("Wrong frequency. Quit!");
                    System.exit(0);
                }

                termArr[count] = term;
                freqArr[count] = tfreq;
                count++;

                if (count % 10000 == 0) {
//                    System.out.println("Processed " + count + " terms");
                }
            }
            System.out.println("Total: " + count);

            double[] data = new double[size];
            String output = "freq\n";
            for (int i = 0; i < freqArr.length; i++) {
                data[i] = freqArr[i];
                output += freqArr[i] + "\n";
                if (i > 0 && i % 10000 == 0) {
//                    System.out.println("Saving " + (i) + " terms.");
                    MyUtils.writeToFile("./",outputFileName, output, true);
                    output = "";
                }
            }
            // write the rest to the file
            MyUtils.writeToFile("./",outputFileName, output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Read idf of each term in the query directly from Lucene index
     * @param indexName name of the index
     * @param terms query containing search terms
     * @return selected top-selectionRatio terms
     */
    private ArrayList<JavaTerm> sortTermsByFreq(String indexName, String field, String terms) {

        String indexFile = elasticsearchLoc + "/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        ArrayList<JavaTerm> selectedTermsArray = new ArrayList<>();

        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            String[] termsArr = terms.split(" ");
            for (String term: termsArr) {
                // TODO: get rid of the blank term (why it's blank?)
                if (!term.equals("")) {
                    Term t = new Term(field, term);
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

    private boolean deleteOutputFile(String outputFile) {
        // if set to delete the output file, delete
        if (CustomSettings.DELETE_OUTPUT_FILE) {
            File oFile = new File(outputFile);
            return oFile.delete();
        }
        return false;
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

    public void setResultOffset(int resultOffset) {
        this.resultOffset = resultOffset;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }
}
