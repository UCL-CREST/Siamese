package crest.siamese.test;
import crest.siamese.document.JavaTerm;
import crest.siamese.main.Siamese;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SiameseTest {

    @Test
    public void testFindMedian() {
        Siamese siamese = new Siamese("config.properties");

        JavaTerm jt1 = new JavaTerm("A", 30);
        JavaTerm jt2 = new JavaTerm("B", 20);
        JavaTerm jt3 = new JavaTerm("C", 10);
        JavaTerm jt4 = new JavaTerm("D", 5);

        ArrayList<JavaTerm> termList = new ArrayList<>();
        termList.add(jt1);
        termList.add(jt2);
        termList.add(jt3);
        termList.add(jt4);

        double stats = siamese.getValueAtPercentile(termList, 25);
        assertEquals(27.5, stats, 0.00001);
        stats = siamese.getValueAtPercentile(termList, 50);
        assertEquals(15.0, stats, 0.00001);
        stats = siamese.getValueAtPercentile(termList, 75);
        assertEquals(6.25, stats, 0.00001);

        JavaTerm jt5 = new JavaTerm("D", 1);
        termList.add(jt5);
        stats = siamese.getValueAtPercentile(termList,25);
        assertEquals(25, stats, 0.00001);
        stats = siamese.getValueAtPercentile(termList, 50);
        assertEquals(10, stats, 0.00001);
        stats = siamese.getValueAtPercentile(termList, 75);
        assertEquals(3, stats, 0.00001);
    }
}
