/*
   Copyright 2018 Wayne Tsui and Jens Krinke

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

package crest.siamese.language.python3;

import crest.siamese.language.Normalizer;
import crest.siamese.language.Tokenizer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tokenizer for Python 3 source code.
 */
public class Python3Tokenizer implements Tokenizer {
    private Normalizer normalizer;

    /**
     * Constructor that refer to parent Object class. Required for calling Class::newInstance.
     */
    public Python3Tokenizer() {
        super();
    }

    @Override
    public void configure(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public ArrayList<String> tokenize(String s) throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> tokenize(Reader reader) throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> tokenizeLine(Reader reader) throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> tokenize(File f) throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> getTokensFromFile(String file) throws Exception {
        return null;
    }

    /**
     * Uses the ANTLR4 Python3Lexer to obtain the list of tokens from the extracted method. Normalises the tokens
     * according to the configuration of the Normalizer (composite object of this class).
     * @param input input
     * @return ArrayList of String
     */
    @Override
    public ArrayList<String> getTokensFromString(String input) {
        Python3Lexer lexer = new Python3Lexer(CharStreams.fromString(input));
        List<? extends Token> tokens = lexer.getAllTokens();
        return tokens.stream()
                .map(token -> normalizer.normalizeAToken(
                        token.getText(),
                        Python3Parser.VOCABULARY.getSymbolicName(token.getType())))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
