/*
   Copyright 2020 Md Rakib Hossain and Jens Krinke

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

package crest.siamese.language.javascript;


import crest.siamese.language.Normalizer;
import crest.siamese.language.Tokenizer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tokenizer for JavaScript source code.
 */

public class JSTokenizer implements Tokenizer {
    private Normalizer normalizer;


    public JSTokenizer() {
        super();
    }

    @Override
    public void configure(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    /**
     * Not Implemented for JavaScript source code.
     */
    @Override
    public ArrayList<String> tokenize(String s) {
        return new ArrayList<>();
    }

    /**
     * Not Implemented for JavaScript source code.
     */
    @Override
    public ArrayList<String> tokenize(Reader reader) {
        return new ArrayList<>();
    }

    /**
     * Not Implemented for JavaScript source code.
     */
    @Override
    public ArrayList<String> tokenizeLine(Reader reader) {
        return new ArrayList<>();
    }


    @Override
    public ArrayList<String> tokenize(File f) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getTokensFromFile(String file) {
        return new ArrayList<>();
    }

    /**
     * Uses the ANTLR4 JavaScriptLexer to obtain the list of tokens from the extracted method. Normalises the tokens
     * according to the configuration of the Normalizer (composite object of this class).
     *
     * @param input input
     * @return ArrayList of String
     */
    @Override
    public ArrayList<String> getTokensFromString(String input) {
        return getTokens(CharStreams.fromString(input));

    }


    /**
     * Uses the ANTLR4 JavaScriptLexer to obtain the list of tokens from the extracted method. Normalises the tokens
     * according to the configuration of the Normalizer (composite object of this class).
     *
     * @param source source code as CharStream
     * @return ArrayList of String
     */
    protected ArrayList<String> getTokens(CharStream source) {
        JavaScriptLexer lexer = new JavaScriptLexer(source);
        List<? extends Token> tokens = lexer.getAllTokens();
        List<Token> normalizedTokens = new ArrayList<>();
        tokens.forEach(token -> {
            if (!JavaScriptParser.VOCABULARY.getSymbolicName(token.getType()).equals("WhiteSpaces")) {
                normalizedTokens.add(token);
            }
        });
        return normalizedTokens.stream()
                .map(token -> normalizer.normalizeAToken(token.getText(),
                        JavaScriptParser.VOCABULARY.getSymbolicName(token.getType()).toUpperCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
