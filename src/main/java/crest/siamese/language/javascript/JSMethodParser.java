package crest.siamese.language.javascript;


import crest.siamese.document.Method;
import crest.siamese.language.MethodParser;
import crest.siamese.settings.Settings;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class JSMethodParser implements MethodParser {

    private String FILE_PATH;
    private String MODE;


    /**
     * Constructor that refer to parent Object class. Required for calling Class::newInstance.
     */
    public JSMethodParser() {
        super();
    }

    /**
     * Initialises a JSMethodParser
     *
     * @param filePath       Location of input file
     * @param prefixToRemove Legacy parameter
     * @param mode           Method level or Class level parsing
     * @param isPrint        Legacy parameter
     */
    public JSMethodParser(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        this.FILE_PATH = filePath;
        this.MODE = mode;
    }

    /**
     * License extraction is not implemented for JavaScript
     *
     * @return null
     */
    @Override
    public String getLicense() {
        return null;
    }

    /**
     * Configures constructor attributes when initialised with Class.newInstance() in Siamese.java
     *
     * @param filePath       Location of input file
     * @param prefixToRemove Legacy parameter
     * @param mode           Method level or Class level parsing
     * @param isPrint        Legacy parameter
     */
    @Override
    public void configure(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        this.FILE_PATH = filePath;
        this.MODE = mode;
    }


    /**
     * Uses ANTLR4 generated Parser and Lexer to extract methods from JavaScript source code file
     *
     * @return ArrayList of methods extracted from FILE_PATH attribute
     */
    @Override
    public ArrayList<Method> parseMethods() {
        ArrayList<Method> methods = new ArrayList<>();
        File file = new File(this.FILE_PATH);
        String sourceCode = readFile(file);

        if (MODE.equals(Settings.MethodParserType.METHOD)) {

            methods.addAll(getJSMethodBlock(file, sourceCode));
        } else {
            methods.addAll(getJSFileBlockMethod(file, sourceCode));

        }
        return methods;
    }

    private static List<Method> getJSMethodBlock(File sourceFile, String jsSourceCode) {
        JavaScriptParser parser = new Builder.Parser(jsSourceCode).build();
        List<Method> methods = new ArrayList<>();
        try {
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            methods.addAll(jsParseTreeListener.getJSMethods());
            return methods;
        } catch (Exception e) {
            System.err.println("Source file->" + sourceFile.getPath() + "-cannot be parsed ");
        }
        return methods;

    }

    private static List<Method> getJSFileBlockMethod(File sourceFile, String jsSourceCode) {
        JavaScriptParser parser = new Builder.Parser(jsSourceCode).build();
        List<Method> methods = new ArrayList<>();
        try {
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            methods.add(jsParseTreeListener.getFileBlockMethod());
            return methods;
        } catch (Exception e) {
            System.err.println("Source file->" + sourceFile.getPath() + "-cannot be parsed ");
        }
        return methods;

    }


    private static String readFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }


}
