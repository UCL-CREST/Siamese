package crest.siamese.language.javascript;

import org.antlr.v4.runtime.*;


/**
 * A builder class that helps to build JavaScript Parser with Custom Error Listener
 * based on the recommended Builder Pattern
 */
public final class Builder {

    private static final DescriptiveBailErrorListener ERROR_LISTENER = new DescriptiveBailErrorListener();

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
         * Create Parser with ANTLRErrorListener
         *
         * @param listener ANTLRErrorListener to receive the errors;
         */
        public Parser withErrorListener(ANTLRErrorListener listener) {
            this.parser.removeErrorListeners();
            this.parser.addErrorListener(listener);
            return this;
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