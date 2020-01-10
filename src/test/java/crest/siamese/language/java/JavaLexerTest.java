/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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
