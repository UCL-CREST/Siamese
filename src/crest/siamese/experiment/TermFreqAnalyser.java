package crest.siamese.experiment;

import crest.siamese.main.Siamese;

public class TermFreqAnalyser {

    private static Siamese siamese;

    public static void main(String[] args) {
        String config = "config_eval_bcb.properties";
        siamese = new Siamese(config);
        siamese.startup();
        analyseTerms(config);
        getIndicesStats();
        siamese.shutdown();
    }

    public static void analyseTerms(String config) {
        String index = "bcb_sample";
        String mode = "df";
        siamese.analyseTermFreq(index, "tokenizedsrc", mode, "freq_" + mode + "_toksrc.csv");
        siamese.analyseTermFreq(index, "src", mode, "freq_" + mode + "_src.csv");
        /* then call the sort_term.py python script to generate a Zipf plot */
    }

    public static void getIndicesStats() {
        System.out.println("Docs = " + siamese.getIndicesStats());
    }
}
