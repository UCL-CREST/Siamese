package crest.isics.main;


import crest.isics.document.Document;
import crest.isics.helpers.BCBEvaluator;
import crest.isics.settings.Settings;
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
        String config = "config_bcb.properties";
        String bcbLoc = "/Users/Chaiyong/Downloads/dataset";
        readFromConfigFile(config);

        ISiCS isics = new ISiCS(config);
        isics.startup();

        BCBEvaluator evaluator = new BCBEvaluator();
        ArrayList<Integer> t1Clones = evaluator.getType1CloneIds(100, -1, minCloneSize);
        System.out.println("Found " + t1Clones.size() + " type-1 clone groups.");

        double sumAvgPrec = 0.0;

        for (int i = 0; i < t1Clones.size(); i++) {
            System.out.println("\n### Query no. " + i + " ID: " + t1Clones.get(i));
            ArrayList<Document> cloneGroup = evaluator.getCloneGroup(t1Clones.get(i), minCloneSize);
            System.out.println("Clone group size " + (cloneGroup.size() - 1));
            String queryFile = cloneGroup.get(0).getFile();
//            System.out.println(queryFile);
            boolean successful = copyBCBFile(queryFile, bcbLoc, inputFolder);
            if (successful) {
                String outputFile = null;
                try {
                    isics.setResultOffset(0);
                    isics.setResultsSize(cloneGroup.size());
                    outputFile = isics.execute();
                    evaluator.printGroundTruth(cloneGroup);
                    double avgPrec = evaluator.evaluateType1Query(cloneGroup, outputFile, Settings.ErrorMeasure.MAP);
                    System.out.println("AvgPrec = " + avgPrec);
                    sumAvgPrec += avgPrec;

                    // delete the output and the query file
                    File oFile = new File(outputFile);
                    File qFile = new File(inputFolder + "/" + queryFile);
                    boolean delSuccess = qFile.delete();
                    if (!delSuccess) {
                        System.out.println("ERROR: can't delete the query file: " + queryFile);
                    }
//                    delSuccess = oFile.delete();
//                    if (!delSuccess) {
//                        System.out.println("ERROR: can't delete the output file: " + outputFile);
//                    }
                    deleteBCBFile(queryFile);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } else {
                System.out.println("ERROR: can't copy the query file to " + outputFolder);
            }
        }

        double map = sumAvgPrec / t1Clones.size();
        System.out.println("MAP = " + map);

        isics.shutdown();
    }

    public static boolean copyBCBFile(String fileName, String from, String to) {
        File fromFile = null;
        File toFile = new File(to + "/" + fileName);
        // check the location of the file in the 3 subfolders
        fromFile = new File(from + "/" + fileName);

//        System.out.println("from: " + fromFile.getAbsolutePath());
//        System.out.println("to: " + toFile);

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
