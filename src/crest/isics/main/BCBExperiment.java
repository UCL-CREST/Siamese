package crest.isics.main;


import crest.isics.document.Document;
import crest.isics.helpers.BCBEvaluator;
import crest.isics.helpers.OutputFormatter;
import crest.isics.settings.Settings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.System.exit;

public class BCBExperiment {

    private static String inputFolder;
    private static int minCloneSize;
    private static String outputFormat;

    public static void main(String[] args) {
        String config = "config.properties";
        String bcbLoc = "/Users/Chaiyong/Downloads/dataset";
        readFromConfigFile(config);

        ISiCS isics = new ISiCS(config);
        isics.startup();

        BCBEvaluator evaluator = new BCBEvaluator();
        ArrayList<Integer> t1Clones = evaluator.getType1CloneIds(10, -1);

        for (int i = 0; i < t1Clones.size(); i++) {
            System.out.println("### Query no. " + i);
            ArrayList<Document> cloneGroup = evaluator.getCloneGroup(t1Clones.get(i), minCloneSize);
            String queryFile = cloneGroup.get(0).getFile();
            boolean successful = copyBCBFile(queryFile, bcbLoc, inputFolder);
            if (successful) {
                String outputFile = null;
                try {
                    outputFile = isics.execute();
                    double map = evaluator.evaluateType1Clones(cloneGroup, outputFile, Settings.ErrorMeasure.MAP);
//                    // delete the output file
//                    File oFile = new File(outputFile);
//                    boolean delSuccess = oFile.delete();
//                    if (!delSuccess) {
//                        System.out.println("ERROR: can't delete the output file: " + outputFile);
//                    }
                    deleteBCBFile(queryFile);
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } else {
                System.out.println("ERROR: can't copy the query file to " + inputFolder);
            }
        }

        isics.shutdown();
    }

    public static boolean copyBCBFile(String fileName, String from, String to) {
        String[] subfolders = {"selected", "default", "sample"};
        File fromFile = null;
        File toFile = new File(to + "/" + fileName);
        // check the location of the file in the 3 subfolders
        for (String s: subfolders) {
            fromFile = new File(from + "/" + s + "/" + fileName);
            if (fromFile.exists() && !fromFile.isDirectory()) {
                break;
            }
        }

        if (fromFile == null) {
            System.out.println("Cannot find the file. Skip.");
            return false;
        }

        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            System.out.println("ERROR: cannot copy file. " + e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean deleteBCBFile(String fileName) {
        File f = new File(inputFolder + "/" + fileName);
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
