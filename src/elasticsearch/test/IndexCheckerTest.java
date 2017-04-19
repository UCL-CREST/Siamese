package elasticsearch.test;

import elasticsearch.main.IndexChecker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Chaiyong on 4/4/17.
 */
public class IndexCheckerTest {
    @Test
    public void powersetTest() {
        Set<String> intSet = new HashSet<String>();
        intSet.add("public");
        intSet.add("static");
        intSet.add("void");
        IndexChecker ic = new IndexChecker();
        Set<Set<String>> powerSet = ic.powerSet(intSet);
        assertEquals(8, powerSet.size());
    }

    @Test
    public void generate2NQueryTest() {
        String source = "BufferedReader reader = new BufferedReader(input);";
        IndexChecker ic = new IndexChecker();
        ic.setOutputFolder("results/170404/");
        try {
            ArrayList<String> tokens = ic.tokenizeStringToArray(ic.tokenize(source));
            System.out.println("Array size: " + tokens.size());
            ArrayList<String> queries = ic.generate2NQuery(tokens);
            System.out.println("Size: " + queries.size());
            for (String q: queries) {
                System.out.println("Query: " + q);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
