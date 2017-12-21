package crest.siamese.helpers;

import crest.siamese.document.Method;

import java.util.ArrayList;

public interface MethodParser {
    public String getLicense();
    public ArrayList<Method> parseMethods();
    public void configure(String filePath, String prefixToRemove, String mode, boolean isPrint);
}
