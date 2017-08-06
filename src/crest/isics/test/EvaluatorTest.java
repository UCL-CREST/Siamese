package crest.isics.test;

/**
 * Created by Chaiyong on 7/17/17.
 */
import crest.isics.helpers.Evaluator;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class EvaluatorTest {

    private String mode = "test";
    private String outputDir = "results";

    @org.junit.Test
    public void TestARP1() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double arp = e.evaluateARP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestARP2() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double arp = e.evaluateARP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestARP3() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double arp = e.evaluateARP("resources/example_results_all_H.csv", 10);
        assertEquals(0.6, arp, 0.0001);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP1() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double map = e.evaluateMAP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP2() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double map = e.evaluateMAP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP3() {
        Evaluator e = new Evaluator("resources/clone_clusters.csv", mode, outputDir, true);
        double map = e.evaluateMAP("resources/example_results_all_H.csv", 10);
        assertEquals(1.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }
}
