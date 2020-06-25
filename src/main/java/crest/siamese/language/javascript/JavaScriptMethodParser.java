package crest.siamese.language.javascript;

import crest.siamese.document.Method;
import crest.siamese.language.MethodParser;

import java.util.ArrayList;

public class JavaScriptMethodParser implements MethodParser {

    @Override
    public String getLicense() {
        return null;
    }


    /**
     * Uses ANTLR4 generated Parser and Lexer to extract methods from JavaScript source code file
     *
     * @return ArrayList of methods extracted from FILE_PATH attribute
     */
    @Override
    public ArrayList<Method> parseMethods() {

        ArrayList<Method> methods = new ArrayList<>();
        if (methods.size() > 0)
            return methods;

        return createFileMethod();

    }

    @Override
    public void configure(String filePath, String prefixToRemove, String mode, boolean isPrint) {

    }

    public ArrayList<Method> createFileMethod() {
        // transfer the file as a Method
        return new ArrayList<>();
    }
}
