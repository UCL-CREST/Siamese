package crest.siamese.helpers;

import crest.siamese.settings.NormalizerMode;
import crest.siamese.settings.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaNormalizer implements Normalizer {

    private HashMap<String, Integer> keywordMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> datatypeMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> symbolMap = new HashMap<>();
    private HashMap<String, Integer> javaClassMap = new HashMap<String, Integer>();
    private HashMap<String, Integer> javaPackagesMap = new HashMap<String, Integer>();
    private NormalizerMode modes = new NormalizerMode();

    public JavaNormalizer(NormalizerMode modes) {
        this.modes = modes;
        setUpKeywordMap();
        setUpSymbolMap();
        setUpDatatypeMap();
        readJavaClassNames(Settings.Normalizer.JAVA_CLASS_FILE);
        readJavaPackages(Settings.Normalizer.JAVA_PACKAGES_FILE);
    }

    public JavaNormalizer() {
        super();
        setUpKeywordMap();
        setUpSymbolMap();
        setUpDatatypeMap();
        readJavaClassNames(Settings.Normalizer.JAVA_CLASS_FILE);
        readJavaPackages(Settings.Normalizer.JAVA_PACKAGES_FILE);
    }

    @Override
    public String normalizeAToken(String token, String type) throws Exception {
        if (datatypeMap.get(token) != null) {
            // data type = D
            if (modes.getDatatype() == Settings.Normalize.DATATYPE_NORM_ON)
                return "D";
            else
                return token;
        } else if (keywordMap.get(type.toLowerCase()) != null) {
            // keyword = K
            if (modes.getKeyword() == Settings.Normalize.KEYWORD_NORM_ON)
                return "K";
            else
                return token;
        } else if (type.equals("DECIMAL_LITERAL") || type.equals("FLOAT_LITERAL") || type.equals("BOOL_LITERAL")) {
            // decimal value = V
            if (modes.getValue() == Settings.Normalize.VALUE_NORM_ON)
                return "V";
            else
                return token;
        } else if (type.equals("STRING_LITERAL")) {
            // string = S
            if (modes.getString() == Settings.Normalize.STRING_NORM_ON)
                return "S";
            else
                return token;
        } else if (javaClassMap.get(token) != null) {
            // java class = J
            if (modes.getJavaClass() == Settings.Normalize.JAVACLASS_NORM_ON)
                return "J";
            else
                return token;
        } else if (javaPackagesMap.get(token) != null) {
            // java packages = P
            if (modes.getJavaPackage() == Settings.Normalize.JAVAPACKAGE_NORM_ON)
                return "P";
            else
                return token;
        } else if (symbolMap.get(token) != null) {
            // symbol, skip
            return token;
        } else if (type.equals("WS")) {
            // white space, skip
            return "";
        } else {
            // other = word = W
            if (modes.getWord() == Settings.Normalize.WORD_NORM_ON)
                return "W";
            else
                return token;
        }
    }

    @Override
    public ArrayList<String> noNormalizeAToken(String token) throws Exception {
        return null;
    }

    private void setUpKeywordMap() {
        String[] keywords = { "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package",
                "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements",
                "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
                "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface",
                "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native",
                "super", "while" };
        for (int i = 0; i < keywords.length; i++) {
            keywordMap.put(keywords[i], 1);
        }
    }

    private void setUpSymbolMap() {
        String[] symbols = {"(", ")", "{", "}", "[", "]", ";", ",", ".", "=", ">", "<", "!", "~", "?", ":",
                "==", "<=", ">=", "!=", "&&", "||", "++", "--", "+", "-", "*", "/", "&", "|", "^", "%", "+=",
                "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=", ">>=", ">>>=", "->", "::", "@", "..."};
        for (int i = 0; i < symbols.length; i++) {
            symbolMap.put(symbols[i], 1);
        }
    }

    private HashMap<String, Integer> readToMap(String filepath) {
        HashMap<String, Integer> map = new HashMap<>();
        File file = new File(filepath);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                map.put(line, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public void readJavaClassNames(String filepath) {
        javaClassMap = readToMap(filepath);
    }

    public void readJavaPackages(String filepath) {
        javaPackagesMap = readToMap(filepath);
    }

    public void setUpDatatypeMap() {
        datatypeMap.put("byte", 1);
        datatypeMap.put("short", 1);
        datatypeMap.put("int", 1);
        datatypeMap.put("long", 1);
        datatypeMap.put("float", 1);
        datatypeMap.put("double", 1);
        datatypeMap.put("boolean", 1);
        datatypeMap.put("char", 1);
    }

    public boolean isKeyword(String x) {
        // System.out.println(x);
        if (keywordMap.get(x) != null)
            return true;
        else
            return false;
    }
}
