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

import crest.siamese.document.Document;
import crest.siamese.document.JavaTerm;
import crest.siamese.document.Method;
import crest.siamese.helpers.ESConnector;
import crest.siamese.helpers.EvalResult;
import crest.siamese.helpers.Evaluator;
import crest.siamese.helpers.FileLevelEvaluator;
import crest.siamese.helpers.LicenseExtractor;
import crest.siamese.helpers.MethodLevelEvaluator;
import crest.siamese.helpers.MyUtils;
import crest.siamese.helpers.OutputFormatter;
import crest.siamese.helpers.nGramGenerator;
import crest.siamese.language.MethodParser;
import crest.siamese.language.Normalizer;
import crest.siamese.language.NormalizerMode;
import crest.siamese.language.Tokenizer;
import crest.siamese.settings.CustomSettings;
import crest.siamese.settings.IndexSettings;
import crest.siamese.settings.Settings;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;

public class Siamese {

    private ESConnector es;
    private String server;
    private String cluster;
    private String index;
    private String type;
    private String inputFolder;
    private String outputFolder;
    private String subInputFolder;
    private String normalizerModeName;
    private String t2NormMode;
    private String t3NormMode;
    private int ngramSize;
    private int t2NgramSize;
    private int t1NgramSize;
    private boolean isNgram;
    private boolean isPrint;
    private nGramGenerator ngen;
    private nGramGenerator t2Ngen;
    private nGramGenerator t1Ngen;
    private boolean isDFS;
    private boolean writeToFile;
    private String[] extensions;
    private int minCloneLine;
    private int resultOffset;
    private int resultsSize;
    private int totalDocuments;
    private boolean queryReduction;
    private double qrPercentileNorm;
    private double qrPercentileOrig;
    private double qrPercentileT2;
    private double qrPercentileT1;
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
    private Client siameseClient;
    private String indexingMode;
    private int bulkSize;
    private int normBoost;
    private int origBoost;
    private int t2Boost;
    private int t1Boost;
    private String methodParserName;
    private String tokenizerName;
    private String normalizerName;
    private boolean multiRep;
    private boolean includeLicense;
    private String licenseExtractor;
    private String url = "none";
    private String fileLicense = "unknown";
    private boolean licenseFileDetection = false;
    private String computeSimilarity = "none";
    private String[] simThreshold = {"80%", "80%", "80%", "80%"};
    private Tokenizer tokenizer;
    private Tokenizer origTokenizer;
    private Tokenizer t2Tokenizer;
    private Tokenizer t1Tokenizer;
    private String deleteField;
    private String deleteWildcard;
    private int deleteAmount;
    private boolean[] enableRep = {true, true, true, true};
    private boolean github = false;
    private boolean ignoreQueryClones = false;
    private IndexReader esIndexRader;

    public Siamese(String configFile) {
        readFromConfigFile(configFile);
        printConfig();
        prepareTokenizers();
    }

    public Siamese(String configFile, String[] overridingParams) {
        readFromConfigFile(configFile);
        // check the overriding command line parameters
        if (!overridingParams[0].isEmpty()) // input folder
            setInputFolder(overridingParams[0]);
        if (!overridingParams[1].isEmpty()) // output folder
            setOutputFolder(overridingParams[1]);
        if (!overridingParams[2].isEmpty()) // command
            setCommand(overridingParams[2]);
        printConfig();
        prepareTokenizers();
    }

    // Reference: https://www.mkyong.com/java/java-properties-file-examples/
    private void readFromConfigFile(String configFile) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(configFile);
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            server = prop.getProperty("server");
            cluster = prop.getProperty("cluster");
            index = prop.getProperty("index");
            type = prop.getProperty("type");
            inputFolder = prop.getProperty("inputFolder");
            subInputFolder = prop.getProperty("subInputFolder");
            outputFolder = prop.getProperty("outputFolder");

            normalizerModeName = prop.getProperty("normalizerMode");
            t2NormMode = prop.getProperty("t2NormMode");
            t3NormMode = prop.getProperty("t3NormMode");

            isNgram = Boolean.parseBoolean(prop.getProperty("isNgram"));
            ngramSize = Integer.parseInt(prop.getProperty("ngramSize"));
            t2NgramSize = Integer.parseInt(prop.getProperty("t2NgramSize"));
            t1NgramSize = Integer.parseInt(prop.getProperty("t1NgramSize"));
            isPrint = Boolean.parseBoolean(prop.getProperty("isPrint"));
            isDFS = Boolean.parseBoolean(prop.getProperty("dfs"));
            writeToFile = Boolean.parseBoolean(prop.getProperty("writeToFile"));
            extensions = prop.getProperty("extension").split(",");
            minCloneLine = Integer.parseInt(prop.getProperty("minCloneSize"));
            command = prop.getProperty("command");

