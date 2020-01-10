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

import crest.siamese.document.BCBDocument;
import crest.siamese.helpers.BCBEvaluator;
import crest.siamese.helpers.MyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class BCBExperiment {

    private static String inputFolder;
    private static String outputFolder;
    private static int minCloneSize;
    private static BCBEvaluator evaluator;

    public static void main(String[] args) {
        String task = args[0];
        String bcbLoc = "/Users/Chaiyong/Downloads/bcb_dataset";
        String outputLoc;
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
            case "type1":
                outputLoc = "type1_clones";
                int limit = 50;
                extractType1Pairs(outputLoc, bcbLoc, getType1Pairs(limit));
                break;
            case "check":
                String fileToCheck = args[1];
                String[] prefixes = {"/Users/Chaiyong/Documents/phd/2017/Siamese/",
                        "/Users/Chaiyong/Downloads/bcb_dataset/"};
                System.out.println("File: " + fileToCheck);
                String pFilename = fileToCheck.replace(".csv", "_p.csv");
                String eFilename = fileToCheck.replace(".csv", "_e.csv");
                // Settings for analysing a raw result file
                int size = 16;
                int checksize = 15;
//                boolean includeQuery = false;
                boolean includeQuery = true;
                processOutputFile(fileToCheck, pFilename, prefixes, size);
                evaluate(pFilename, eFilename, includeQuery, checksize);
                // Settings for analysing the result file with result size = 15
                size = 15;
                checksize = 10;
                int[] targetTypes = {1, 2, 3};
//                int[] targetTypes = {3};
                calculate(eFilename, size, checksize, includeQuery, targetTypes);
                break;
        }
        evaluator.closeDBConnection();
    }

    private static boolean checkInArray(int[] arr, int item) {
        for (int i=0; i<arr.length; i++) {
            if (item == arr[i])
                return true;
        }
        return false;
    }

    private static void calculate(String outputFile,
                                  int resultSize,
                                  int size,
                                  boolean includeQuery, int[] targetTypes) {
        System.out.println("Checking: " + outputFile);
        try {
            String[] lines = MyUtils.readFile(outputFile);
            System.out.println(size + "-prec, MRR");
            double sum10Prec = 0;
            double sumMRR = 0;
            int[] types = new int[3];
            /* check the top-N results of each query */
            for (int i = 0; i < lines.length; i += (resultSize + 1)) {
                double tp = 0;
                int rel = 0;
                /* skip the query (i.e. j=0) */
                String[] query = lines[i].split(",");
                int j = 1;
                int limit = size;
                int foundQ = 0;
                while (j <= limit) {
//                for (int j = 1; j <= size; j++) {
                    String[] result = lines[i + j].split(",");
                    try {
                        /* if not include query in the result, skip when find it */
                        if (!includeQuery && compare(query, result, 4)) {
                            limit++;
                            j++;
                            foundQ = 1;
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println(result[0] + "," + result[1] + "," + result[2]
                                    + "," + result[3] + "," + result[4]);
                    System.out.println(result[0] + "," + result[1] + "," + result[2]
                            + "," + result[3] + "," + result[4] + "," + result[5]);
                    /* find a true positive */
                    if (result[5].equals("T") && checkInArray(targetTypes, Integer.parseInt(result[4]))) {
                        tp += 1;
                        /* found the first relevant result */
                        if (rel == 0) {
                            rel = j - foundQ;
                        }
                        int type = Integer.parseInt(result[4]);
                        types[type - 1]++;
                    }
                    j++;
                }
                sum10Prec += tp / size;
                double mrr = 0;
                if (rel != 0)
                    mrr = (double)1/rel;
                System.out.println(tp / size + "," + mrr);
                sumMRR += mrr;
            }
            System.out.println("Avg. " + size + "-prec," + sum10Prec/(lines.length/(resultSize + 1)));
            System.out.println("Avg. MRR," + sumMRR/(lines.length/(resultSize + 1)));
            System.out.println("Type-1," + types[0]);
            System.out.println("Type-2," + types[1]);
            System.out.println("Type-3," + types[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean compare(String[] arr1, String[] arr2, int size) throws Exception {
        if (arr1.length >= size && arr2.length >= size) {
            for (int i = 0; i < size; i++) {
                if (!arr1[i].equals(arr2[i]))
                    return false;
            }
        } else {
            throw new Exception("The arrays are smaller than the specified size.");
        }
        return true;
    }

    private static void evaluate(String filename, String outputFile, boolean includeQuery, int size) {
        MyUtils.deleteFile(outputFile);
        int count = 1;
        try {
            String[] lines = MyUtils.readFile(filename);
            for (String line: lines) {
                System.out.println(count + "," + line);
                checkResults(line, outputFile, includeQuery, size);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check the result by comparing it to the groundtruth
     * @param result a ranked list of search results
     * @param outputFile the file to write the check result
     * @param includeQuery include query in the check?
     * @param size size of the results to consider
     * @return
     */
    private static void checkResults(String result, String outputFile, boolean includeQuery, int size) {
        int check = 0;
        String[] parts = result.split(",");
        ArrayList<BCBDocument> groundtruth = getClone(parts[0]);
        MyUtils.writeToFile(outputFile, parts[1] + "," + parts[2] + "," +
                parts[3] + "," + parts[4] + ",Q\n", true);
        for (int i=5; i<parts.length; i+=4) {
            String toWrite = "";
            /* check if the query is reported in the results */
            if (!identical(parts[1], parts[2], parts[3], parts[4],
                    parts[i], parts[i+1], parts[i+2], parts[i+3])) {
                int type = inGroundtruth(groundtruth, parts[i], parts[i+1], parts[i+2], parts[i+3]);
                /* found in the groundtruth */
                if (type != 0) {
                    toWrite = parts[i] + "," + parts[i+1] + "," + parts[i+2] + "," + parts[i+3] + "," + type + ",T";

                } else {
                    toWrite = parts[i] + "," + parts[i+1] + "," + parts[i+2] + "," + parts[i+3] + ",,F";
                }
                check++;
            } else {
                /* if not include query, just skip this one and not print anything */
                if (!includeQuery) {
                    continue;
                }
                /* found query as a result, as it as T1 */
                toWrite = parts[i] + "," + parts[i+1] + "," + parts[i+2] + "," + parts[i+3] + ",1,T";
                check++;
            }
            System.out.println(toWrite);
            MyUtils.writeToFile(outputFile, toWrite + "\n", true);
            /* return if the no. of specified results are checked */
            if (check >= size) {
                return;
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
                for (String pref : prefixes) {
                    line = line.replace(pref, "");
                }
                String[] parts = line.split(",");
                /* working on the query */
                String[] q = parts[0].split("#");
                String query = q[0] + "," + q[1] + "," + q[2] + ".java," + q[3] + "," + q[4].split("\\.")[0];
                line = query;
                /* start working on the result */
                for (int i = 1; i < parts.length && i <= limit; i++) {
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
        System.out.println("Processed file: " + outfile);
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

    private static ArrayList<BCBDocument> getType1Pairs(int limit) {
        /* get sample clone fragments */
        ArrayList<BCBDocument> clones;
        String sql = "SELECT * FROM\n" +
                "  (SELECT FUNCTION_ID_ONE\n" +
                "   FROM clones\n" +
                "    WHERE SYNTACTIC_TYPE = 1\n" +
                "   UNION\n" +
                "   SELECT FUNCTION_ID_TWO\n" +
                "   FROM clones\n" +
                "    WHERE SYNTACTIC_TYPE = 1\n" +
                "  )\n" +
                "    AS A\n" +
                "  INNER JOIN FUNCTIONS\n" +
                "    ON A.FUNCTION_ID_ONE = FUNCTIONS.ID\n" +
                "AND FUNCTIONS.ENDLINE - FUNCTIONS.STARTLINE + 1 >= 6\n" +
                "LIMIT " + limit + ";";
        clones = evaluator.getCloneFragments(sql, "ID", false);
        return clones;
    }

    private static void extractSamplePairs(String outputLoc, String bcbLoc, ArrayList<BCBDocument> clones) {
        extractClones(outputLoc, bcbLoc, clones);
    }

    private static void extractType1Pairs(String outputLoc, String bcbLoc, ArrayList<BCBDocument> clones) {
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
