package crest.siamese.experiment;

import crest.siamese.language.java.JavaTokenizer;

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
