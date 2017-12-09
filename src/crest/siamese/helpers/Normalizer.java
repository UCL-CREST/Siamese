package crest.siamese.helpers;

import crest.siamese.settings.NormalizerMode;

import java.util.ArrayList;

public interface Normalizer {
    public void configure(NormalizerMode modes);
    public String normalizeAToken(String token, String type) throws Exception;
    public ArrayList<String> noNormalizeAToken(String token) throws Exception;
}