            String ranking = prop.getProperty("rankingFunction");
            switch (ranking) {
                case "tfidf":
                    rankingFunc = Settings.RankingFunction.TFIDF;
                    break;
                case "bm25":
                    rankingFunc = Settings.RankingFunction.BM25;
                    break;
                case "dfr":
                    rankingFunc = Settings.RankingFunction.DFR;
                    break;
                case "ib":
                    rankingFunc = Settings.RankingFunction.IB;
                    break;
                case "lmd":
                    rankingFunc = Settings.RankingFunction.LMD;
                    break;
                case "lmj":
                    rankingFunc = Settings.RankingFunction.LMJ;
                    break;
                default:
                    break;
            }

            // get the property value and print it out
            this.resultOffset = Integer.parseInt(prop.getProperty("resultOffset"));
            this.resultsSize = Integer.parseInt(prop.getProperty("resultsSize"));
            this.totalDocuments = Integer.parseInt(prop.getProperty("totalDocuments"));
            this.queryReduction = Boolean.parseBoolean(prop.getProperty("queryReduction"));
            this.qrPercentileNorm = Double.parseDouble(prop.getProperty("QRPercentileNorm"));
            this.qrPercentileOrig = Double.parseDouble(prop.getProperty("QRPercentileOrig"));
            this.qrPercentileT2 = Double.parseDouble(prop.getProperty("QRPercentileT2"));
            this.qrPercentileT1 = Double.parseDouble(prop.getProperty("QRPercentileT1"));
            this.normBoost = Integer.parseInt(prop.getProperty("normBoost"));
            this.origBoost = Integer.parseInt(prop.getProperty("origBoost"));
            this.t2Boost = Integer.parseInt(prop.getProperty("t2Boost"));
            this.t1Boost = Integer.parseInt(prop.getProperty("t1Boost"));
            // multi-representation
            this.multiRep = Boolean.parseBoolean(prop.getProperty("multirep"));
            // customization to support other languages
            this.methodParserName = prop.getProperty("methodParser");
            this.tokenizerName = prop.getProperty("tokenizer");
            this.normalizerName = prop.getProperty("normalizer");
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
            includeLicense = Boolean.parseBoolean(prop.getProperty("license"));
            licenseExtractor = prop.getProperty("licenseExtractor");
            licenseFileDetection = Boolean.parseBoolean(prop.getProperty("licenseFileDetection"));
            computeSimilarity = prop.getProperty("computeSimilarity");
            String simThresholds = prop.getProperty("simThreshold");
            simThreshold = simThresholds.split(",");
            if (command.equals("delete")) {
                deleteField = prop.getProperty("deleteField");
                deleteWildcard = prop.getProperty("deleteWildcard");
                deleteAmount = Integer.parseInt(prop.getProperty("deleteAmount"));
            }
            String[] enableRepStr = prop.getProperty("enableRep").split(",");
            for (int i = 0; i < enableRepStr.length; i++) {
                enableRep[i] = Boolean.valueOf(enableRepStr[i]);
            }
            github = Boolean.parseBoolean(prop.getProperty("github"));
            ignoreQueryClones = Boolean.parseBoolean(prop.getProperty("ignoreQueryClones"));
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
        System.out.println("========== Configurations ==========");
        System.out.println("---------- ELASTICSEARCH -----------");
        System.out.println("server         : " + server);
        System.out.println("index          : " + index);
        System.out.println("type           : " + type);
        System.out.println("---------- DATA --------------------");
        System.out.println("inputFolder    : " + inputFolder);
        System.out.println("outputFolder   : " + outputFolder);
        System.out.println("dfs            : " + isDFS);
        System.out.println("extension      : " + Arrays.toString(extensions));
        System.out.println("minCloneSize   : " + minCloneLine);
        System.out.println("---------- EXECUTION ---------------");
        System.out.println("command        : " + command);
        System.out.println("indexingMode   : " + indexingMode + " (" + bulkSize + ")");
        System.out.println("outputFormat   : " + outputFormat);
        System.out.println("---------- MULTI-REPRESENTATION ----");
        System.out.println("multiRep       : " + multiRep + " " + Arrays.toString(enableRep));
        System.out.println("T2 norm        : " + t2NormMode);
        System.out.println("T3 norm        : " + t3NormMode);
        System.out.println("ngramSize      : t1=" + t1NgramSize + " t2=" + t2NgramSize + " t3=" + ngramSize);
        System.out.println("---------- QUERY REDUCTION ---------");
        System.out.println("queryReduction : " + queryReduction);
        System.out.println("qrThresholds   : t0=" + this.qrPercentileOrig + " t1=" + this.qrPercentileT1 +
                " t2=" + this.qrPercentileT2 + " t3=" + this.qrPercentileNorm);
        System.out.println("queryBoosts    : t0=" + origBoost + " t1=" + t1Boost +
                " t2=" + t2Boost + " t3=" + normBoost);
        System.out.println("---------- SIMILARITY --------------");
        System.out.println("computeSimilarity : " + computeSimilarity);
        System.out.println("simThreshold      : " + simThreshold[0] + "," + simThreshold[1] + ","
                + simThreshold[2] + "," + simThreshold[3]);
        System.out.println("====================================");
    }

    private void connect() {
        // create a connector
        es = new ESConnector(server, cluster);
    }

    public void startup() {
        connect();
        try {
            siameseClient = es.startup();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        es.shutdown();
    }

    private void prepareTokenizers() {
        NormalizerMode origMode = initialiseNormalizerMode();
        Normalizer origNormalizer = initialiseNormalizer(origMode);
        origTokenizer = initialiseTokenizer(origNormalizer);

        NormalizerMode t1Mode = initialiseNormalizerMode();
        Normalizer t1Normalizer = initialiseNormalizer(t1Mode);
        t1Tokenizer = initialiseTokenizer(t1Normalizer);

        NormalizerMode t2Mode = initialiseNormalizerMode(t2NormMode.toLowerCase().toCharArray());
        Normalizer t2Normalizer = initialiseNormalizer(t2Mode);
        t2Tokenizer = initialiseTokenizer(t2Normalizer);

        // set the normalisation + tokenization mode
        NormalizerMode t3Mode = initialiseNormalizerMode(t3NormMode.toLowerCase().toCharArray());
        Normalizer normalizer = initialiseNormalizer(t3Mode);
        tokenizer = initialiseTokenizer(normalizer);
    }

    private OutputFormatter getOutputFormatter() {
        OutputFormatter formatter = new OutputFormatter();
        switch (outputFormat) {
            case "csv":
                formatter.setFormat("csv");
                formatter.setAddStartEndLine(false);
                break;
            case "csvfline":
                formatter.setFormat("csv");
                formatter.setAddStartEndLine(true);
                if (this.includeLicense) {
                    formatter.setAddLicense(true);
                }
                break;
            case "gcf":
                formatter.setFormat("gcf");
                break;
            case "json":
                formatter.setFormat("json");
                break;
            default:
                System.out.println("ERROR: wrong output format");
                return null;
        }
        return formatter;
    }

    public void delete(String field, String query, int amount) {
        System.out.println(es.delete(index, type, field, query, isDFS, amount));
    }

    public String execute() throws Exception {
        // check if the client is already started up
        if (siameseClient == null) {
            startup();
        }

        // initialise the n-gram generator
        ngen = new nGramGenerator(ngramSize);
        t2Ngen = new nGramGenerator(t2NgramSize);
        t1Ngen = new nGramGenerator(t1NgramSize);

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

        if (siameseClient != null) {
            if (command.toLowerCase().equals("index")) {
                if (this.github) {
                    genGitHubInfo();
                }
                /*
                Read the license from the license file at the root dir.
                Do this only once per input folder.
                 */
                if (recreateIndexIfExists) {
                    createIndex(indexSettings, mappingStr);
                }
                long startingId = 0;
                if (!recreateIndexIfExists && doesIndexExist()) {
                    startingId = getMaxId(index) + 1;
                }
                long insertSize = insert(startingId);
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
                    // create the output folder if it doesn't exist.
                    MyUtils.createDir(outputFolder);
                    // reading the index for query reduction
                    readESIndex(index);
                    outputFile = search(inputFolder, resultOffset, resultsSize, queryReduction, formatter);
                } else {
                    // index does not exist
                    throw new Exception("index " + this.index + " does not exist.");
                }
            } else if (command.toLowerCase().equals("delete")) {
                if (es.doesIndexExist(this.index)) {
                    delete(deleteField, deleteWildcard, deleteAmount);
                } else {
                    // index does not exist
                    throw new Exception("index " + this.index + " does not exist.");
                }
            }
        } else {
            System.out.println("ERROR: cannot create Elasticsearch client ... ");
        }

        return outputFile;
    }

    private long getMaxId(String index) throws Exception {
        return es.getMaxId(index, isDFS);
    }

    private boolean doesIndexExist() {
        return es.doesIndexExist(index);
    }

    private void createIndex(String indexSettings, String mappingStr) throws NoNodeAvailableException {
        if (isPrint) {
            System.out.println("INDEX," + index);
        }
        // delete the index if it exists
        if (es.doesIndexExist(index)) {
            es.deleteIndex(index);
        }
        // create index
        boolean isCreated = es.createIndex(index, type, indexSettings, mappingStr);
        if (!isCreated) {
            System.err.println("Cannot create index: " + index);
        }
    }

    public ArrayList<EvalResult> runExperiment(String indexSettings, String mappingStr, String cloneClusterFilePrefix) {
        this.cloneClusterFile = "resources/" + cloneClusterFilePrefix + "_" + this.parseMode + ".csv";
        // create the output folder
        MyUtils.createDir(outputFolder);
        // create a connector
        es = new ESConnector(server, cluster);
        ArrayList<EvalResult> resultSet = new ArrayList<>();
        // tries to delete the combination results of all settings if the file exist.
        // we're gonna generate this from the experiment.
        File allErrorMeasureResults = new File("all_" + this.errMeasure + ".csv");
        if (allErrorMeasureResults.exists()) {
            allErrorMeasureResults.delete();
        }
        try {
            es.startup();
            prepareTokenizers();
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
            t2Ngen = new nGramGenerator(t2NgramSize);
            t1Ngen = new nGramGenerator(t1NgramSize);
            totalDocuments = (int) insert(0);
            if (totalDocuments != 0) {
                // if ok, refresh the index, then search
                es.refresh(index);
                // read the index for query reduction
                readESIndex(index);
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
            if (deleteIndexAfterUse && !es.deleteIndex(index)) {
                System.err.println("Cannot delete index: " + index);
                System.exit(-1);
            }
            es.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    @SuppressWarnings("unchecked")
    private long insert(long startingId) throws Exception {
        boolean isIndexed = true;
        ArrayList<Document> docArray = new ArrayList<>();
        ArrayList<String> origDocArray = new ArrayList<>();
        File folder = new File(inputFolder);
        // Pass the  extensions array to filter the files
        List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);
        // method counter
        long count = 0;
        int fileCount = 0;
        System.out.println("Indexing Phase: found " + listOfFiles.size() + " source code files.");
        // extract the license at project level
        if (this.includeLicense) {
            this.fileLicense = extractProjectLicense();
        }
        for (File file : listOfFiles) {
            try {
                String license = "none";
                // TODO: find a way to consolidate this:
                // When doing CloPlag or SOCO evaluation, use this.
                // String filePath = file.getAbsolutePath().replace(prefixToRemove, "");
                // GitHub, use this.
                String filePath = file.getAbsolutePath();
                if (isPrint) {
                    System.out.println(fileCount + ": " + filePath);
                }
                fileCount++;
                // parse each file into method (if possible)
                MethodParser methodParser = initialiseMethodParser(file.getAbsolutePath(),
                        prefixToRemove, parseMode, isPrint);
                ArrayList<Method> methodList;
                try {
                    methodList = methodParser.parseMethods();
                    // extract license (if any)
                    if (this.includeLicense) {
                        switch (this.licenseExtractor.toLowerCase()) {
                            case "ninka":
                                license = LicenseExtractor.
                                        extractLicenseWithNinka(file.getAbsolutePath()).split(";")[1];
                                break;
                            case "regexp":
                                license = methodParser.getLicense();
                                break;
                            default:
                                license = "none";
                        }
                        // level is in the file in the root, use it if cannot find localised license
                        if ((license.equals("unknown") || license.equals("none"))
                                && !this.fileLicense.equals("unknown")) {
                            license = this.fileLicense;
                        }
                    }
                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            // check minimum size
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {
                                // Create Document object and put in an array list
                                String normSource = tokenize(method.getSrc(), tokenizer, isNgram, ngen);
                                String t2Source = tokenize(method.getSrc(), t2Tokenizer, isNgram, t2Ngen);
                                String t1Source = tokenize(method.getSrc(), t1Tokenizer, isNgram, t1Ngen);
                                String tokenizedSource = tokenize(method.getComment() + " " +
                                        method.getSrc(), origTokenizer, false, ngen);
                                String finalUrl = this.url;
                                if (!finalUrl.equals("none")) {
                                    String prefix = inputFolder;
                                    if (inputFolder.endsWith("/")) {
                                        prefix = StringUtils.chop(inputFolder);
                                    }
                                    finalUrl += filePath.replace(prefix, "");
                                }
                                Document d = new Document(
                                        startingId + count,
                                        filePath + "_" + method.getName(),
                                        method.getStartLine(),
                                        method.getEndLine(),
                                        normSource,
//                                        "", "", "",
                                        t2Source,
                                        t1Source,
                                        tokenizedSource,
//                                        method.getSrc(),
                                        "", // insert empty original code for performance now.
                                        license,
                                        finalUrl);
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
                    if (!isIndexed) {
                        throw new Exception("Cannot insert docId " + count + " in sequential mode");
                    } else {
                        docArray.clear(); // reset the array list
                    }
                } else if (this.indexingMode.equals(Settings.IndexingMode.BULK)) {
                    // index every N docs (bulk insertion mode)
                    if (docArray.size() >= this.bulkSize) {
                        isIndexed = es.bulkInsert(index, type, docArray);
                        if (!isIndexed) {
                            throw new Exception("Cannot bulk insert documents");
                        } else {
                            // reset the array list
                            docArray.clear();
                        }
                    }
                }
                if (fileCount % printEvery == 0) {
                    double percent = (double) fileCount * 100 / listOfFiles.size();
                    DecimalFormat df = new DecimalFormat("#.00");
                    System.out.println("Indexed " + fileCount
                            + " [" + df.format(percent) + "%] documents/files (" + count + " methods).");
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
                    + " [" + df.format(percent) + "%] files (" + count + " methods).");
        }
        return count;
    }

    protected String search(String inputFolder, int offset, int size,
                            boolean queryReduction, OutputFormatter formatter) throws Exception {
        String qr = "no_qr";
        if (queryReduction) {
            qr = "qr";
        }
        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-S");
        Date dateobj = new Date();
        String outfilePath = outputFolder + "/" + index + "_" + qr + "_" + df.format(dateobj);
        if (formatter.getFormat().contains("csv")) {
            outfilePath += ".csv";
        } else if (formatter.getFormat().equals("gcf")) {
            outfilePath += ".xml";
        } else if (formatter.getFormat().equals("json")) {
            outfilePath += ".json";
        } else {
            outfilePath += ".csv"; // fall back to csv
        }
        File outfile = new File(outfilePath);
        // if file doesn't exists, then create it
        boolean isCreated = false;
        if (!outfile.exists()) {
            isCreated = outfile.createNewFile();
        }
        if (isCreated) {
            FileWriter fw = new FileWriter(outfile.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            // Pass the  extensions array to filter the files
            File folder = new File(inputFolder);
            List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);
            System.out.println("Querying Phase: found " + listOfFiles.size() + " source code files.");
            long count = 0;
            long methodCount = 0;
            long search = 0;
            StringBuilder outToFile = new StringBuilder();
            for (File file : listOfFiles) {
                if (isPrint) {
                    System.out.println(count + ": " + file.getAbsolutePath());
                }
                // parse each file into methods (if possible)
                MethodParser methodParser = initialiseMethodParser(file.getAbsolutePath(),
                        prefixToRemove, parseMode, isPrint);
                ArrayList<Method> methodList;
                String t3Query = "";
                String t2Query = "";
                String t1Query = "";
                String origQuery = "";
                try {
                    methodList = methodParser.parseMethods();
                    String license = methodParser.getLicense();
                    ArrayList<Document> results;
                    // check if there's a method
                    if (methodList.size() > 0) {
                        for (Method method : methodList) {
                            methodCount++;
                            // check minimum size
                            if ((method.getEndLine() - method.getStartLine() + 1) >= minCloneLine) {
                                // write output to file
                                Document q = new Document();
                                q.setFile(method.getFile() + "_" + method.getName());
                                q.setStartline(method.getStartLine());
                                q.setEndline(method.getEndLine());
                                String queryText = formatter.format(q, prefixToRemove, license);

                                // t3Query size limit is enforced
                                if (queryReduction) {
                                    long docCount = getIndicesStats();
                                    if (enableRep[3]) {
                                        t3Query = reduceQuery(tokenizeAsArray(method.getSrc(),
                                                tokenizer, isNgram, ngen),
                                                "src", this.qrPercentileNorm * docCount / 100);
                                    }
                                    if (enableRep[2]) {
                                        t2Query = reduceQuery(tokenizeAsArray(method.getSrc(),
                                                t2Tokenizer, isNgram, t2Ngen),
                                                "t2src", this.qrPercentileT2 * docCount / 100);
                                    }
                                    if (enableRep[1]) {
                                        t1Query = reduceQuery(tokenizeAsArray(method.getSrc(),
                                                t1Tokenizer, isNgram, t1Ngen),
                                                "t1src", this.qrPercentileT1 * docCount / 100);
                                    }
                                    if (enableRep[0]) {
                                        origQuery = reduceQuery(tokenizeAsArray(method.getComment() +
                                                        " " + method.getSrc(), origTokenizer, false, ngen),
                                                "tokenizedsrc", this.qrPercentileOrig * docCount / 100);
                                    }
                                    if (isPrint) {
                                        System.out.println(methodCount + " T3Q " +
                                                this.qrPercentileNorm * docCount / 100 + "," + t3Query + "\n");
                                        System.out.println(methodCount + " T2Q " +
                                                this.qrPercentileT2 * docCount / 100 + "," + t2Query + "\n");
                                        System.out.println(methodCount + " T1Q " +
                                                this.qrPercentileT1 * docCount / 100 + "," + t1Query + "\n");
                                        System.out.println(methodCount + " T0Q " +
                                                this.qrPercentileOrig * docCount / 100 + "," + origQuery);
                                        System.out.println("-------------------------------------------");
                                    }
                                } else {
                                    if (enableRep[3]) {
                                        t3Query = tokenize(method.getSrc(), tokenizer, isNgram, ngen);
                                    }
                                    if (enableRep[2]) {
                                        t2Query = tokenize(method.getSrc(), t2Tokenizer, isNgram, t2Ngen);
                                    }
                                    if (enableRep[1]) {
                                        t1Query = tokenize(method.getSrc(), t1Tokenizer, isNgram, t1Ngen);
                                    }
                                    if (enableRep[0]) {
                                        origQuery = tokenize(method.getComment() + " " + method.getSrc(),
                                                origTokenizer, false, ngen);
                                    }
                                }

                                // search for results depending on the MR setting
                                if (this.multiRep) {
                                    results = es.search(index, type, origQuery, t3Query, t2Query, t1Query,
                                            origBoost, normBoost, t2Boost, t1Boost, isPrint, isDFS, offset,
                                            size, this.computeSimilarity, simThreshold);
                                } else {
                                    System.out.println("QUERY: " + methodCount + "\n" + origQuery);
                                    results = es.search(index, type, origQuery, isPrint, isDFS, offset, size);
                                }
                                // fuzzywuzzy similarity is applied after the search
                                if (this.computeSimilarity.equals("fuzzywuzzy")) {
//                                    int[] sim = computeSimilarityOneRep(origQuery, results);
//                                    outToFile.append(formatter.format(results, sim, this.simThreshold, prefixToRemove));
                                    // TODO: only for the thesis, put this back after the experiment.
                                    int[][] sim = computeSimilarity(origQuery, t1Query, t2Query, t3Query, results);
                                    outToFile.append(formatter.format(results, sim, this.simThreshold,
                                            prefixToRemove, ignoreQueryClones, q, queryText));
                                } else {
                                    outToFile.append(formatter.format(results, prefixToRemove,
                                            ignoreQueryClones, q, queryText));
                                }
                                search++;
                            } else {
                                if (isPrint) {
                                    System.out.println("Not searched (smaller than the threshold of " +
                                            minCloneLine + " lines): " + method.getFullyQualifiedMethodName() +
                                            ": " + method.getFile());
                                }
                            }
                        }
                    }
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                if (count % printEvery == 0) {
                    double percent = (double) count * 100 / listOfFiles.size();
                    DecimalFormat percentFormat = new DecimalFormat("#.00");
                    System.out.println("Searched " + search + " code fragments ["
                            + percentFormat.format(percent) + "%] (from " + count
                            + " files/" + methodCount + " methods).");
                    if (formatter.getFormat().equals("gcf")) {
                        outToFile.replace(0, outToFile.length(), formatter.getXML());
                    } else if (formatter.getFormat().equals("json")) {
                        outToFile.replace(0, outToFile.length(), formatter.getJSON());
                    }
                    bw.write(outToFile.toString());
                    // reset the output to print
                    outToFile.setLength(0);
                }
            }
            // flush the last part of output
            if (formatter.getFormat().equals("gcf")) {
                outToFile.replace(0, outToFile.length(), formatter.getXML());
            } else if (formatter.getFormat().equals("json")) {
                outToFile.replace(0, outToFile.length(), formatter.getJSON());
            }
            bw.write(outToFile.toString());
            bw.close();
            System.out.println("Searching done for " + count + " files (" +
                    search + " out of " + methodCount + " methods after clone size filtering).");
            System.out.println("See output at " + outfile.getAbsolutePath());
        } else {
            throw new IOException("Cannot create the output file: " + outfile.getAbsolutePath());
        }
        return outfile.getAbsolutePath();
    }

    /**
     * Compute similarity between query and results using fuzzywuzzy string matching
     *
     * @param query the f0 code query (original)
     * @return a 2D array of similarity values
     */
    private int[] computeSimilarityOneRep(String query, ArrayList<Document> results) {
        int[] simResults = new int[results.size()]; // 2D sim array of four representations
        for (int i = 0; i < results.size(); i++) {
            Document d = results.get(i);
            int sim0 = FuzzySearch.tokenSetRatio(query, d.getTokenizedSource());
            // compute an average similarity of the four representations
            simResults[i] = sim0;
        }
        return simResults;
    }

    /**
     * Compute similarity between query and results using fuzzywuzzy string matching
     *
     * @param query   the f0 code query (original)
     * @param t1Query the f1 code query
     * @param t2Query the f2 code query
     * @param t3Query the f3 code query
     * @param results the list of results
     * @return a 2D array of similarity values
     */
    private int[][] computeSimilarity(String query,
                                      String t1Query, String t2Query, String t3Query,
                                      ArrayList<Document> results) {
        int[][] simResults = new int[results.size()][4]; // 2D sim array of four representations
        for (int i = 0; i < results.size(); i++) {
            Document d = results.get(i);
            int sim0 = FuzzySearch.tokenSetRatio(query, d.getTokenizedSource());
            int sim1 = FuzzySearch.tokenSetRatio(t1Query, d.getT1Source());
            int sim2 = FuzzySearch.tokenSetRatio(t2Query, d.getT2Source());
            int sim3 = FuzzySearch.tokenSetRatio(t3Query, d.getSource());
            // compute an average similarity of the four representations
            int[] sims = {sim0, sim1, sim2, sim3};
            simResults[i] = sims;
        }
        return simResults;
    }

    /**
     * Reduce number of tokens in the query
     *
     * @param query the query
     * @param field index field to analyse
     * @param limit the maximum number of terms in the reduced query
     * @return the reduced query
     */
    private String reduceQuery(ArrayList<String> query, String field, double limit) {
        // find the top-N rare terms in the query
        // clear the query
        StringJoiner queryStr = new StringJoiner(" ");
        ArrayList<JavaTerm> sortedTerms = sortTermsByFreq(index, field, query);
        for (JavaTerm sortedTerm : sortedTerms) {
            if (sortedTerm.getFreq() <= limit) {
                queryStr.add(sortedTerm.getTerm());
            }
        }
        return queryStr.toString();
    }


    /***
     * Evaluate the search results by either r-precision or mean average precision (MAP)
     * @param mode parameter settings
     * @param workingDir location of the results
     * @param errMeasure type of error measure
     * @return A pair of the best performance (either ARP or MAP) and its value
     */
    private EvalResult evaluate(String mode, String workingDir, String errMeasure,
                                boolean queryReduction, boolean isPrint) throws Exception {

        // default is method-level evaluator
        Evaluator evaluator = new MethodLevelEvaluator(this.cloneClusterFile, mode, workingDir, isPrint);

        // if file-level is specified, switch to file-level evaluator
        if (parseMode.equals(Settings.MethodParserType.FILE)) {
            evaluator = new FileLevelEvaluator(this.cloneClusterFile, mode, workingDir, isPrint);
        }

        // generate a search key and retrieve result size (if MAP)
        int searchKeySize = evaluator.generateSearchKey();
        EvalResult result = new EvalResult();
        String outputFile;

        // get the output formatter according to the settings
        OutputFormatter formatter = getOutputFormatter();

        if (errMeasure.equals(Settings.ErrorMeasure.ARP)) {
            outputFile = search(inputFolder, resultOffset, resultsSize, queryReduction, formatter);
            double arp = evaluator.evaluateARP(outputFile, resultsSize);
            if (isPrint) {
                System.out.println(Settings.ErrorMeasure.ARP + ": " + arp);
            }
            // update the max ARP value
            if (result.getValue() < arp) {
                result.setValue(arp);
                result.setSetting(outputFile);
            }
            deleteOutputFile(outputFile);
        } else if (errMeasure.equals(Settings.ErrorMeasure.MAP)) {
            outputFile = search(inputFolder, resultOffset, totalDocuments, queryReduction, formatter);
            double map = evaluator.evaluateMAP(outputFile, totalDocuments);
            if (isPrint) {
                System.out.println(Settings.ErrorMeasure.MAP + ": " + map);
            }
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

    private String tokenize(String sourcecode, Tokenizer tokenizer,
                            boolean isNgram, nGramGenerator ngen) throws Exception {
        // generate tokens
        ArrayList<String> tokens = tokenizer.getTokensFromString(sourcecode);
        // enter ngram mode
        if (isNgram) {
            return printArray(ngen.generateNGramsFromJavaTokens(tokens), false);
        } else {
            return printArray(tokens, false);
        }
    }

    private ArrayList<String> tokenizeAsArray(String sourcecode, Tokenizer tokenizer,
                                              boolean isNgram, nGramGenerator ngen) throws Exception {
        // generate tokens
        ArrayList<String> tokens = tokenizer.getTokensFromString(sourcecode);
        if (isNgram) {
            return ngen.generateNGramsFromJavaTokens(tokens);
        } else {
            return tokens;
        }
    }

    public long getIndicesStats() {
        return es.getIndicesStats(this.index);
    }

    /**
     * Read the ES index file
     *
     * @param indexName the name of the index
     */
    private void readESIndex(String indexName) {
        String indexFile = elasticsearchLoc + "/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        try {
            esIndexRader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
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
    private ArrayList<JavaTerm> sortTermsByFreq(String indexName, String field, ArrayList<String> terms) {
        ArrayList<JavaTerm> selectedTermsArray = new ArrayList<>();
        try {
            for (String term : terms) {
                // TODO: get rid of the blank term (why it's blank?)
                if (!term.isEmpty()) {
                    Term t = new Term(field, term);
                    int freq = esIndexRader.docFreq(t);
                    JavaTerm newTerm = new JavaTerm(term, freq);
                    if (!selectedTermsArray.contains(newTerm))
                        selectedTermsArray.add(newTerm);
                }
            }
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
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (String anArr : arr) {
            if (pretty && anArr.equals("\n")) {
                System.out.print(anArr);
                continue;
            }
            stringJoiner.add(anArr);
        }
        return stringJoiner.toString();
    }

    private String escapeString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("/", "\\/")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private MethodParser initialiseMethodParser(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        MethodParser parser = null;
        try {
            Class cl = Class.forName(this.methodParserName);
            parser = (MethodParser) cl.newInstance();
            parser.configure(filePath, prefixToRemove, mode, isPrint);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR: could not find the specified method parser: " +
                    this.methodParserName + ". Please check if the class and package name is correct.");
        }
        return parser;
    }

    private Tokenizer initialiseTokenizer(Normalizer normalizer) {
        Tokenizer tokenizer = null;
        try {
            Class cl = Class.forName(this.tokenizerName);
            tokenizer = (Tokenizer) cl.newInstance();
            tokenizer.configure(normalizer);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR: could not find the specified tokenizer: " +
                    this.tokenizerName + ". Please check if the class and package name is correct.");
        }
        return tokenizer;
    }

    private Normalizer initialiseNormalizer(NormalizerMode modes) {
        Normalizer normalizer = null;
        try {
            Class cl = Class.forName(this.normalizerName);
            normalizer = (Normalizer) cl.newInstance();
            normalizer.configure(modes);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR: could not find the specified normalizer: " +
                    this.normalizerName + ". Please check if the class and package name is correct.");
        }
        return normalizer;
    }

    private NormalizerMode initialiseNormalizerMode() {
        NormalizerMode normalizerMode = null;
        try {
            Class cl = Class.forName(this.normalizerModeName);
            normalizerMode = (NormalizerMode) cl.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR: could not find the specified normalizer mode: " +
                    this.normalizerModeName + ". Please check if the class and package name is correct.");
        }
        return normalizerMode;
    }

    private NormalizerMode initialiseNormalizerMode(char[] normOptions) {
        NormalizerMode normalizerMode = null;
        try {
            Class cl = Class.forName(this.normalizerModeName);
            normalizerMode = (NormalizerMode) cl.newInstance();
            normalizerMode.configure(normOptions);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR: could not find the specified normalizer mode: " +
                    this.normalizerModeName + ". Please check if the class and package name is correct.");
        }
        return normalizerMode;
    }

    public String extractProjectLicense() {
        File f = new File(this.inputFolder + "/LICENSE.txt");
        if (!f.exists() || f.isDirectory()) {
            f = new File(this.inputFolder + "/LICENSE");
        } else if (!f.exists() || f.isDirectory()) {
            f = new File(this.inputFolder + "/LICENSE.md");
        } else if (!f.exists() || f.isDirectory()) {
            f = new File(this.inputFolder + "/license.txt");
        } else if (!f.exists() || f.isDirectory()) {
            f = new File(this.inputFolder + "/COPYING");
        } else if (!f.exists() || f.isDirectory()) {
            f = new File(this.inputFolder + "/COPYING.LESSER");
        }

        String license = "none";
        StringJoiner licenseStrJoiner = new StringJoiner(" ");
        try {
            if (f.exists() && !f.isDirectory()) {
                String[] lines = FileUtils.readFileToString(f).split("\n");
                // concat the license string into one single line
                for (String line : lines) {
                    licenseStrJoiner.add(line);
                }
                String licenseStr = licenseStrJoiner.toString();
                licenseStr = licenseStr.trim().replaceAll("\\s+", " ");
                license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);

                if (!license.equals("unknown")) {
                    this.fileLicense = license;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: cannot read the license file.");
        }
        return license;
    }

    private void genGitHubInfo() {
        String[] inputPath = this.inputFolder.split("/");
        String projName = inputPath[inputPath.length - 2] + "/" + inputPath[inputPath.length - 1];
        this.url = "https://github.com/" + projName + "/blob/master";
    }

    public void setIsPrint(boolean isPrint) {
        this.isPrint = isPrint;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public void setResultOffset(int resultOffset) {
        this.resultOffset = resultOffset;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }

    public String getComputeSimilarity() {
        return this.computeSimilarity;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
