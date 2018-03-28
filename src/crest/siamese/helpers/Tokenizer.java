package crest.siamese.helpers;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;

public interface Tokenizer {
    public void configure(Normalizer normalizer);
    public ArrayList<String> tokenize(String s) throws Exception;
    public ArrayList<String> tokenize(Reader reader) throws Exception;
    public ArrayList<String> tokenizeLine(Reader reader) throws Exception;
    public ArrayList<String> tokenize(File f) throws Exception;
    public ArrayList<String> getTokensFromFile(String file) throws Exception;
    public ArrayList<String> getTokensFromString(String input) throws Exception;
}
