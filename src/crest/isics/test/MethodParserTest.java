package crest.isics.test;

import static org.junit.Assert.*;
import crest.isics.helpers.MethodParser;
import crest.isics.document.Method;
import crest.isics.settings.Settings;

import java.util.ArrayList;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class MethodParserTest {

    @org.junit.Test
    public void checkMethodParser() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        MethodParser mParser = new MethodParser(srcStr, "", Settings.MethodParserType.METHOD);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(3, methods.size());
        assertEquals("ReadWordsFromFile", methods.get(0).getName());
        assertEquals("ReadString", methods.get(1).getName());
        assertEquals("main", methods.get(2).getName());
    }

    @org.junit.Test
    public void checkMethodParserFileLevel() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        MethodParser mParser = new MethodParser(srcStr, "", Settings.MethodParserType.FILE);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("method", methods.get(0).getName());
    }

    @org.junit.Test
    public void checkMethodParser2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        MethodParser mParser = new MethodParser(srcStr, "", Settings.MethodParserType.METHOD);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("main", methods.get(0).getName());
    }
}
