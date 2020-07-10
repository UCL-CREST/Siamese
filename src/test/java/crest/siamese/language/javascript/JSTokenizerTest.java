package crest.siamese.language.javascript;

import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;


public class JSTokenizerTest {

    final String DUMMY_FILE_PATH = "crest/siamese/language/javascript/DemoTest.js";
    final String TEST_SOURCE = "function (a,b){ return a+b;} ";
    final ArrayList<String> TOKENIZED_SOURCE = new ArrayList<>(Arrays.asList(
            "function", "(", "a", ",", "b", ")", "{", "return", "a", "+", "b", ";", "}"
    ));

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
        resourceSourceFile = new File(Objects.requireNonNull(classLoader.getResource(DUMMY_FILE_PATH)).getFile());

    }

    @Test
    public void getTokensFromStringTest() {
        ArrayList<String> actual = jsTokenizer.getTokensFromString(TEST_SOURCE);
        assertEquals(actual, TOKENIZED_SOURCE);

    }

    @Test
    public void testTokenize() throws IOException {
        Reader reader = new FileReader(resourceSourceFile);
        assertEquals(0, jsTokenizer.tokenize(TEST_SOURCE).size());
        assertEquals(0, jsTokenizer.tokenize(reader).size());
        assertEquals(0, jsTokenizer.tokenizeLine(reader).size());
        assertEquals(0, jsTokenizer.tokenize(resourceSourceFile).size());
        assertEquals(0, jsTokenizer.getTokensFromFile(resourceSourceFile.getAbsolutePath()).size());

    }

}
