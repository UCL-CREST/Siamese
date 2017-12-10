package crest.siamese.experiment;

import crest.siamese.document.BCBDocument;
import crest.siamese.document.Document;
import crest.siamese.helpers.BCBEvaluator;
import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class BCBExperiment {

    private static String inputFolder;
    private static String outputFolder;
    private static int minCloneSize;
    private static String outputFormat;

    public static void main(String[] args) {
        String config = "config_eval_bcb.properties";
        String bcbLoc = "/Users/Chaiyong/Downloads/dataset";
        readFromConfigFile(config);
        int resultSize = 101;

        // delete the previous result file
        File resultFile = new File("results/search_results.txt");
        if (resultFile.exists())
            resultFile.delete();

        Siamese siamese = new Siamese(config);
        siamese.startup();

        BCBEvaluator evaluator = new BCBEvaluator();
        ArrayList<Integer> clones = evaluator.getCloneIds(100, -1, minCloneSize);
        System.out.println("Found initial " + clones.size() + " clone groups.");

        for (int i = 0; i < clones.size(); i++) {

            String outToFile = "";
            System.out.println("\n### Query no. " + i + " ID: " + clones.get(i));
//            outToFile += "Query no. " + i + " ID: " + clones.get(i) + "\n";

            Document query = evaluator.getQuery(clones.get(i));
            ArrayList<BCBDocument> cloneGroup = evaluator.getCloneGroup(clones.get(i), minCloneSize);

//            // too large, skip
//            if (cloneGroup.size() > resultSize) {
//                System.out.println(query.getLocationString() + " is too large. Skip...");
//                outToFile += query.getLocationString() + " is too large. Skip..." + "\n";
//                continue;
//            }

            System.out.println("Clone group size: " + cloneGroup.size());
//            outToFile += "Clone group size: " + cloneGroup.size() + "\n";
            String queryFile = query.getFile();

            boolean successful = copyBCBFile(queryFile, bcbLoc, inputFolder);
            if (successful) {
                String outputFile = null;
                try {
                    siamese.setResultOffset(0);

                    int multiply = 2; // start with double the size
                    boolean foundAllTPs = false;
                    // retrieve documents at double the size of the clone group.
                    // so we can manually check for all missing pairs.
//                    do {
                    siamese.setResultsSize(resultSize);
                    outputFile = siamese.execute();

//                    System.out.println("Query size: " + resultSize);
                    System.out.println("Query size: " + resultSize + "\n" + "Q: " + query.getLocationString());

                    MyUtils.writeToFile("results", "search_results.txt", outToFile, true);

                    foundAllTPs = evaluator.evaluateCloneQuery(query, cloneGroup, resultSize, outputFile);
                        // if all TPs are not found yet, increase the result size
//                        multiply += 1;
//                    } while (!foundAllTPs && multiply <= 2);

                    // delete the output and the query file
                    File oFile = new File(outputFile);
                    File qFile = new File(inputFolder + "/" + queryFile);
                    boolean delSuccess = qFile.delete();
                    if (!delSuccess) {
                        System.out.println("ERROR: can't delete the query file: " + queryFile);
                    }
                    delSuccess = oFile.delete();
                    if (!delSuccess) {
                        System.out.println("ERROR: can't delete the output file: " + outputFile);
                    }
                    deleteBCBFile(queryFile);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } else {
                System.out.println("ERROR: can't copy the query file to " + outputFolder);
            }
        }

//        double map = sumAvgPrec / clones.size();
//        System.out.println("MAP = " + map);
        System.out.println("=============================");
        siamese.shutdown();
    }

    public static boolean copyBCBFile(String fileName, String from, String to) {
        File fromFile = null;
        File toFile = new File(to + "/" + fileName);
        // check the location of the file in the 3 subfolders
        fromFile = new File(from + "/" + fileName);

        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            System.out.println("ERROR: cannot copy file. " + e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean deleteBCBFile(String fileName) {
        File f = new File(outputFolder + "/" + fileName);
        return f.delete();
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
            inputFolder = prop.getProperty("inputFolder");
            outputFolder = prop.getProperty("outputFolder");
            minCloneSize = Integer.parseInt(prop.getProperty("minCloneSize"));
            outputFormat = prop.getProperty("outputFormat");
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
}
