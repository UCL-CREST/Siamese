package crest.siamese.language.javascript;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;


public class DescriptiveBailErrorListenerTest {


    String syntaxErrorSourceCode = "function (a, b) {\n" +
            "    return a + b;\n" +
            "}";

    String sourcePath = "/dummy/test/path/syntaxErrorSourceCode.js";

    @Test(expected = RuntimeException.class)
    public void syntaxErrorTest() {
        CharStream jsSourceCode = CharStreams.fromString(syntaxErrorSourceCode);
        JavaScriptParser parser = new Builder.Parser(jsSourceCode).build();
        ParseTree parseTree = parser.program();
        JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(sourcePath, parseTree);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
    }
}
