package crest.siamese.test;

import crest.siamese.helpers.JavaNormalizer;
import crest.siamese.helpers.JavaTokenizer;
import crest.siamese.helpers.nGramGenerator;
import crest.siamese.settings.Settings;
import crest.siamese.settings.NormalizerMode;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Chaiyong on 7/27/16.
 */
public class JavaNormalizerTest {

    @org.junit.Test
    public void checkTokenizationFromString() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

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
        NormalizerMode mode = new NormalizerMode();
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "!= += ++ -- *= /= <= >= == () {}");

        String[] expectedTokens = {"!=", "+=", "++", "--", "*=", "/=", "<=", ">=", "==", "(", ")", "{", "}"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + "|");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkOperators() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setOperator(Settings.Normalize.OPERATOR_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));
        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "!= += ++ -- *= /= <= >= == () {}");

        String[] expectedTokens = {"O", "O", "O", "O", "O", "O", "O", "O", "O", "(", ")", "{", "}"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + "|");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkDatatypeNormalisation() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static void main ( String[] args ) { int x = 10;");

        String[] expectedTokens = {"public", "static", "void", "main", "(", "String", "[", "]"
                , "args", ")", "{", "D", "x", "=", "10", ";"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(expectedTokens[i], tokens.get(i));
        }
    }

    @org.junit.Test
    public void checkJavaClassNormalisation() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

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
        NormalizerMode mode = new NormalizerMode();
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

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
        NormalizerMode mode = new NormalizerMode();
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString("int x = y; MyVar");
        String[] expectedTokens = {"int", "W", "=", "W", ";", "W"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }

    @org.junit.Test
    public void checkStringNormalisation() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString("String x = \"Hello world!\"");
        String[] expectedTokens = {"String", "x", "=", "S"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }

    @org.junit.Test
    public void checkDJKPSVWNormalisation() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        mode.setValue(Settings.Normalize.VALUE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens =
                tokenizer.getTokensFromString("while (true) { int y = 0; String x = \"Hello world!\"; }");
        String[] expectedTokens = {"K", "(", "V", ")", "{", "D", "W", "=", "V", ";"
                , "J", "W", "=", "S", ";", "}"};

        for (int i=0; i<tokens.size(); i++) {
            System.out.print(tokens.get(i) + " ");
            assertEquals(tokens.get(i), expectedTokens[i]);
        }
    }

    @org.junit.Test
    public void checkDJKPSVWNormalisation2() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        mode.setValue(Settings.Normalize.VALUE_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static long checksum(File file) throws IOException {\n" +
                        "    CRC32 crc = new CRC32();\n" +
                        "    FileReader fr = new FileReader(file);\n" +
                        "    int data;\n" +
                        "    while((data = fr.read()) != -1) {\n" +
                        "        crc.update(data);\n" +
                        "    }\n" +
                        "    fr.close();\n" +
                        "    return crc.getValue();\n" +
                        "}");

        nGramGenerator gen = new nGramGenerator(4);
        ArrayList<String> grams = gen.generateNGramsFromJavaTokens(tokens);

        for (int i=0; i<grams.size(); i++) {
            System.out.print(grams.get(i) + " ");
        }
    }

    @org.junit.Test
    public void checkDJKOPSVWNormalisation() throws Exception {
        NormalizerMode mode = new NormalizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        mode.setValue(Settings.Normalize.VALUE_NORM_ON);
        mode.setOperator(Settings.Normalize.OPERATOR_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString(
                "public static long checksum(File file) throws IOException {\n" +
                        "    CRC32 crc = new CRC32();\n" +
                        "    FileReader fr = new FileReader(file);\n" +
                        "    int data;\n" +
                        "    while((data = fr.read()) != -1) {\n" +
                        "        crc.update(data);\n" +
                        "    }\n" +
                        "    fr.close();\n" +
                        "    return crc.getValue();\n" +
                        "}");

        nGramGenerator gen = new nGramGenerator(4);
        ArrayList<String> grams = gen.generateNGramsFromJavaTokens(tokens);

        String[] expected = {"KKDW", "KDW(", "DW(J", "W(JP", "(JP)", "JP)K",
                "P)KJ", ")KJ{", "KJ{J", "J{JW", "{JWO", "JWOK", "WOKJ",
                "OKJ(", "KJ()", "J();", "();J", ");JW", ";JWO", "JWOK",
                "WOKJ", "OKJ(", "KJ(P", "J(P)", "(P);", "P);D", ");DW",
                ";DW;", "DW;K", "W;K(", ";K((", "K((W", "((WO", "(WOW",
                "WOW.", "OW.W", "W.W(", ".W()", "W())", "())O", "))OO",
                ")OOV", "OOV)", "OV){", "V){W", "){W.", "{W.W", "W.W(",
                ".W(W", "W(W)", "(W);", "W);}", ");}W", ";}W.", "}W.W",
                "W.W(", ".W()", "W();", "();K", ");KW", ";KW.", "KW.W",
                "W.W(", ".W()", "W();", "();}"};
        for (int i=0; i<expected.length; i++) {
            System.out.print(grams.get(i) + "|");
            assertEquals(expected[i], grams.get(i));
        }
    }

    @org.junit.Test
    public void TestDefaultNormalizeMode() throws Exception {
        NormalizerMode mode = new NormalizerMode();
//        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
//        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
//        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
//        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
//        mode.setString(Settings.Normalize.STRING_NORM_ON);
//        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        JavaTokenizer tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        ArrayList<String> tokens = tokenizer.getTokensFromString("public static long checksum(File file) throws IOException {\n" +
                "    CRC32 crc = new CRC32();\n" +
                "    FileReader fr = new FileReader(file);\n" +
                "    int data;\n" +
                "    while((data = fr.read()) != -1) {\n" +
                "        crc.update(data);\n" +
                "    }\n" +
                "    fr.close();\n" +
                "    return crc.getValue();\n" +
                "}");

        // initialise the n-gram generator
        nGramGenerator ngen = new nGramGenerator(5);
        ArrayList<String> ntokens = ngen.generateNGramsFromJavaTokens(tokens);

        System.out.println(ntokens.size());
        for (int i=0; i<ntokens.size(); i++) {
            System.out.print(ntokens.get(i) + " ");
        }

        System.out.println("\n\n");

        mode = new NormalizerMode();
        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
//        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
//        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
//        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setValue(Settings.Normalize.VALUE_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        tokens = tokenizer.getTokensFromString("public static long checksum(File file) throws IOException {\n" +
                "    CRC32 crc = new CRC32();\n" +
                "    FileReader fr = new FileReader(file);\n" +
                "    int data;\n" +
                "    while((data = fr.read()) != -1) {\n" +
                "        crc.update(data);\n" +
                "    }\n" +
                "    fr.close();\n" +
                "    return crc.getValue();\n" +
                "}");

        // initialise the n-gram generator
        ngen = new nGramGenerator(5);
        ntokens = ngen.generateNGramsFromJavaTokens(tokens);

        System.out.println(ntokens.size());
        for (int i=0; i<ntokens.size(); i++) {
            System.out.print(ntokens.get(i) + " ");
        }

        System.out.println("\n\n");

        mode.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
        mode.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
        mode.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
        mode.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
        mode.setString(Settings.Normalize.STRING_NORM_ON);
        mode.setWord(Settings.Normalize.WORD_NORM_ON);
        tokenizer = new JavaTokenizer(new JavaNormalizer(mode));

        tokens = tokenizer.getTokensFromString("public static long checksum(File file) throws IOException {\n" +
                "    CRC32 crc = new CRC32();\n" +
                "    FileReader fr = new FileReader(file);\n" +
                "    int data;\n" +
                "    while((data = fr.read()) != -1) {\n" +
                "        crc.update(data);\n" +
                "    }\n" +
                "    fr.close();\n" +
                "    return crc.getValue();\n" +
                "}");

        // initialise the n-gram generator
        ngen = new nGramGenerator(5);
        ntokens = ngen.generateNGramsFromJavaTokens(tokens);

        System.out.println(ntokens.size());
        for (int i=0; i<ntokens.size(); i++) {
            System.out.print(ntokens.get(i) + " ");
        }
    }
}
