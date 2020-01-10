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

package crest.siamese.experiment;

import crest.siamese.Siamese;
import crest.siamese.helpers.EvalResult;
import crest.siamese.helpers.MyUtils;
import crest.siamese.settings.Settings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Experiment {

    public static String prefixToRemove = "";
    private static String[] normModes = {};
    private static int[] ngramSizes = {};

    // The "x" is removed. We can put it back in if needed.
    private static String[] normModesAll = {
            "d", "j", "jd", "jk", "jkd", "jko", "jkod", "jo", "jod", "jp", "jpd", "jpk", "jpkd", "jpko", "jpkod",
            "jpo", "jpod", "jps", "jpsd", "jpsk", "jpskd", "jpsko", "jpskod", "jpso", "jpsod", "jpv", "jpvd", "jpvk",
            "jpvkd", "jpvko", "jpvkod", "jpvo", "jpvod", "jpvs", "jpvsd", "jpvsk", "jpvskd", "jpvsko", "jpvskod",
            "jpvso", "jpvsod", "jpw", "jpwd", "jpwk", "jpwkd", "jpwko", "jpwkod", "jpwo", "jpwod", "jpws", "jpwsd",
            "jpwsk", "jpwskd", "jpwsko", "jpwskod", "jpwso", "jpwsod", "jpwv", "jpwvd", "jpwvk", "jpwvkd", "jpwvko",
            "jpwvkod", "jpwvo", "jpwvod", "jpwvs", "jpwvsd", "jpwvsk", "jpwvskd", "jpwvsko", "jpwvskod", "jpwvso",
            "jpwvsod", "js", "jsd", "jsk", "jskd", "jsko", "jskod", "jso", "jsod", "jv", "jvd", "jvk", "jvkd", "jvko",
            "jvkod", "jvo", "jvod", "jvs", "jvsd", "jvsk", "jvskd", "jvsko", "jvskod", "jvso", "jvsod", "jw", "jwd",
            "jwk", "jwkd", "jwko", "jwkod", "jwo", "jwod", "jws", "jwsd", "jwsk", "jwskd", "jwsko", "jwskod", "jwso",
            "jwsod", "jwv", "jwvd", "jwvk", "jwvkd", "jwvko", "jwvkod", "jwvo", "jwvod", "jwvs", "jwvsd", "jwvsk",
            "jwvskd", "jwvsko", "jwvskod", "jwvso", "jwvsod", "k", "kd", "ko", "kod", "o", "od", "p", "pd", "pk",
            "pkd", "pko", "pkod", "po", "pod", "ps", "psd", "psk", "pskd", "psko", "pskod", "pso", "psod", "pv",
            "pvd", "pvk", "pvkd", "pvko", "pvkod", "pvo", "pvod", "pvs", "pvsd", "pvsk", "pvskd", "pvsko", "pvskod",
            "pvso", "pvsod", "pw", "pwd", "pwk", "pwkd", "pwko", "pwkod", "pwo", "pwod", "pws", "pwsd", "pwsk",
            "pwskd", "pwsko", "pwskod", "pwso", "pwsod", "pwv", "pwvd", "pwvk", "pwvkd", "pwvko", "pwvkod", "pwvo",
            "pwvod", "pwvs", "pwvsd", "pwvsk", "pwvskd", "pwvsko", "pwvskod", "pwvso", "pwvsod", "s", "sd", "sk",
            "skd", "sko", "skod", "so", "sod", "v", "vd", "vk", "vkd", "vko", "vkod", "vo", "vod", "vs", "vsd",
            "vsk", "vskd", "vsko", "vskod", "vso", "vsod", "w", "wd", "wk", "wkd", "wko", "wkod", "wo", "wod",
            "ws", "wsd", "wsk", "wskd", "wsko", "wskod", "wso", "wsod", "wv", "wvd", "wvk", "wvkd", "wvko", "wvkod",
            "wvo", "wvod", "wvs", "wvsd", "wvsk", "wvskd", "wvsko", "wvskod", "wvso", "wvsod"};
    private static String[] normModesText = { "x" };
    private static String[] normModesDefault = { "djkopsvw" };
//    private static String[] normModesDefault = { "dsvw" };
//    private static String[] normModesDefault = { "x" };
    private static int[] ngramSizesAll = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
    private static int[] ngramSizesText = { 1 };

    private static int[] ngramSizeDefault = { 11 }; // T3 n-gram size
    private static String workingDir;
    private static String configFile;
    private static String errMeasure;
    private static String mode;
    private static boolean queryReduction;
    private static String cloneClusterFile;
    private static String cloneClusterFilePrefix = "clone_clusters";

    public static void main(String[] args) {
        configFile = args[0];
        readFromConfigFile(configFile);
        cloneClusterFilePrefix = cloneClusterFile + "_" + cloneClusterFilePrefix;
//        if (mode.endsWith("_text")) {
//            normModes = normModesText;
//            ngramSizes = ngramSizesText;
//        } else if (mode.endsWith("_ngram")) {
//            normModes = normModesText;
//            ngramSizes = ngramSizesAll;
//        } else if (mode.endsWith("_codenorm")) {
//            normModes = normModesAll;
//            ngramSizes = ngramSizesText;
//        } else if (mode.endsWith("_both")) {
//            normModes = normModesAll;
//            ngramSizes = ngramSizesAll;
//        } else if (mode.endsWith("_def")) {
//            normModes = normModesDefault;
//            ngramSizes = ngramSizeDefault;
//        } else {
//            normModes = normModesAll;
//            ngramSizes = ngramSizesAll;
//        }
        ArrayList<EvalResult> bestResults = new ArrayList<>();
//        switch (mode) {
//            case "tfidf_text":
//            case "tfidf_text_ngram":
//            case "tfidf_text_codenorm":
//            case "tfidf_text_def":
//            case "tfidf_text_both":
//                bestResults = tfidfTextExp();
//                break;
//            default:
//                System.out.println("No ranking function found");
//        }
        bestResults = tfidfTextExp();
        System.out.println("best " + errMeasure.toLowerCase()
                + " = " + bestResults.get(0).getSetting() + "," + bestResults.get(0).getValue());
        String qr = "no_qr";
        if (queryReduction) {
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
            workingDir = prop.getProperty("outputFolder");
            mode = prop.getProperty("similarityMode");
            cloneClusterFile = prop.getProperty("cloneClusterFile");
            ngramSizeDefault[0] = Integer.parseInt(prop.getProperty("ngramSize"));
            String errMeasureConfig = prop.getProperty("errorMeasure");
            if (errMeasureConfig.equals("arp"))
                errMeasure = Settings.ErrorMeasure.ARP;
            else
                errMeasure = Settings.ErrorMeasure.MAP;
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
        return siamese.runExperiment(indexSettings, mappingStr, cloneClusterFilePrefix);
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
