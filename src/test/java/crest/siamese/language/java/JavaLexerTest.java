package crest.siamese.language.java;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaLexerTest {
    @Test
    public void checkLexer() throws IOException {
        File f = new File("resources/lexer_test/233.java");
        String code = FileUtils.readFileToString(f);
        InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        JavaLexer lexer = new JavaLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
        List<? extends Token> tokenList;
        tokenList = lexer.getAllTokens();
        for (Token token: tokenList) {
            String symbolicName = JavaLexer.VOCABULARY.getSymbolicName(token.getType());
            System.out.println(token.getText().trim() + "," + symbolicName);
        }
    }
}
