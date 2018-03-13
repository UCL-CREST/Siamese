package crest.siamese.experiment;

import crest.siamese.helpers.EvalResult;
import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;
import crest.siamese.settings.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Experiment {

    public static String prefixToRemove = "";
    private static String[] normModes = {};
    private static int[] ngramSizes = {};

    // TODO: I removed the "x", can put it back in if needed.
    private static String[] normModesAll = {
            "w", "v", "vw", "s", "sw", "sv", "svw", "p", "pw",
            "pv", "pvw", "ps", "psw", "psv", "psvw", "k", "kw", "kv", "kvw",
            "ks", "ksw", "ksv", "ksvw", "kp", "kpw", "kpv", "kpvw", "kps", "kpsw",
            "kpsv", "kpsvw", "j", "jw", "jv", "jvw", "js", "jsw", "jsv", "jsvw",
            "jp", "jpw", "jpv", "jpvw", "jps", "jpsw", "jpsv", "jpsvw", "jk", "jkw",
            "jkv", "jkvw", "jks", "jksw", "jksv", "jksvw", "jkp", "jkpw", "jkpv", "jkpvw",
            "jkps", "jkpsw", "jkpsv", "jkpsvw", "d", "dw", "dv", "dvw", "ds", "dsw",
            "dsv", "dsvw", "dp", "dpw", "dpv", "dpvw", "dps", "dpsw", "dpsv", "dpsvw",
            "dk", "dkw", "dkv", "dkvw", "dks", "dksw", "dksv", "dksvw", "dkp", "dkpw",
            "dkpv", "dkpvw", "dkps", "dkpsw", "dkpsv", "dkpsvw", "dj", "djw", "djv", "djvw",
            "djs", "djsw", "djsv", "djsvw", "djp", "djpw", "djpv", "djpvw", "djps", "djpsw",
            "djpsv", "djpsvw", "djk", "djkw", "djkv", "djkvw", "djks", "djksw", "djksv", "djksvw",
            "djkp", "djkpw", "djkpv", "djkpvw", "djkps", "djkpsw", "djkpsv", "djkpsvw"};
    private static String[] normModesText = { "x" };
    private static String[] normModesDefault = { "djkpsvw" };
    private static int[] ngramSizesAll = { 1, 2, 3, 4, 5 };
    private static int[] ngramSizesText = { 1 };
    private static int[] ngramSizeDefault = { 15 };

//    private static double[] dfCapNorm = { 10, 20, 40 };
//    private static double[] dfCapOrig = { 20, 40, 60, 80 };
    private static double[] dfCapNorm = { 5 };
    private static double[] dfCapOrig = { 20 };

    private static String inputDir;
    private static String workingDir;
    private static String configFile;
    private static String errMeasure;
    private static String mode;
    private static boolean queryReduction;
    private static String cloneClusterFile;
    private static boolean deleteIndexAfterUse;
    private static String cloneClusterFilePrefix = "clone_clusters";

    public static void main(String[] args) {

        configFile = "config_cloplag.properties";
        readFromConfigFile(configFile);
        cloneClusterFilePrefix = cloneClusterFile + "_" + cloneClusterFilePrefix;

        if (mode.endsWith("_text")) {
            normModes = normModesText;
            ngramSizes = ngramSizesText;
        } else if (mode.endsWith("_ngram")) {
            normModes = normModesText;
            ngramSizes = ngramSizesAll;
        } else if (mode.endsWith("_codenorm")) {
            normModes = normModesAll;
            ngramSizes = ngramSizesText;
        } else if (mode.endsWith("_both")) {
            normModes = normModesAll;
            ngramSizes = ngramSizesAll;
        } else if (mode.endsWith("_def")) {
            normModes = normModesDefault;
            ngramSizes = ngramSizeDefault;
        } else {
            normModes = normModesAll;
            ngramSizes = ngramSizesAll;
        }

        ArrayList<EvalResult> bestResults = new ArrayList<>();

        switch (mode) {
            case "tfidf_text":
            case "tfidf_text_ngram":
            case "tfidf_text_codenorm":
            case "tfidf_text_def":
            case "tfidf_text_both":
                bestResults = tfidfTextExp();
                break;
//            case "bm25_text":
//            case "bm25_text_ngram":
//            case "bm25_text_codenorm":
//            case "bm25_text_both":
//                bestResults = bm25TextExp();
//                break;
//            case "dfr_text":
//            case "dfr_text_ngram":
//            case "dfr_text_codenorm":
//            case "dfr_text_both":
//                bestResults = dfrTextExp();
//                break;
//            case "ib_text":
//            case "ib_text_ngram":
//            case "ib_text_codenorm":
//            case "ib_text_both":
//                bestResults = ibTextExp();
//                break;
//            case "lmd_text":
//            case "lmd_text_ngram":
//            case "lmd_text_codenorm":
//            case "lmd_text_both":
//                bestResults = lmdTextExp();
//                break;
//            case "lmj_text":
//            case "lmj_text_ngram":
//            case "lmj_text_codenorm":
//            case "lmj_text_both":
//                bestResults = lmjTextExp();
//                break;
//            case "tfidf": /* normal mode (search all parameters + grams + normalisation) */
//                bestResults = tfidfExp();
//                break;
//            case "bm25":
//                bestResults = bm25Exp();
//                break;
//            case "dfr":
//                bestResults = dfrExp();
//                break;
//            case "ib":
//                bestResults = ibExp();
//                break;
//            case "lmdirichlet":
//            case "lmd":
//                bestResults = lmdExp();
//                break;
//            case "lmjelinekmercer":
//            case "lmj":
//                bestResults = lmjExp();
//                break;
            default:
                System.out.println("No ranking function found");
        }

        System.out.println("best " + errMeasure.toLowerCase()
                + " = " + bestResults.get(0).getSetting() + "," + bestResults.get(0).getValue());

        String qr = "no_qr";
        if (!queryReduction) {
            System.out.println("No query reduction");
        } else {
            System.out.println("Query reduction enabled");
            qr = "qr";
        }

        MyUtils.writeToFile(workingDir, "report_" + errMeasure.toLowerCase() + "_" + mode + "_" + qr + ".txt",
                formatResults(bestResults),
                false);
    }

    private static void readFromConfigFile(String configFile) {
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
            inputDir = prop.getProperty("inputFolder");
            workingDir = prop.getProperty("outputFolder");

            mode = prop.getProperty("similarityMode");
            cloneClusterFile = prop.getProperty("cloneClusterFile");

            String errMeasureConfig = prop.getProperty("errorMeasure");
            if (errMeasureConfig.equals("arp"))
                errMeasure = Settings.ErrorMeasure.ARP;
            else
                errMeasure = Settings.ErrorMeasure.MAP;

            deleteIndexAfterUse = Boolean.parseBoolean(prop.getProperty("deleteIndexAfterUse"));
            queryReduction = Boolean.parseBoolean(prop.getProperty("queryReduction"));
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

    private static String formatResults(ArrayList<EvalResult> bestResults) {
        String output = "Best result: ";
        output += bestResults.get(0).getSetting() + "," + bestResults.get(0).getValue() + "\n";
        output += "\nAll results:\n";
        for (int i=1; i<bestResults.size(); i++) {
            output += bestResults.get(i).getSetting() + "," + bestResults.get(i).getValue() + "\n";
        }

        return output;
    }

    private static ArrayList<EvalResult> tfidfTextExp() {
        String discO = "true";
        Siamese siamese = new Siamese(configFile);
        String indexSettings = "";

        if (!discO.equals("false"))
            indexSettings = "{ \"number_of_shards\": 1, " +
                    "\"similarity\": { \"tfidf_similarity\": " +
                    "{ \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                    "\"analysis\": { " +
                    "\"analyzer\": { " +
                    "\"default\": { " +
                    "\"type\": \"whitespace\"" +
                    "} } } }";
        else {
            indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
        }

        String mappingStr = "{ \"properties\": { \"src\": " +
                "{ \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";

        return siamese.runExperiment(
                indexSettings,
                mappingStr,
                normModes,
                ngramSizes,
                dfCapNorm,
                dfCapOrig,
                cloneClusterFilePrefix);
    }


//    private static ArrayList<EvalResult> tfidfExp() {
//
//        String[] discountOverlap = {"no", "true", "false"};
//        Siamese siamese = new Siamese(configFile);
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        for (String discO : discountOverlap) {
//
//            String indexSettings = "";
//            if (!discO.equals("no"))
//                indexSettings = "{ \"number_of_shards\": 1, " +
//                        "\"similarity\": { \"tfidf_similarity\": " +
//                        "{ \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
//                        "\"analysis\": { " +
//                        "\"analyzer\": { " +
//                        "\"default\": { " +
//                        "\"type\": \"whitespace\"" +
//                        "} } } }";
//            else {
//                indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
//            }
//
//            String mappingStr = "{ \"properties\": { \"src\": " +
//                    "{ \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
//
//            ArrayList<EvalResult> result = siamese.runExperiment(indexSettings, mappingStr, normModes, ngramSizes, dfCapNorm, dfCapOrig, boost, cloneClusterFilePrefix);
//
//            if (result.get(0).getValue() > bestResult.get(0).getValue()) {
//                bestResult = result;
//            }
//        }
//
//        return bestResult;
//    }
//
//    private static ArrayList<EvalResult> bm25TextExp() {
//        double k1 = 1.2;
//        double b = 0.75;
//        String discO = "true";
//        Siamese siamese = new Siamese(configFile);
//
//        String indexSettings = "{ \"number_of_shards\": 1, " +
//                "\"similarity\": "
//                + "{ \"bm25_similarity\": " +
//                "{ \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
//                + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
//
//        String mappingStr = "{ \"properties\": { \"src\": " +
//                "{ \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
//
//        return siamese.runExperiment(
//                indexSettings,
//                mappingStr,
//                normModes,
//                ngramSizes,
//                dfCapNorm,
//                dfCapOrig,
//                boost,
//                cloneClusterFilePrefix);
//    }
//
//    private static ArrayList<EvalResult> bm25Exp() {
//
//        String[] k1s = {"0.0", "0.6", "1.2", "1.8", "2.4"};
//        String[] bs = {"0.0", "0.25", "0.50", "0.75", "1.00"};
//        String[] discountOverlaps = {"true", "false"};
//
//        Siamese siamese = new Siamese(configFile);
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        for (String k1 : k1s) {
//            for (String b : bs) {
//                for (String discO : discountOverlaps) {
//                    String indexSettings = "{ \"number_of_shards\": 1," +
//                            "\"similarity\": "
//                            + "{ \"bm25_similarity\": " +
//                            "{ \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
//                            + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
//                    String mappingStr = "{ \"properties\": { \"src\": " +
//                            "{ \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
//                    System.out.println(indexSettings);
//                    ArrayList<EvalResult> result = siamese.runExperiment(
//                            indexSettings,
//                            mappingStr,
//                            normModes,
//                            ngramSizes,
//                            dfCapNorm,
//                            dfCapOrig,
//                            boost,
//                            cloneClusterFilePrefix);
//
//                    if (result.get(0).getValue() > bestResult.get(0).getValue()) {
//                        bestResult = result;
//                    }
//                }
//            }
//        }
//
//        return bestResult;
//    }
//
//    private static ArrayList<EvalResult> dfrTextExp() {
//        String bm = "be";
//        String ae = "b";
//        String norm = "h1";
//
//        Siamese siamese = new Siamese(configFile);
//
//        String indexSettings = "{ \"number_of_shards\": 1," +
//                "\"similarity\": { \"dfr_similarity\" : " +
//                "{ \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
//                + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
//                + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";
//
//        String mappingStr = "{ \"properties\": { \"src\": " +
//                "{ \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
//
//        return siamese.runExperiment(
//                indexSettings,
//                mappingStr,
//                normModes,
//                ngramSizes,
//                dfCapNorm,
//                dfCapOrig,
//                boost,
//                cloneClusterFilePrefix);
//
//    }
//
//    private static ArrayList<EvalResult> dfrExp() {
//        String[] basicModelArr = {"be", "d", "g", "if", "in", "ine", "p"};
//        String[] afterEffectArr = {"no", "b", "l"};
//        String[] dfrNormalizationArr = {"no", "h1", "h2", "h3", "z"};
//
//        Siamese siamese = new Siamese(configFile);
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        for (String bm : basicModelArr) {
//            for (String ae : afterEffectArr) {
//                for (String norm : dfrNormalizationArr) {
//                    String indexSettings = "{ \"number_of_shards\": 1," +
//                            "\"similarity\": { \"dfr_similarity\" : " +
//                            "{ \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
//                            + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
//                            + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";
//
//                    String mappingStr = "{ \"properties\": { \"src\": " +
//                            "{ \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
//
//                    ArrayList<EvalResult> result = siamese.runExperiment(
//                            indexSettings,
//                            mappingStr,
//                            normModes,
//                            ngramSizes,
//                            dfCapNorm,
//                            dfCapOrig,
//                            boost,
//                            cloneClusterFilePrefix);
//
//                    if (result.get(0).getValue() > bestResult.get(0).getValue()) {
//                        bestResult = result;
//                    }
//                }
//            }
//        }
//        return bestResult;
//    }
//
//    private static ArrayList<EvalResult> ibTextExp() {
//        String dist = "ll";
//        String lamb = "df";
//        String ibNorm = "h1";
//
//        Siamese siamese = new Siamese(configFile);
//
//        String indexSettings = "{ \"number_of_shards\": 1," +
//                "\"similarity\": "
//                + "{ \"ib_similarity\" : "
//                + "{ \"type\": \"IB\", "
//                + "\"distribution\": \"" + dist + "\", "
//                + "\"lambda\": \"" + lamb + "\", "
//                + "\"normalization\": \"" + ibNorm + "\""
//                + "} "
//                + "},  "
//                + "\"analysis\" : { \"analyzer\" : "
//                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//        String mappingStr = "{ \"properties\": { \"src\": " +
//                "{ \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
//
//        return siamese.runExperiment(
//                indexSettings,
//                mappingStr,
//                normModes,
//                ngramSizes,
//                dfCapNorm,
//                dfCapOrig,
//                boost,
//                cloneClusterFilePrefix);
//
//    }
//
//    private static ArrayList<EvalResult> ibExp() {
//        String[] distributions = {"ll", "spl"};
//        String[] lambdas = {"df", "ttf"};
//        String[] ibNormalizationArr = {"no", "h1", "h2", "h3", "z"};
//
//        Siamese siamese = new Siamese(configFile);
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        for (String dist : distributions) {
//            for (String lamb : lambdas) {
//                for (String ibNorm : ibNormalizationArr) {
//                    String indexSettings = "{ \"number_of_shards\": 1," +
//                            "\"similarity\": "
//                            + "{ \"ib_similarity\" : "
//                            + "{ \"type\": \"IB\", "
//                            + "\"distribution\": \"" + dist + "\", "
//                            + "\"lambda\": \"" + lamb + "\", "
//                            + "\"normalization\": \"" + ibNorm + "\""
//                            + "} "
//                            + "},  "
//                            + "\"analysis\" : { \"analyzer\" : "
//                            + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//                    String mappingStr = "{ \"properties\": { \"src\": " +
//                            "{ \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
//
//                    ArrayList<EvalResult> result = siamese.runExperiment(
//                            indexSettings,
//                            mappingStr,
//                            normModes,
//                            ngramSizes,
//                            dfCapNorm,
//                            dfCapOrig,
//                            boost,
//                            cloneClusterFilePrefix);
//
//                    if (result.get(0).getValue() > bestResult.get(0).getValue()) {
//                        bestResult = result;
//                    }
//                }
//            }
//        }
//        return bestResult;
//    }
//
//    private static ArrayList<EvalResult> lmdTextExp() {
//        String mu = "2000";
//        Siamese siamese = new Siamese(configFile);
//        String indexSettings = "{ \"number_of_shards\": 1," +
//                "\"similarity\": "
//                + "{ \"lmd_similarity\" : "
//                + "{ \"type\": \"LMDirichlet\", "
//                + "\"mu\": \"" + mu + "\""
//                + "} "
//                + "}, "
//                + "\"analysis\" : { \"analyzer\" : "
//                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//
//        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
//        return siamese.runExperiment(
//                indexSettings,
//                mappingStr,
//                normModes,
//                ngramSizes,
//                dfCapNorm,
//                dfCapOrig,
//                boost,
//                cloneClusterFilePrefix);
//    }
//
//    private static ArrayList<EvalResult> lmdExp() {
//        String[] mus = {"500", "1000", "1500",
//                "2000", "2500", "3000"};
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        Siamese siamese = new Siamese(configFile);
//        for (String mu : mus) {
//            String indexSettings = "{ \"number_of_shards\": 1," +
//                    "\"similarity\": "
//                    + "{ \"lmd_similarity\" : "
//                    + "{ \"type\": \"LMDirichlet\", "
//                    + "\"mu\": \"" + mu + "\""
//                    + "} "
//                    + "}, "
//                    + "\"analysis\" : { \"analyzer\" : "
//                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//            // System.out.println(indexSettings);
//
//            String mappingStr = "{ \"properties\": { \"src\": " +
//                    "{ \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
//            ArrayList<EvalResult> result = siamese.runExperiment(
//                    indexSettings,
//                    mappingStr,
//                    normModes,
//                    ngramSizes,
//                    dfCapNorm,
//                    dfCapOrig,
//                    boost,
//                    cloneClusterFilePrefix);
//
//            if (result.get(0).getValue() > bestResult.get(0).getValue()) {
//                bestResult = result;
//            }
//        }
//        return bestResult;
//    }
//
//    private static ArrayList<EvalResult> lmjTextExp() {
//
//        String lambda = "0.1";
//        Siamese siamese = new Siamese(configFile);
//        String indexSettings = "{ \"number_of_shards\": 1," +
//                "\"similarity\": "
//                + "{ \"lmj_similarity\" : "
//                + "{ \"type\": \"LMJelinekMercer\", "
//                + "\"lambda\": \"" + lambda + "\""
//                + "} "
//                + "}, "
//                + "\"analysis\" : { \"analyzer\" : "
//                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//
//        String mappingStr = "{ \"properties\": { \"src\": " +
//                "{ \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
//
//        return siamese.runExperiment(
//                indexSettings,
//                mappingStr,
//                normModes,
//                ngramSizes,
//                dfCapNorm,
//                dfCapOrig,
//                boost,
//                cloneClusterFilePrefix);
//    }
//
//    private static ArrayList<EvalResult> lmjExp() {
//
//        String[] lambdas = { "0.1", "0.2", "0.3", "0.4", "0.5",
//                "0.6", "0.7", "0.8", "0.9", "1.0" };
//        Siamese siamese = new Siamese(configFile);
//        ArrayList<EvalResult> bestResult = new ArrayList<>();
//
//        for (String lambda : lambdas) {
//            String indexSettings = "{ \"number_of_shards\": 1," +
//                    "\"similarity\": "
//                    + "{ \"lmj_similarity\" : "
//                    + "{ \"type\": \"LMJelinekMercer\", "
//                    + "\"lambda\": \"" + lambda + "\""
//                    + "} "
//                    + "}, "
//                    + "\"analysis\" : { \"analyzer\" : "
//                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
//
//            String mappingStr = "{ \"properties\": { \"src\": " +
//                    "{ \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
//
//            ArrayList<EvalResult> result = siamese.runExperiment(
//                    indexSettings,
//                    mappingStr,
//                    normModes,
//                    ngramSizes,
//                    dfCapNorm,
//                    dfCapOrig,
//                    boost,
//                    cloneClusterFilePrefix);
//
//            if (result.get(0).getValue() > bestResult.get(0).getValue())
//                bestResult = result;
//        }
//
//        return bestResult;
//    }

	/* New similarity (does not work in the current Elasticsearch version */
    /*
    public static String dfiTextExp(String inputDir, String workingDir, boolean isPrint) {
        String ind = "standardized";
        String[] normModes = { "x" };
        int[] ngramSizes = { 1 };

        Main checker = new Main();

        String indexSettings = "{ \"similarity\": "
                + "{ \"dfi_similarity\" : "
                + "{ \"type\": \"DFI\", "
                + "\"distribution\": \"" + ind + "\""
                + "} "
                + "},  "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfi_similarity\" } } } } }";
        // System.out.println(indexSettings);
        return checker.runExperiment("localhost",
                "dfi_" + ind, "doc",
                inputDir, normModes, ngramSizes, true, true,
                workingDir, false, indexSettings, mappingStr, isPrint);

    }
    */
}
