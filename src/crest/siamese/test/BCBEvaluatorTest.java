package crest.siamese.test;

import crest.siamese.document.BCBDocument;
import crest.siamese.helpers.BCBEvaluator;
import org.junit.Test;

import java.util.ArrayList;

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
