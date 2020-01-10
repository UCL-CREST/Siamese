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

package crest.siamese.helpers;

/**
 * Created by Chaiyong on 7/17/17.
 */

import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class MethodLevelEvaluatorTest {

    private String mode = "test";
    private String outputDir = "results";

    @Test
    public void TestARP1() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @Test
    public void TestARP2() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, arp);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @Test
    public void TestARP3() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double arp = e.evaluateARP("resources/example_results_all_H.csv", 10);
        assertEquals(1.0, arp, 0.0001);

        // delete the result file
        File resultFile = new File(outputDir + "/rprec_" + mode + ".csv");
        resultFile.delete();
    }

    @Test
    public void TestMAP1() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double map = e.evaluateMAP("resources/example_results_all_T.csv", 10);
        assertEquals(1.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @Test
    public void TestMAP2() {
        MethodLevelEvaluator e = new MethodLevelEvaluator("resources/cloplag_clone_clusters_METHOD-LEVEL.csv", mode, outputDir, true);
        e.generateSearchKey();
        double map = e.evaluateMAP("resources/example_results_all_F.csv", 10);
        assertEquals(0.0, map);

        // delete the result file
        File resultFile = new File(outputDir + "/map_" + mode + ".csv");
        resultFile.delete();
    }

    @Test
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
