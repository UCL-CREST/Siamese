package crest.siamese.experiment;

import crest.siamese.main.Siamese;

public class TermFreqAnalyser {

    private static Siamese siamese;

    public static void main(String[] args) {
        String config = "config_bcb_search.properties";
        siamese = new Siamese(config);
        siamese.startup();
        analyseTerms(config);
//        getIndicesStats();
        siamese.shutdown();
    }

    public static void analyseTerms(String config) {
        String index = "bigclonebench_utf8_1shard";
        String mode = "df";
        siamese.analyseTermFreq(index, "tokenizedsrc", mode, "freq_" + mode + "_toksrc_" + index + ".csv");
        siamese.analyseTermFreq(index, "src", mode, "freq_" + mode + "_src_" + index + ".csv");
        /* then call the sort_term.py python script to generate a Zipf plot */
    }

    public static void getIndicesStats() {
        System.out.println("Docs = " + siamese.getIndicesStats());
    }
}
