package crest.siamese.test;

import static org.junit.Assert.*;
import crest.siamese.helpers.MethodParser;
import crest.siamese.document.Method;
import crest.siamese.settings.Settings;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class JavaMethodParserTest {

    @org.junit.Test
    public void checkMethodParser() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("crest.siamese.helpers.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(3, methods.size());
        assertEquals("ReadWordsFromFile", methods.get(0).getName());
        assertEquals("ReadString", methods.get(1).getName());
        assertEquals("main", methods.get(2).getName());
    }

    @org.junit.Test
    public void checkMethodParserFileLevel() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("crest.siamese.helpers.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.FILE, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("method", methods.get(0).getName());
    }

    @org.junit.Test
    public void checkMethodParser2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("crest.siamese.helpers.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("main", methods.get(0).getName());
    }

    @org.junit.Test
    public void testExtractingComment() throws Exception {
        String srcStr = "resources/tests/WritableComparable.java";
        Class cl = Class.forName("crest.siamese.helpers.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }

    @org.junit.Test
    public void testExtractingComment2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("crest.siamese.helpers.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }
}
