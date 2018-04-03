package crest.siamese.experiment;

import crest.siamese.document.BCBDocument;
import crest.siamese.document.Document;
import crest.siamese.helpers.BCBEvaluator;
import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.transport.NoNodeAvailableException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class BCBExperiment {

    private static String inputFolder;
    private static String outputFolder;
    private static int minCloneSize;
    private static BCBEvaluator evaluator;

    public static void main(String[] args) {
        String task = args[0];
        Date startDate = MyUtils.getCurrentTime();
        String config = "config_bcb_search.properties";
        String bcbLoc = "/Users/Chaiyong/Downloads/bcb_dataset";
        String outputLoc;
//        readFromConfigFile(config);
//        Siamese siamese = new Siamese(config);
//        siamese.startup();
        evaluator = new BCBEvaluator();
        evaluator.connectDB();

        switch (task) {
            case "all":
                outputLoc = "bcb_clones";
                extractAllClonePairs(outputLoc, bcbLoc);
                break;
            case "sample":
                outputLoc = "sample_clones";
                extractSamplePairs(outputLoc, bcbLoc, getSamplePairs());
                break;
            case "check":
                String fileToCheck = args[1];
                String[] prefixes = {"/Users/Chaiyong/Documents/phd/2017/Siamese/",
                        "/Users/Chaiyong/Downloads/bcb_dataset/"};
                System.out.println("File: " + fileToCheck);
                String pFilename = fileToCheck.replace(".csv", "_p.csv");
                String eFilename = fileToCheck.replace(".csv", "_e.csv");
//                processOutputFile(fileToCheck, pFilename, prefixes, 11);
//                evaluate(pFilename, eFilename);
                calculate(eFilename);
                break;
        }
        evaluator.closeDBConnection();
    }

    private static void calculate(String outputFile) {
        int SIZE = 10;
        try {
            String[] lines = MyUtils.readFile(outputFile);
            double sum10Prec = 0;
            double sumMRR = 0;
            int[] types = new int[3];
            /* check the top-10 results of each query */
            for (int i = 0; i < lines.length; i += SIZE) {
                double tp = 0;
                int rel = 0;
                for (int j = 0; j < SIZE; j++) {
//                    System.out.println(i + "," + j + ":" + lines[i + j]);
                    String[] result = lines[i + j].split(",");
                    /* find a true positive */
                    if (result[result.length - 1].equals("T")) {
                        tp += 1;
                        /* found the first relevant result */
                        if (rel == 0) {
                            rel = j + 1;
                        }
                        int type = Integer.parseInt(result[result.length - 2]);
                        types[type - 1]++;
                    }
                }

                System.out.println("10-prec," + tp / SIZE);
                sum10Prec += tp / SIZE;
                double mrr = 0;
                if (rel != 0)
                    mrr = 1/rel;
                System.out.println("MRR," + mrr);
                sumMRR += mrr;
            }
            System.out.println("Avg. 10-prec," + sum10Prec/(lines.length/SIZE));
            System.out.println("Avg. MRR," + sumMRR/(lines.length/SIZE));
            System.out.println("Type-1," + types[0]);
            System.out.println("Type-2," + types[1]);
            System.out.println("Type-3," + types[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void evaluate(String filename, String outputFile) {
        MyUtils.deleteFile(outputFile);
        int count = 1;
        try {
            String[] lines = MyUtils.readFile(filename);
            for (String line: lines) {
                System.out.println(count + "," + line);
                checkResults(line, outputFile);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkResults(String result, String outputFile) {
        String[] parts = result.split(",");
        ArrayList<BCBDocument> groundtruth = getClone(parts[0]);
//        for (BCBDocument d: groundtruth)
//            System.out.println(d);
//        System.out.println();
        for (int i=5; i<parts.length; i+=4) {
            /* skip the query in the result */
            if (!identical(parts[1], parts[2], parts[3], parts[4],
                    parts[i], parts[i+1], parts[i+2], parts[i+3])) {
                int type = inGroundtruth(groundtruth, parts[i], parts[i+1], parts[i+2], parts[i+3]);
                if (type != 0) { // found in the groundtruth
                    System.out.println(parts[i] + "," + parts[i+1] + "," + parts[i+2] + "," + parts[i+3]
                            + "," + type + ",T");
                    MyUtils.writeToFile(outputFile, parts[i] + "," + parts[i+1] + "," + parts[i+2] +
                            "," + parts[i+3]
                            + "," + type + ",T\n", true);
                } else {
                    System.out.println(parts[i] + "," + parts[i+1] + "," + parts[i+2] + "," + parts[i+3] + ",,F");
                    MyUtils.writeToFile(outputFile, parts[i] + "," + parts[i+1] + "," + parts[i+2]
                            + "," + parts[i+3] + ",,F\n", true);
                }
//                /* already go over the groundtruth size */
//                if (i >= (groundtruth.size() * 4) + 4)
//                    break;
            }
        }
    }

    private static int inGroundtruth(ArrayList<BCBDocument> groundtruth,
                                         String type, String file, String start, String end) {
        for (BCBDocument d: groundtruth) {
            if (d.getFile().equals(type + "/" + file) && d.getStartLine() == Integer.parseInt(start)
                    && d.getEndLine() == Integer.parseInt(end))
                return d.getSyntacticType();
        }
        return 0;
    }

    private static boolean identical(String type1, String file1, String start1, String end1,
                                     String type2, String file2, String start2, String end2) {
        return type1.equals(type2) && file1.equals(file2) && start1.equals(start2) && end1.equals(end2);
    }

    private static ArrayList<BCBDocument> getClone(String qid) {
        String sql = "SELECT ID AS FUNCTION_ID_ONE, TYPE, F2NAME AS NAME, F2START AS STARTLINE, F2END AS ENDLINE, STYPE AS SYNTACTIC_TYPE\n" +
                "FROM (\n" +
                "  SELECT F1.NAME AS F1NAME, F1.STARTLINE AS F1START, F1.ENDLINE AS F1END, CLONES.SYNTACTIC_TYPE AS STYPE,\n" +
                "         F2.NAME AS F2NAME, F2.STARTLINE AS F2START, F2.ENDLINE AS F2END, F2.TYPE AS TYPE,\n" +
                "         F2.ID AS ID\n" +
                "  FROM CLONES\n" +
                "    INNER JOIN FUNCTIONS AS F1 ON FUNCTION_ID_ONE = F1.ID\n" +
                "    INNER JOIN FUNCTIONS AS F2 ON FUNCTION_ID_TWO = F2.ID\n" +
                "  WHERE F1.ID = " + qid + "\n" +
                "  UNION\n" +
                "  SELECT F2.NAME AS F1NAME, F2.STARTLINE AS F1START, F2.ENDLINE AS F1END, CLONES.SYNTACTIC_TYPE AS STYPE,\n" +
                "         F1.NAME AS F2NAME, F1.STARTLINE AS F2START, F1.ENDLINE AS F2END, F1.TYPE AS TYPE,\n" +
                "         F1.ID AS ID\n" +
                "  FROM CLONES\n" +
                "    INNER JOIN FUNCTIONS AS F1 ON FUNCTION_ID_ONE = F1.ID\n" +
                "    INNER JOIN FUNCTIONS AS F2 ON FUNCTION_ID_TWO = F2.ID\n" +
                "  WHERE F2.ID = " + qid + "\n" +
                ");";
        ArrayList<BCBDocument> clones = evaluator.getCloneFragments(sql, "FUNCTION_ID_ONE", true);
        return clones;
    }

    private static void processOutputFile(String fileToCheck, String outfile, String[] prefixes, int limit) {
        try {
            /* delete the existing processed file */
            MyUtils.deleteFile(outfile);
            String[] lines = MyUtils.readFile(fileToCheck);
            for (String line: lines) {
                for (String pref: prefixes) {
                    line = line.replace(pref, "");
                }
                String[] parts = line.split(",");
                /* working on the query */
                String[] q = parts[0].split("#");
                String query = q[0] + "," + q[1] + "," + q[2] + ".java," + q[3] + "," + q[4].split("\\.")[0];
                line = query;
                /* start working on the result */
                for (int i=1; i<parts.length && i<=limit; i++) {
                    String[] rParts = parts[i].split("#");
                    String[] names = rParts[0].split(".java_")[0].split("/");
                    line += "," + names[0] + "," + names[1] + ".java," + rParts[1] + "," + rParts[2];
                }
                line += "\n";
                MyUtils.writeToFile(outfile, line, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractAllClonePairs(String outputLoc, String bcbLoc) {
        /* get all clone fragments */
        ArrayList<BCBDocument> clones;
        String sql = "SELECT *\n" +
                "FROM (\n" +
                "  SELECT FUNCTION_ID_ONE\n" +
                "  FROM clones\n" +
                "  UNION SELECT FUNCTION_ID_TWO\n" +
                "  FROM clones\n" +
                ") AS A INNER JOIN FUNCTIONS ON A.FUNCTION_ID_ONE = FUNCTIONS.ID;";
        clones = evaluator.getCloneFragments(sql, "FUNCTION_ID_ONE", false);
        extractClones(outputLoc, bcbLoc, clones);
    }

    private static ArrayList<BCBDocument> getSamplePairs() {
        /* get sample clone fragments */
        ArrayList<BCBDocument> clones;
        String sql = "SELECT *\n" +
                "FROM (\n" +
                "       SELECT FUNCTION_ID_ONE\n" +
                "       FROM clones\n" +
                "       UNION SELECT FUNCTION_ID_TWO\n" +
                "             FROM clones\n" +
                "     ) AS A INNER JOIN FUNCTIONS ON A.FUNCTION_ID_ONE = FUNCTIONS.ID\n" +
                "WHERE FUNCTIONS.TYPE = 'sample'\n" +
                "  AND FUNCTIONS.ENDLINE - FUNCTIONS.STARTLINE + 1 >= 6;";
        clones = evaluator.getCloneFragments(sql, "ID", false);
        return clones;
    }

    private static void extractSamplePairs(String outputLoc, String bcbLoc,
                                                             ArrayList<BCBDocument> clones) {
        extractClones(outputLoc, bcbLoc, clones);
    }

    private static void extractClones(String outputLoc, String bcbLoc, ArrayList<BCBDocument> clones) {
        MyUtils.createDir(outputLoc);
        for (BCBDocument c: clones) {
            try {
                MyUtils.saveClone(outputLoc,
                        c.getId() + "#" +
                        c.getFile()
                                .replace(".java", "")
                                .replace("/", "#") + "#"  + c.getStartLine() + "#" + c.getEndLine()
                                + ".java",
                        MyUtils.readFile(bcbLoc + "/" + c.getFile()), c.getStartLine(), c.getEndLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Extracted " + clones.size() + " clones to files.");
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
