package crest.siamese.test;

import crest.siamese.helpers.JavaLexer;
import crest.siamese.helpers.JavaTokenizer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaLexerTest {
    @org.junit.Test
    public void checkLexer() {
        File f = new File("resources/lexer_test/233.java");
        String[] symbols = JavaLexer._SYMBOLIC_NAMES;
        try {
            String code = FileUtils.readFileToString(f);
            InputStream stream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
            JavaLexer lexer = new JavaLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
            List<? extends Token> tokenList;
            tokenList = lexer.getAllTokens();
            for (int i=0; i<tokenList.size(); i++) {
                Token token = tokenList.get(i);
                System.out.println(token.getText().trim() + "," + symbols[token.getType()]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
