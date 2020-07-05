package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public final class JSMethodParserSimulatorTest {

    private static void printFunctionDeclaration(File sourceFile) {
        List<Method> methods = new ArrayList<>();
        try {
            CharStream sourceStream = getSourceAsCharStreams(sourceFile);
            JavaScriptParser parser = new Builder.Parser(sourceStream).build();
            ParseTree parseTree = parser.program();
            JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourceFile.getPath(), parseTree);
            ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
            //methods.add(jsParseTreeListener.getFileBlockMethod());
            methods.addAll(jsParseTreeListener.getJSMethods());
        } catch (Exception e) {
            System.err.println("Source file->" + sourceFile.getPath() + "-cannot be parsed ");
        }
        System.out.println("Number of methods collected: " + methods.size());
        System.out.println("---Completed---");

    }


    private static CharStream getSourceAsCharStreams(File sourceFile) throws IOException {
        Path sourcePath = Paths.get(sourceFile.getPath());
        return CharStreams.fromPath(sourcePath);
    }

    @Test
    public void JSMethodParserTest() {
        /*URL url = JSMethodParserSimulator.class.getClassLoader().getResource("/crest/siamese/language/javascript/DemoTest.js");
        if (url == null) {
            System.out.println("Resource not found, please check input to getResource().");
        }*/
        String filePath = "/home/mrhmisu/UCL-MS/Siamese/src/test/resources/crest/siamese/language/javascript/All-Combination-Of-JS-Functions.js";
        File file = new File(filePath);
        printFunctionDeclaration(file);
    }
}
