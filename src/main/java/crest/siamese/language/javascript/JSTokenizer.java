package crest.siamese.language.javascript;

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
     *
     * @param input input
     * @return ArrayList of String
     */
    @Override
    public ArrayList<String> getTokensFromString(String input) {
        JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString(input));
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