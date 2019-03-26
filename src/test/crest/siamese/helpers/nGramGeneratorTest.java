package crest.siamese.helpers;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
/**
 * Created by Chaiyong on 7/27/16.
 */
public class nGramGeneratorTest {
    @Test
    public void checkFourGrams() {
        nGramGenerator gen = new nGramGenerator(4);
        String[] grams = gen.generateNGrams("hello world!");
        String[] expectedGrams = {"hell", "ello", "llo ", "lo w", "o wo", " wor", "worl", "orld", "rld!"};
        assertArrayEquals(grams, expectedGrams);
    }

    @Test
    public void checkFourGramsFromJavaTokens() {
        nGramGenerator gen = new nGramGenerator(2);
        ArrayList<String> javaTokens = new ArrayList<>();
        javaTokens.add("public");
        javaTokens.add("static");
        javaTokens.add("void");
        javaTokens.add("main");
        javaTokens.add("(");
        javaTokens.add("String");
        javaTokens.add("[");
        javaTokens.add("]");
        javaTokens.add("args");
        javaTokens.add(")");

        ArrayList<String> grams = gen.generateNGramsFromJavaTokens(javaTokens);
        String[] expectedGrams = {"publicstatic", "staticvoid", "voidmain", "main(", "(String", "String["
                , "[]", "]args", "args)"};

        for (int i=0; i<grams.size(); i++) {
            assertEquals(grams.get(i), expectedGrams[i]);
        }
    }
}
