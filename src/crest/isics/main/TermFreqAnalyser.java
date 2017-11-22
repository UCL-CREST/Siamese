package crest.isics.main;

public class TermFreqAnalyser {

    private static ISiCS isics;

    public static void main(String[] args) {
        String config = "config_eval_bcb.properties";
        isics = new ISiCS(config);
        isics.startup();
        analyseTerms(config);
//        getIndicesStats();
        isics.shutdown();
    }

    public static void analyseTerms(String config) {
        String index = "bcb_sample";
        String mode = "df";
        isics.analyseTermFreq(index, "tokenizedsrc", mode, "freq_" + mode + "_toksrc.csv");
        isics.analyseTermFreq(index, "src", mode, "freq_" + mode + "_src.csv");
        /* then call the sort_term.py python script to generate a Zipf plot */
    }

    public static void getIndicesStats() {
        System.out.println("Docs = " + isics.getIndicesStats());
    }
}
