package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public final class SourceTreeWalker {
    private static void printFunctionDeclaration(File sourceFile, String jsSourceCode) {

        JavaScriptParser parser = new Builder.Parser(jsSourceCode).build();
        List<Method> methods = new ArrayList<>();
        try {
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            //methods.add(jsParseTreeListener.getFileBlockMethod());
            methods.addAll(jsParseTreeListener.getJSMethods());
        } catch (Exception e) {
            System.err.println("Source file->" + sourceFile.getPath() + "-cannot be parsed ");
        }
        System.out.println("---Completed---");

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

    public static void main(String[] args) throws Exception {
        URL url = SourceTreeWalker.class.getClassLoader().getResource("/crest/siamese/language/javascript/Generator.js");
        if (url == null) {
            System.out.println("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        File file = new File(filePath);
        String sourceCode = readFile(file);
        printFunctionDeclaration(file, sourceCode);
    }
}
