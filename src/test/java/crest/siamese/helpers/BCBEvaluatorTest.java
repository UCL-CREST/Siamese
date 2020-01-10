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

import crest.siamese.document.BCBDocument;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

/**
 * TODO: @Chaiyong
 * This test class is ignored until resolved.
 * Maven could not run these tests successfully at compile time because it requires BCB database connection.
 * Is this test class necessary for compile time?
 * Is this component core to the clone search functionality?
 */
@Ignore
public class BCBEvaluatorTest {

    @Test
    public void testConstructor() {
        BCBEvaluator evaluator = new BCBEvaluator();
    }

    @Test
    public void testGetType1Clones() {
        BCBEvaluator evaluator = new BCBEvaluator();
        ArrayList<Integer> cloneList = evaluator.getCloneIds(1000, 10, 10);
        System.out.println(cloneList.size());
        evaluator.closeDBConnection();
    }

    @Test
    public void testGetType1CloneGroup() {
        BCBEvaluator evaluator = new BCBEvaluator();
        ArrayList<Integer> cloneList = evaluator.getCloneIds(1000, 10, 10);
        for (int clone: cloneList) {
            System.out.println("===============================");
            ArrayList<BCBDocument> results = evaluator.getCloneGroup(clone, 10);
            for (BCBDocument d: results) {
                System.out.println(d.getLocationString());
            }
        }
        evaluator.closeDBConnection();
    }
}
