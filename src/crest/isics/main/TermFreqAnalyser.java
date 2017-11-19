package crest.isics.main;

public class TermFreqAnalyser {

    public static void main(String[] args) {
        String config = "config_eval_bcb.properties";
        analyseTerms(config);
    }

    public static void analyseTerms(String config) {
        ISiCS isics = new ISiCS(config);
        isics.startup();
        String index = "bcb_sample";
        String mode = "df";
        isics.analyseTermFreq(index, "tokenizedsrc", mode, "freq_" + mode + "_toksrc.csv");
        isics.analyseTermFreq(index, "src", mode, "freq_" + mode + "_src.csv");
        isics.shutdown();

        /* then call the sort_term.py python script to generate a Zipf plot */
    }
}
