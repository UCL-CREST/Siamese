package crest.isics.main;

import crest.isics.helpers.JavaLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class Antlr4Test {
    public static void main(String[] args) {
        tokenizer("public static void main(String[] args) { int x = 0; double y = 0.5; System.out.println(\"Hello\");");
    }

    public static void tokenizer(String code) {
        ANTLRInputStream in = new ANTLRInputStream(code);
        JavaLexer lexer = new JavaLexer(in);
        List<? extends Token> tokenList = new ArrayList<>();
        tokenList = lexer.getAllTokens();
        String[] symbols = JavaLexer._SYMBOLIC_NAMES;
        for(Token token : tokenList){
            System.out.println(token.getText() + "," + symbols[token.getType()]);
        }
    }
}
