package elasticsearch.test;

import static org.junit.Assert.*;
import elasticsearch.main.MethodParser;
import elasticsearch.document.Method;

import java.util.ArrayList;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class MethodParserTest {

    @org.junit.Test
    public void checkMethodParser() throws Exception {
        MethodParser mParser = new MethodParser();

        String srcStr = "/Users/Chaiyong/Documents/cloplag/tests/guessword/0_orig/GuessWord.java";

        ArrayList<Method> methods = mParser.parseMethods(srcStr);

        assertEquals(methods.size(), 3);
        assertEquals(methods.get(0).getName(), "ReadWordsFromFile");
        assertEquals(methods.get(1).getName(), "ReadString");
        assertEquals(methods.get(2).getName(), "main");
    }

    @org.junit.Test
    public void checkMethodParser2() throws Exception {
        MethodParser mParser = new MethodParser();

        String srcStr = "/Users/Chaiyong/Downloads/stackoverflow/stackoverflow_formatted/10135525_0.java";

        ArrayList<Method> methods = mParser.parseMethods(srcStr);

        assertEquals(methods.size(), 3);
        assertEquals(methods.get(0).getName(), "ReadWordsFromFile");
        assertEquals(methods.get(1).getName(), "ReadString");
        assertEquals(methods.get(2).getName(), "main");
    }
}
