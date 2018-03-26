package crest.siamese.experiment;

import crest.siamese.helpers.MyUtils;
import crest.siamese.main.Siamese;

import java.io.File;

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
        String index = "qualitas";
        String mode = "df";
        String toksrc = "freq_" + mode + "_t0src_" + index + ".csv";
        String t1src = "freq_" + mode + "_t1src_" + index + ".csv";
        String t2src = "freq_" + mode + "_t2src_" + index + ".csv";
        String src = "freq_" + mode + "_t3src_" + index + ".csv";
        // delete previous result files.
        File toksrcf = new File(toksrc);
        toksrcf.delete();
        File t2srcf = new File(t2src);
        t2srcf.delete();
        File t1srcf = new File(t1src);
        t1srcf.delete();
        File srcf = new File(src);
        srcf.delete();
        // start analysing the tokens
        siamese.analyseTermFreq(index, "tokenizedsrc", mode, toksrc);
        siamese.analyseTermFreq(index, "t1src", mode, t1src);
        siamese.analyseTermFreq(index, "t2src", mode, t2src);
        siamese.analyseTermFreq(index, "src", mode, src);
        /* then call the sort_term.py python script to generate a Zipf plot */
    }

    public static void getIndicesStats() {
        System.out.println("Docs = " + siamese.getIndicesStats());
    }
}
