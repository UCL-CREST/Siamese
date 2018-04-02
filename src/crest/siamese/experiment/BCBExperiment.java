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
        String outputLoc = "bcb_clones";
//        readFromConfigFile(config);
//        Siamese siamese = new Siamese(config);
//        siamese.startup();
        evaluator = new BCBEvaluator();
        evaluator.connectDB();

        switch (task) {
            case "all":
                extractAllClonePairs(outputLoc, bcbLoc);
                break;
            case "sample":
                extractSamplePairs(outputLoc, bcbLoc, getSamplePairs());
                break;
            case "check":
                ArrayList<BCBDocument> pairs = getSamplePairs();
                for (int i=0; i<pairs.size(); i++) {
                    
                }
        }

//        System.out.println("Start searching ...");
//        try {
//            siamese.execute();
//        } catch(NoNodeAvailableException nne) {
//            System.out.println("Elasticsearch is not running. Please execute the following command:\n" +
//                    "./elasticsearch-2.2.0/bin/elasticsearch -d");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//        siamese.shutdown();
//        Date endDate = MyUtils.getCurrentTime();
//        System.out.println("Elapse time (ms): " + (endDate.getTime() - startDate.getTime()));
        evaluator.closeDBConnection();
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
        clones = evaluator.getCloneFragments(sql, "FUNCTION_ID_ONE");
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
                "WHERE FUNCTIONS.TYPE = 'sample';";
        clones = evaluator.getCloneFragments(sql, "ID");
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
