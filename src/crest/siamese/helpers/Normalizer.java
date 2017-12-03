package crest.siamese.helpers;

import java.util.ArrayList;

public interface Normalizer {
    public String normalizeAToken(String token, String type) throws Exception;
    public ArrayList<String> noNormalizeAToken(String token) throws Exception;
}
