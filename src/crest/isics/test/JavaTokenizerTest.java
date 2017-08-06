package crest.isics.test;

import crest.isics.helpers.JavaTokenizer;
import crest.isics.settings.Settings;
import crest.isics.settings.TokenizerMode;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Chaiyong on 7/27/16.
 */
public class JavaTokenizerTest {

    @org.junit.Test
    public void checkTokenizationFromString() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static void main ( String[] args ) { String inpstring = \"hello\";");

        String[] expectedTokens = {"public", "static", "void", "main", "(", "String", "[", "]"
                , "args", ")", "{", "String", "inpstring", "=", "\"hello\"", ";"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkSpecialCharacters() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "!= += ++ -- *= /= <= >= == () {}");

        String[] expectedTokens = {"!=", "+=", "++", "--", "*=", "/=", "<=", ">=", "==", "(", ")", "{", "}"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + "|");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkDatatypeNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static void main ( String[] args ) { int x = 10;");

        String[] expectedTokens = {"public", "static", "void", "main", "(", "String", "[", "]"
                , "args", ")", "{", "D", "x", "=", "V", ";"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkJavaClassNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static void main ( String[] args ) { ArrayList<String> x = new ArrayList<>();");

        String[] expectedTokens = {"public", "static", "void", "main", "(", "J", "[", "]"
                , "args", ")", "{", "J", "<", "J", ">", "x", "=", "new", "J", "<", ">"
                , "(", ")", ";"};

//        ArrayList<String> tokens = tokenizer.getTokensFromString("Arrays");
//        String[] expectedTokens = {"J"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " "); // + ":" + expectedTokens[i]);
            assertEquals(expectedTokens[i],tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkJavaPackageNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString("Dynamic\n" +
                "DynamicAny\n" +
                "IOP\n" +
                "Messaging\n" +
                "NamingContextExtPackage\n" +
                "NamingContextPackage\n" +
                "ORBInitInfoPackage\n" +
                "ORBPackage");
        String[] expectedTokens = {"P", "P", "P", "P", "P", "P", "P", "P"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkWordNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString("int x = y; MyVar");
        String[] expectedTokens = {"int", "W", "=", "W", ";", "W"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }

    @org.junit.Test
    public void checkStringNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString("String x = \"Hello world!\"");
        String[] expectedTokens = {"String", "x", "=", "S"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }

    @org.junit.Test
    public void checkDJKPWSNormalisation() throws Exception {
        TokenizerMode mode = new TokenizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(mode);

        ArrayList<String> tokens = tokenizer.getTokensFromString("while (true) { int y = 0; String x = \"Hello world!\"; }");
        String[] expectedTokens = {"K", "(", "W", ")", "{", "D", "W", "=", "V", ";"
                , "J", "W", "=", "S", ";", "}"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }
}
