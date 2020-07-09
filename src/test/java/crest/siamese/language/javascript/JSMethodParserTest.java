package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.*;

public class JSMethodParserTest {

    String fileName = "crest/siamese/language/javascript/All-Combination-Of-JS-Functions.js";
    String syntaxErrorFileName = "crest/siamese/language/javascript/Syntax-Error.js";
    public static String METHOD_MOOD = "METHOD-LEVEL";
    public static String FILE_MOOD = "FILE-LEVEL";


    @Test
    public void parseMethodsMethodLevelTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        //  MOOD = "METHOD-LEVEL";
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(file.getAbsolutePath(), StringUtils.EMPTY, METHOD_MOOD, false);
        ArrayList<Method> methods = jsMethodParser.parseMethods();
        assertEquals(methods.size(), 30);

    }

    @Test
    public void parseMethodsFileLevelTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
        //  MOOD = "FILE-LEVEL";
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(file.getAbsolutePath(), StringUtils.EMPTY, FILE_MOOD, false);
        ArrayList<Method> methods = jsMethodParser.parseMethods();
        assertEquals(methods.size(), 1);

    }

    @Test
    public void getParsedTreeTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(file.getAbsolutePath(), StringUtils.EMPTY, METHOD_MOOD, false);

        ParseTree parseTree = jsMethodParser.getParsedTree(file);
        assertNotNull(parseTree);


    }

    @Test
    public void getParsedTreeForSyntaxErrorFileTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File syntaxErrorSourceFile = new File(classLoader.getResource(syntaxErrorFileName).getFile());
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(syntaxErrorSourceFile.getAbsolutePath(), StringUtils.EMPTY, METHOD_MOOD, false);
        ParseTree parseTree = jsMethodParser.getParsedTree(syntaxErrorSourceFile);
        assertNotNull(parseTree);
    }

    @Test
    public void getTraversedJSParseTreeListenerTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(file.getAbsolutePath(), StringUtils.EMPTY, METHOD_MOOD, false);
        JSParseTreeListener jsParseTreeListener = jsMethodParser.getTraversedJSParseTreeListener(file);
        assertNotNull(jsParseTreeListener);
    }

    @Test
    public void getJavaScriptParserTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(file.getAbsolutePath(), StringUtils.EMPTY, METHOD_MOOD, false);
        JavaScriptParser jsParser = jsMethodParser.getJavaScriptParser(file);
        assertNotNull(jsParser);
    }

    @Test
    public void getSourceAsCharStreamsInvalidFileTest() {
        JSMethodParser jsMethodParser = new JSMethodParser();
        File dummyFile = new File("Demo.js");
        assertTrue(jsMethodParser.getSourceAsCharStreams(dummyFile) instanceof CharStream);
    }

    @Test
    public void configureTest() {
        JSMethodParser jsMethodParser = new JSMethodParser();
        jsMethodParser.configure(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false);
        assertNull(jsMethodParser.getLicense());
    }

    @Test
    public void getLicenseTest() {
        JSMethodParser jsMethodParser = new JSMethodParser();
        assertNull(jsMethodParser.getLicense());
        JSMethodParser jsmp = new JSMethodParser(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false);
        assertNull(jsmp.getLicense());
    }

}
