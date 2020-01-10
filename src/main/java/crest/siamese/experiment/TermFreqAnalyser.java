/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.experiment;

import crest.siamese.helpers.MyUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class TermFreqAnalyser {
    private static String elasticsearchLoc = "/Users/Chaiyong/Documents/phd/2017/Siamese/elasticsearch-2.2.0";

    public static void main(String[] args) {
        analyseTerms();
//        getIndicesStats();
    }

    private static void analyseTermFreq(String indexName, String field, String freqType, String outputFileName) {
        String indexFile = elasticsearchLoc + "/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        DecimalFormat df = new DecimalFormat("#.00");
        int printEvery = 100000;
        File outputFile = new File(outputFileName);
        if (outputFile.exists()) {
            if (!outputFile.delete()) {
                System.out.println("ERROR: cannot delete the output file.");
                System.exit(0);
            }
        }
        /* adapted from
        https://stackoverflow.com/questions/28244961/lucene-4-10-2-calculate-tf-idf-for-all-terms-in-index
         */
        int count = 0;
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            Fields fields = MultiFields.getFields(reader);
            Terms terms = fields.terms(field);
            TermsEnum termsEnum = terms.iterator();
            int size = 0;
            // TODO: is there a better solution?
            // iterate to get the size
            while (termsEnum.next() != null) {
                size++;
            }
//            String[] termArr = new String[size];
            long[] freqArr = new long[size];
            // do the real work
            termsEnum = terms.iterator();
            while (termsEnum.next() != null) {
//                String term = termsEnum.term().utf8ToString();
                long tfreq = 0;
                if (freqType.equals("tf"))
                    tfreq = termsEnum.totalTermFreq();
                else if (freqType.equals("df"))
                    tfreq = termsEnum.docFreq();
                else {
                    System.out.println("Wrong frequency. Quit!");
                    System.exit(0);
                }
//                termArr[count] = term;
                freqArr[count] = tfreq;
                if (count % printEvery == 0) {
                    System.out.println("processed: " + count + " terms "
                            + " [" + df.format(((long)count * 100)/size) + "%]");
                }
                count++;
            }
            System.out.println(field + ": total = " + count);
            double[] data = new double[size];
            String output = "freq\n";
            for (int i = 0; i < freqArr.length; i++) {
                data[i] = freqArr[i];
                output += freqArr[i] + "\n";
                if (i > 0 && i % printEvery == 0) {
                    MyUtils.writeToFile("./", outputFileName, output, true);
                    System.out.println("written: " + i + " terms "
                            + " [" + df.format(((long)i * 100)/size) + "%]");
                    output = "";
                }
            }
            // write the rest to the file
            MyUtils.writeToFile("./",outputFileName, output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void analyseTerms() {
        String index = "qualitas";
        String mode = "df";
        String dir = "results/results_for_rq0_qr_thresholds/";
        String toksrc = dir + "freq_" + mode + "_t0src_" + index + ".csv";
        String t1src = dir + "freq_" + mode + "_t1src_" + index + ".csv";
        String t2src = dir + "freq_" + mode + "_t2src_" + index + ".csv";
        String src = dir + "freq_" + mode + "_t3src_" + index + ".csv";
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
        analyseTermFreq(index, "tokenizedsrc", mode, toksrc);
        analyseTermFreq(index, "t1src", mode, t1src);
        analyseTermFreq(index, "t2src", mode, t2src);
        analyseTermFreq(index, "src", mode, src);
        /* then call the sort_term.py python script to generate a Zipf plot */
    }
}
