package crest.siamese.language.javascript;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;


public class ErrorListenerTest {


    final String SYNTAX_ERROR_SOURCE = "function (a, b) {\n" +
            "    return a + b;\n" +
            "}";

    final String FILE_NAME = "/dummy/test/path/syntaxErrorSourceCode.js";

    @Test(expected = RuntimeException.class)
    public void syntaxErrorTest() {
        CharStream jsSourceCode = CharStreams.fromString(SYNTAX_ERROR_SOURCE);
        JavaScriptParser parser = new Builder.Parser(jsSourceCode).build();
        ParseTree parseTree = parser.program();
        JSParseTreeListener jsParseTreeListener = new JSParseTreeListener(FILE_NAME);
        ParseTreeWalker.DEFAULT.walk(jsParseTreeListener, parseTree);
    }
}
