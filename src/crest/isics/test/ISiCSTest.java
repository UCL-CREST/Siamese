package crest.isics.test;
import crest.isics.document.JavaTerm;
import crest.isics.main.ISiCS;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ISiCSTest {

    @Test
    public void testFindMedian() {
        ISiCS isics = new ISiCS("config.properties");

        JavaTerm jt1 = new JavaTerm("A", 30);
        JavaTerm jt2 = new JavaTerm("B", 20);
        JavaTerm jt3 = new JavaTerm("C", 10);
        JavaTerm jt4 = new JavaTerm("D", 5);

        ArrayList<JavaTerm> termList = new ArrayList<>();
        termList.add(jt1);
        termList.add(jt2);
        termList.add(jt3);
        termList.add(jt4);

        double[] stats = isics.findMedianLoc(termList);
        assertEquals(27.5, stats[0], 0.00001);
        assertEquals(15.0, stats[1], 0.00001);
        assertEquals(6.25, stats[2], 0.00001);

        JavaTerm jt5 = new JavaTerm("D", 1);
        termList.add(jt5);
        stats = isics.findMedianLoc(termList);
        assertEquals(25, stats[0], 0.00001);
        assertEquals(10, stats[1], 0.00001);
        assertEquals(3, stats[2], 0.00001);
    }
}
