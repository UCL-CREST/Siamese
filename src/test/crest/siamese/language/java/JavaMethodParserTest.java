package crest.siamese.language.java;

import crest.siamese.document.Method;
import crest.siamese.language.MethodParser;
import crest.siamese.settings.Settings;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class JavaMethodParserTest {

    @Test
    public void checkMethodParser() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(3, methods.size());
        assertEquals("ReadWordsFromFile", methods.get(0).getName());
        assertEquals("ReadString", methods.get(1).getName());
        assertEquals("main", methods.get(2).getName());
    }

    @Test
    public void checkMethodParserFileLevel() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.FILE, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("method", methods.get(0).getName());
    }

    @Test
    public void checkMethodParser2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("main", methods.get(0).getName());
    }

    @Test
    public void testExtractingComment() throws Exception {
        String srcStr = "resources/tests/WritableComparable.java";
        Class cl = Class.forName("JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }

    @Test
    public void testExtractingComment2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }
}
