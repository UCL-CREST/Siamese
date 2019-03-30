package crest.siamese.language;

import java.util.ArrayList;

public interface Normalizer {
    public void configure(NormalizerMode modes);
    public String normalizeAToken(String token, String type);
    public ArrayList<String> noNormalizeAToken(String token);
}
