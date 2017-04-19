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
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        MethodParser mParser = new MethodParser(srcStr, "");
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(methods.size(), 3);
        assertEquals(methods.get(0).getName(), "ReadWordsFromFile");
        assertEquals(methods.get(1).getName(), "ReadString");
        assertEquals(methods.get(2).getName(), "main");
    }

    @org.junit.Test
    public void checkMethodParser2() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        MethodParser mParser = new MethodParser(srcStr, "");
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(methods.size(), 3);
        assertEquals(methods.get(0).getName(), "ReadWordsFromFile");
        assertEquals(methods.get(1).getName(), "ReadString");
        assertEquals(methods.get(2).getName(), "main");
    }
}
