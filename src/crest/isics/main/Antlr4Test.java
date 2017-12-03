package crest.isics.main;

import crest.isics.helpers.JavaLexer;
import crest.isics.helpers.JavaTokenizer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Antlr4Test {
    public static void main(String[] args) {
        JavaTokenizer tokenizer = new JavaTokenizer();
        try {
//            tokenizer.tokenize(
//                    "int x = 0; double y = 0.4; float z = 0.005; boolean t = true;");
//            tokenizer.tokenize(new File("/Users/Chaiyong/Documents/phd/2016/cloplag/tests/bubblesort/0_orig/BubbleSort.java"));
            tokenizer.tokenize("( ) { } [ ] ; , . = > < ! ~ ? : == <= >= != && || ++ -- + - * / & | ^ % +=" +
                    "-= *= /= &= |= ^= %= <<= >>= >>>= -> :: @ ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
