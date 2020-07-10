package crest.siamese.language.javascript;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;


/**
 * A builder class that helps to build JavaScriptParser with Custom Error Listener
 * based on the recommended Builder Pattern
 */
public final class Builder {

    private static final ErrorListener ERROR_LISTENER = new ErrorListener();

    /**
     * This class will not be instantiated.
     */
    private Builder() {
    }

    /**
     * A static class  generates a Parser Builder
     */
    public static final class Parser {

        private JavaScriptParser parser;

        /**
         * Constructor to Create Parser with source code as CharStream
         *
         * @param input Provided source code as CharStream
         */

        public Parser(CharStream input) {
            JavaScriptLexer lexer = new JavaScriptLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(ERROR_LISTENER);
            this.parser = new JavaScriptParser(new CommonTokenStream(lexer));
            this.parser.removeErrorListeners();
            this.parser.addErrorListener(ERROR_LISTENER);
        }

        /**
         * Constructor to Create Parser with JavaScriptLexer
         *
         * @param lexer JavaScript lexer;
         */
        public Parser(JavaScriptLexer lexer) {
            this.parser = new JavaScriptParser(new CommonTokenStream(lexer));
            this.parser.removeErrorListeners();
            this.parser.addErrorListener(ERROR_LISTENER);
        }

        /**
         * Build JavaScript Parser
         *
         * @return JavaScript Parser
         */
        public JavaScriptParser build() {
            return this.parser;
        }
    }

}