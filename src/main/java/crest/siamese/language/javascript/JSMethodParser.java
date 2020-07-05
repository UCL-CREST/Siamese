package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import crest.siamese.language.MethodParser;
import crest.siamese.settings.Settings;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class responsible for extracting all the JavaScript function block as Method objects
 */
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
        if (MODE.equals(Settings.MethodParserType.METHOD)) {
            methods.addAll(getJSMethodBlock(file));
        } else {
            methods.addAll(getJSFileBlockMethod(file));
        }
        return methods;
    }

    /**
     * Create a list of Methods for a given JavaScript source file.
     *
     * @param sourceFile JavaScript source file
     * @return A list of JavaScript Methods extracted from the given JavaScript source file
     */
    private List<Method> getJSMethodBlock(File sourceFile) {
        List<Method> methods = new ArrayList<>();
        try {
            CharStream sourceStream = getSourceAsCharStreams(sourceFile);
            JavaScriptParser parser = new Builder.Parser(sourceStream).build();
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            methods.addAll(jsParseTreeListener.getJSMethods());
            return methods;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Parsing Errors Occurs at source file: " + sourceFile.getPath());
        }
        return methods;

    }

    /**
     * Create a Method as File Block for a given JavaScript source file.
     *
     * @param sourceFile JavaScript source file
     * @return A list of JavaScript Methods extracted as a File Block (contains a single File Block Method)
     */
    private List<Method> getJSFileBlockMethod(File sourceFile) {
        List<Method> methods = new ArrayList<>();
        try {
            CharStream sourceStream = getSourceAsCharStreams(sourceFile);
            JavaScriptParser parser = new Builder.Parser(sourceStream).build();
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            methods.add(jsParseTreeListener.getFileBlockMethod());
            return methods;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Parsing Errors Occurs at source file: " + sourceFile.getPath());
        }
        return methods;

    }

    /**
     * Read file content as CharStream for a given file
     *
     * @param sourceFile Source file
     * @return File content as CharStream and throws IOException if the file is not found or read
     */
    private CharStream getSourceAsCharStreams(File sourceFile) {
        CharStream input = null;
        try {
            Path sourcePath = Paths.get(sourceFile.getPath());
            input = CharStreams.fromPath(sourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

}
