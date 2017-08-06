package crest.isics.main;

import crest.isics.helpers.EvalResult;
import crest.isics.settings.Settings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Experiment {

    public static String prefixToRemove = "";

    private static String[] normModes = {};
    private static int[] ngramSizes = {};

    private static String[] normModesAll = {
            "x", "s", "w", "ws", "p", "ps", "pw", "pws", "k", "ks",
            "kw", "kws", "kp", "kps", "kpw", "kpws", "j", "js", "jw", "jws",
            "jp", "jps", "jpw", "jpws", "jk", "jks", "jkw", "jkws", "jkp", "jkps",
            "jkpw", "jkpws", "d", "ds", "dw", "dws", "dp", "dps", "dpw", "dpws",
            "dk", "dks", "dkw", "dkws", "dkp", "dkps", "dkpw", "dkpws", "dj", "djs",
            "djw", "djws", "djp", "djps", "djpw", "djpws", "djk", "djks", "djkw", "djkws",
            "djkp", "djkps", "djkpw", "djkpws"};
    private static String[] normModesText = { "x" };
    private static int[] ngramSizesAll = { 1, 2, 3, 4, 5 };
    private static int[] ngramSizesText = { 1 };

    private static String errMeasure = Settings.ErrorMeasure.MAP;
    public static boolean isPrint = false;
    private static boolean deleteIndexAfterUse = false;
    private static int resultOffset = 0;
    private static int resultSize = 204;
    private static int querySizeLimit = 100;

    public static void main(String[] args) {

        if (args.length < 3) {
            // If missing some arguments, show the help
            System.out.println("Usage: java Experiment <similarity> <input folder> <working dir>");
            System.exit(-1);

        } else {

            String outputFile = "";

            /* TODO: FIX THIS
            String mode = args[0];
            String inputDir = args[1];
            String workingDir = args[2];
            */
            // Use input here for easiness
            // TODO: FIX THIS
            String mode = "tfidf_text";
            String inputDir = "/Users/Chaiyong/Documents/phd/2016/cloplag/tests_andrea";
            String workingDir = "/Users/Chaiyong/Downloads/isics_results";

            if (mode.endsWith("_text")) {
                normModes = normModesText;
                ngramSizes = ngramSizesText;
            } else if (mode.endsWith("_ngram")) {
                normModes = normModesText;
                ngramSizes = ngramSizesAll;
            } else if (mode.endsWith("_codenorm")) {
                normModes = normModesAll;
                ngramSizes = ngramSizesAll;
            } else {
                normModes = normModesAll;
                ngramSizes = ngramSizesAll;
            }

            prefixToRemove = inputDir;
            if (!prefixToRemove.endsWith("/"))
                prefixToRemove += "/"; // append / at the end

            EvalResult bestResult = new EvalResult();

            switch(mode) {
                case "tfidf_text":
                case "tfidf_text_ngram":
                case "tfidf_text_ngram_codenorm":
                    bestResult = tfidfTextExp(inputDir, workingDir, isPrint);
                    break;
                case "bm25_text":
                case "bm25_text_ngram":
                case "bm25_text_ngram_codenorm":
                    bestResult = bm25TextExp(inputDir, workingDir, isPrint);
                    break;
                case "dfr_text":
                case "dfr_text_ngram":
                case "dfr_text_ngram_codenorm":
                    bestResult = dfrTextExp(inputDir, workingDir, isPrint);
                    break;
                case "ib_text":
                case "ib_text_ngram":
                case "ib_text_ngram_codenorm":
                    bestResult = ibTextExp(inputDir, workingDir, isPrint);
                    break;
                case "lmd_text":
                case "lmd_text_ngram":
                case "lmd_text_ngram_codenorm":
                    bestResult = lmdTextExp(inputDir, workingDir, isPrint);
                    break;
                case "lmj_text":
                case "lmj_text_ngram":
                case "lmj_text_ngram_codenorm":
                    bestResult = lmjTextExp(inputDir, workingDir, isPrint);
                    break;
                case "tfidf": /* normal mode (search all parameters + grams + normalisation) */
                    bestResult = tfidfExp(inputDir, workingDir, isPrint);
                    break;
                case "bm25":
                    bestResult = bm25Exp(inputDir, workingDir, isPrint);
                    break;
                case "dfr":
                    bestResult = dfrExp(inputDir, workingDir, isPrint);
                    break;
                case "ib":
                    bestResult = ibExp(inputDir, workingDir, isPrint);
                    break;
                case "lmdirichlet":
                case "lmd":
                    bestResult = lmdExp(inputDir, workingDir, isPrint);
                    break;
                case "lmjelinekmercer":
                case "lmj":
                    bestResult = lmjExp(inputDir, workingDir, isPrint);
                    break;
                default:
                    System.out.println("No ranking function found");
            }

            String ngram = "ngram_";
            if (ngramSizes.length == 1)
                ngram = "";

            writeToFile(workingDir, "best_" + errMeasure + "_" + ngram + mode + ".txt",
                    bestResult.getSetting() + ", " + bestResult.getValue(),
                    false);
        }

    }

    private static EvalResult tfidfTextExp(String inputDir, String workingDir, boolean isPrint) {
        String discO = "true";
        ISiCS isics = new ISiCS();
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

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";

        return isics.runExperiment(
                "localhost",
                "tfidf",
                "doc",
                inputDir,
                normModes,
                ngramSizes,
                true,
                true,
                workingDir,
                true,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);
    }


    private static EvalResult tfidfExp(String inputDir, String workingDir, boolean isPrint) {

        String[] discountOverlap = {"no", "true", "false"};
        ISiCS isics = new ISiCS();
        EvalResult bestResult = new EvalResult();

        for (String discO : discountOverlap) {

            String indexSettings = "";
            if (!discO.equals("no"))
                indexSettings = "{ \"number_of_shards\": 1, " +
                        "\"similarity\": { \"tfidf_similarity\": { \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                        "\"analysis\": { " +
                        "\"analyzer\": { " +
                        "\"default\": { " +
                        "\"type\": \"whitespace\"" +
                        "} } } }";
            else {
                indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
            }

            String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";

            EvalResult result = isics.runExperiment(
                    "localhost",
                    "tfidf_" + discO,
                    "doc",
                    inputDir,
                    normModes,
                    ngramSizes,
                    true,
                    true,
                    workingDir,
                    false,
                    indexSettings,
                    mappingStr,
                    isPrint,
                    deleteIndexAfterUse,
                    errMeasure,
                    resultOffset,
                    resultSize, querySizeLimit);

            if (result.getValue() > bestResult.getValue()) {
                bestResult = result;
            }
        }

        return bestResult;
    }

    private static EvalResult bm25TextExp(String inputDir, String workingDir, boolean isPrint) {
//        String[] normModes = { "x" };
//	    int[] ngramSizes = { 1 };
        double k1 = 1.2;
        double b = 0.75;
        String discO = "true";
        ISiCS isics = new ISiCS();

        String indexSettings = "{ \"number_of_shards\": 1, " +
                "\"similarity\": "
                + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
                + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";

        return isics.runExperiment(
                "localhost",
                "bm25",
                "doc",
                inputDir,
                normModes,
                ngramSizes,
                true,
                true,
                workingDir,
                true,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);
    }

    private static EvalResult bm25Exp(String inputDir, String workingDir, boolean isPrint) {

        String[] k1s = {"0.0", "0.6", "1.2", "1.8", "2.4"};
        String[] bs = {"0.0", "0.25", "0.50", "0.75", "1.00"};
        String[] discountOverlaps = {"true", "false"};

        ISiCS isics = new ISiCS();
        EvalResult bestResult = new EvalResult();

        for (String k1 : k1s) {
            for (String b : bs) {
                for (String discO : discountOverlaps) {
                    String indexSettings = "{ \"number_of_shards\": 1," +
                            "\"similarity\": "
                            + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
                            + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
                    String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
                    System.out.println(indexSettings);
                    EvalResult result = isics.runExperiment(
                            "localhost",
                            "bm25_" + k1 + "_" + b + "_" + discO,
                            "doc",
                            inputDir,
                            normModes,
                            ngramSizes,
                            true,
                            true,
                            workingDir,
                            false,
                            indexSettings,
                            mappingStr,
                            isPrint,
                            deleteIndexAfterUse,
                            errMeasure,
                            resultOffset,
                            resultSize,
                            querySizeLimit);

                    if (result.getValue() > bestResult.getValue()) {
                        bestResult = result;
                    }
                }
            }
        }

        return bestResult;
    }

    private static EvalResult dfrTextExp(String inputDir, String workingDir, boolean isPrint) {
        String bm = "be";
        String ae = "b";
        String norm = "h1";
//        String[] normModes = {"x"};
//        int[] ngramSizes = {1};

        ISiCS isics = new ISiCS();

        String indexSettings = "{ \"number_of_shards\": 1," +
                "\"similarity\": { \"dfr_similarity\" : { \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
                + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
                + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";

        return isics.runExperiment(
                "localhost",
                "dfr_" + bm + "_" + ae + "_" + norm,
                "doc",
                inputDir,
                normModes,
                ngramSizes,
                true,
                true,
                workingDir,
                false,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);

    }

    private static EvalResult dfrExp(String inputDir, String workingDir, boolean isPrint) {
        // String[] basicModelArr = {"be", "d", "g", "if", "in", "ine", "p"};
        String[] basicModelArr = {"ine", "p"};
        String[] afterEffectArr = {"no", "b", "l"};
        String[] dfrNormalizationArr = {"no", "h1", "h2", "h3", "z"};

        ISiCS isics = new ISiCS();
        EvalResult bestResult = new EvalResult();

        for (String bm : basicModelArr) {
            for (String ae : afterEffectArr) {
                for (String norm : dfrNormalizationArr) {
                    String indexSettings = "{ \"number_of_shards\": 1," +
                            "\"similarity\": { \"dfr_similarity\" : { \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
                            + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
                            + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";

                    String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";

                    EvalResult result = isics.runExperiment(
                            "localhost",
                            "dfr_" + bm + "_" + ae + "_" + norm,
                            "doc",
                            inputDir,
                            normModes,
                            ngramSizes,
                            true,
                            true,
                            workingDir,
                            false,
                            indexSettings,
                            mappingStr,
                            isPrint,
                            deleteIndexAfterUse,
                            errMeasure,
                            resultOffset,
                            resultSize,
                            querySizeLimit);

                    if (result.getValue() > bestResult.getValue()) {
                        bestResult = result;
                    }
                }
            }
        }
        return bestResult;
    }

    private static EvalResult ibTextExp(String inputDir, String workingDir, boolean isPrint) {
        String dist = "ll";
        String lamb = "df";
        String ibNorm = "h1";
//        int[] ngramSizes = {1};
//        String[] normModes = { "x" };

        ISiCS isics = new ISiCS();

        String indexSettings = "{ \"number_of_shards\": 1," +
                "\"similarity\": "
                + "{ \"ib_similarity\" : "
                + "{ \"type\": \"IB\", "
                + "\"distribution\": \"" + dist + "\", "
                + "\"lambda\": \"" + lamb + "\", "
                + "\"normalization\": \"" + ibNorm + "\""
                + "} "
                + "},  "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";

        return isics.runExperiment(
                "localhost",
                "ib_" + dist + "_" + lamb + "_" + ibNorm,
                "doc",
                inputDir,
                normModes,
                ngramSizes,
                true,
                true,
                workingDir,
                false,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);

    }

    private static EvalResult ibExp(String inputDir, String workingDir, boolean isPrint) {
        String[] distributions = {"ll", "spl"};
        String[] lambdas = {"df", "ttf"};
        String[] ibNormalizationArr = {"no", "h1", "h2", "h3", "z"};

        ISiCS isics = new ISiCS();
        EvalResult bestResult = new EvalResult();

        for (String dist : distributions) {
            for (String lamb : lambdas) {
                for (String ibNorm : ibNormalizationArr) {
                    String indexSettings = "{ \"number_of_shards\": 1," +
                            "\"similarity\": "
                            + "{ \"ib_similarity\" : "
                            + "{ \"type\": \"IB\", "
                            + "\"distribution\": \"" + dist + "\", "
                            + "\"lambda\": \"" + lamb + "\", "
                            + "\"normalization\": \"" + ibNorm + "\""
                            + "} "
                            + "},  "
                            + "\"analysis\" : { \"analyzer\" : "
                            + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
                    String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";

                    EvalResult result = isics.runExperiment(
                            "localhost",
                            "ib_" + dist + "_" + lamb + "_" + ibNorm,
                            "doc",
                            inputDir,
                            normModes,
                            ngramSizes,
                            true,
                            true,
                            workingDir,
                            false,
                            indexSettings,
                            mappingStr,
                            isPrint,
                            deleteIndexAfterUse,
                            errMeasure,
                            resultOffset,
                            resultSize,
                            querySizeLimit);

                    if (result.getValue() > bestResult.getValue()) {
                        bestResult = result;
                    }
                }
            }
        }
        return bestResult;
    }

    private static EvalResult lmdTextExp(String inputDir, String workingDir, boolean isPrint) {
        String mu = "2000";
//        String[] normModes = { "x" };
//        int[] ngramSizes = { 1 };

        ISiCS isics = new ISiCS();
        String indexSettings = "{ \"number_of_shards\": 1," +
                "\"similarity\": "
                + "{ \"lmd_similarity\" : "
                + "{ \"type\": \"LMDirichlet\", "
                + "\"mu\": \"" + mu + "\""
                + "} "
                + "}, "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
        return isics.runExperiment(
                "localhost",
                "lmd_" + mu,
                "doc",
                inputDir,
                normModes,
                ngramSizes,
                true,
                true,
                workingDir,
                false,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);
    }

    private static EvalResult lmdExp(String inputDir, String workingDir, boolean isPrint) {
        String[] mus = {"500", "1000", "1500",
                "2000", "2500", "3000"};
        EvalResult bestResult = new EvalResult();

        ISiCS isics = new ISiCS();
        for (String mu : mus) {
            String indexSettings = "{ \"number_of_shards\": 1," +
                    "\"similarity\": "
                    + "{ \"lmd_similarity\" : "
                    + "{ \"type\": \"LMDirichlet\", "
                    + "\"mu\": \"" + mu + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
            // System.out.println(indexSettings);

            String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
            EvalResult result = isics.runExperiment(
                    "localhost",
                    "lmd_" + mu,
                    "doc",
                    inputDir,
                    normModes,
                    ngramSizes,
                    true,
                    true,
                    workingDir,
                    false,
                    indexSettings,
                    mappingStr,
                    isPrint,
                    deleteIndexAfterUse,
                    errMeasure,
                    resultOffset,
                    resultSize,
                    querySizeLimit);

            if (result.getValue() > bestResult.getValue()) {
                bestResult = result;
            }
        }
        return bestResult;
    }

    private static EvalResult lmjTextExp(String inputDir, String workingDir, boolean isPrint) {
//        String[] normModes = { "x" };
        String lambda = "0.1";
//        int[] ngramSizes = { 1 };
        ISiCS isics = new ISiCS();
        String indexSettings = "{ \"number_of_shards\": 1," +
                "\"similarity\": "
                + "{ \"lmj_similarity\" : "
                + "{ \"type\": \"LMJelinekMercer\", "
                + "\"lambda\": \"" + lambda + "\""
                + "} "
                + "}, "
                + "\"analysis\" : { \"analyzer\" : "
                + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

        String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";

        return isics.runExperiment(
                "localhost",
                "lmj_" + lambda,
                "doc",
                inputDir, normModes,
                ngramSizes, true,
                true,
                workingDir,
                false,
                indexSettings,
                mappingStr,
                isPrint,
                deleteIndexAfterUse,
                errMeasure,
                resultOffset,
                resultSize,
                querySizeLimit);
    }

    private static EvalResult lmjExp(String inputDir, String workingDir, boolean isPrint) {

        String[] lambdas = { "0.1", "0.2", "0.3", "0.4", "0.5",
                "0.6", "0.7", "0.8", "0.9", "1.0" };
        ISiCS isics = new ISiCS();
        EvalResult bestResult = new EvalResult();

        for (String lambda : lambdas) {
            String indexSettings = "{ \"number_of_shards\": 1," +
                    "\"similarity\": "
                    + "{ \"lmj_similarity\" : "
                    + "{ \"type\": \"LMJelinekMercer\", "
                    + "\"lambda\": \"" + lambda + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";

            String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";

            EvalResult result = isics.runExperiment(
                    "localhost",
                    "lmj_" + lambda,
                    "doc",
                    inputDir,
                    normModes,
                    ngramSizes,
                    true,
                    true,
                    workingDir,
                    false,
                    indexSettings,
                    mappingStr,
                    isPrint,
                    deleteIndexAfterUse,
                    errMeasure,
                    resultOffset,
                    resultSize,
                    querySizeLimit);

            if (result.getValue() > bestResult.getValue())
                bestResult = result;
        }

        return bestResult;
    }

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

    public static void writeToFile(String location, String filename, String content, boolean isAppend) {
        if (createDir(location)) {
            /* copied from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ */
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                fw = new FileWriter(location + "/" + filename, isAppend);
                bw = new BufferedWriter(fw);
                bw.write(content);
                if (!isAppend)
                    System.out.println("Saved as: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        } else {
            System.out.println("ERROR: can't create a directory at: " + location);
        }
    }

    private static boolean createDir(String location) {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
