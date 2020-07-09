package crest.siamese.language.javascript;

import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


public class JSTokenizerTest {
    String dummyFilePath = "crest/siamese/language/javascript/DemoTest.js";
    String input = "function (a,b){ return a+b;} ";
    JSTokenizer jsTokenizer;
    File resourceSourceFile;

    @Before
    public void init() {
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        JSNormalizer jsNormalizer = new JSNormalizer();
        jsNormalizer.configure(jsNormalizerMode);
        jsTokenizer = new JSTokenizer();
        jsTokenizer.configure(jsNormalizer);
        ClassLoader classLoader = getClass().getClassLoader();
        resourceSourceFile = new File(classLoader.getResource(dummyFilePath).getFile());

    }

    @Test
    public void getTokensFromStringTest() {
        ArrayList<String> actual = jsTokenizer.getTokensFromString(input);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "function", "(", "a", ",", "b", ")", "{", "return", "a", "+", "b", ";", "}"
        ));
        assertEquals(actual, expected);

    }

    @Test
    public void testTokenize() throws IOException {
        Reader reader = new FileReader(resourceSourceFile);
        assertEquals(0, jsTokenizer.tokenize(input).size());
        assertEquals(0, jsTokenizer.tokenize(reader).size());
        assertEquals(0, jsTokenizer.tokenizeLine(reader).size());
        assertEquals(0, jsTokenizer.tokenize(resourceSourceFile).size());
        assertEquals(0, jsTokenizer.getTokensFromFile(resourceSourceFile.getAbsolutePath()).size());

    }

}
