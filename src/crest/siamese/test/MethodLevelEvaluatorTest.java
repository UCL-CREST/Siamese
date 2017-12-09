package crest.siamese.test;

/**
 * Created by Chaiyong on 7/17/17.
 */
import crest.siamese.helpers.MethodLevelEvaluator;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class MethodLevelEvaluatorTest {

    private String mode = "test";
    private String outputDir = "results";

    @org.junit.Test
    public void TestARP1() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestARP2() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestARP3() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_H.csv", 10);
        assertEquals(1.0, arp, 0.0001);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP1() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double map = e.evaluateMAP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP2() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double map = e.evaluateMAP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @org.junit.Test
    public void TestMAP3() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double map = e.evaluateMAP("resources/example_results_all_H.csv", 10);
        assertEquals(1.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }
}
