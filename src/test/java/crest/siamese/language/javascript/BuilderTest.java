package crest.siamese.language.javascript;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuilderTest {

    String dummyFilePath = "crest/siamese/language/javascript/DemoTest.js";
    String functionalDeclarationSourceCode = "function factorial(n) {\n" +
            "    if (n === 0) {\n" +
            "        return 1;\n" +
            "    }\n" +
            "    return n * factorial(n - 1);\n" +
            "}";

    @Test
    public void buildParserWithCharStreamTest() {
        CharStream sourceStream = CharStreams.fromString(functionalDeclarationSourceCode, dummyFilePath);
        JavaScriptParser parser = new Builder.Parser(sourceStream).build();
        assertNotNull(parser);
    }

    @Test
    public void buildParserWithLexerTest() {
        CharStream sourceStream = CharStreams.fromString(functionalDeclarationSourceCode, dummyFilePath);
        JavaScriptLexer lexer = new JavaScriptLexer(sourceStream);
        JavaScriptParser parser = new Builder.Parser(lexer).build();
        assertNotNull(parser);


    }
}
