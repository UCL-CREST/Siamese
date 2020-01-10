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

package crest.siamese.helpers;

import crest.siamese.document.Method;
import crest.siamese.experiment.Experiment;
import crest.siamese.language.NormalizerMode;
import crest.siamese.language.java.JavaMethodParser;
import crest.siamese.language.java.JavaNormalizerMode;
import crest.siamese.language.java.JavaTokenizer;
import crest.siamese.settings.Settings;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaiyong on 3/28/17.
 */
/* TODO: outdated (remove?) */
public class SiameseIndexReader {
    private static String[] extensions = { "java" };
    private static NormalizerMode modes = new JavaNormalizerMode();
    private static IndexReader reader = null;
    private static nGramGenerator ngen;

    public static void main(String[] args) {
         // testIdfRetrieval();

        String indexName = "tfidf_sw_1";
        // initialise the n-gram generator
        ngen = new nGramGenerator(1);

        // directory where your index is stored
        String indexFile =  "/Users/Chaiyong/elasticsearch-2.2.0/data/stackoverflow/nodes/0/indices/"
                + indexName + "/0/index";
        char[] normMode = { 's', 'w' };

        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            // IndexSearcher searcher = new IndexSearcher(reader);
            String queries = queryTermSelection("/Users/Chaiyong/Documents/phd/2016/cloplag/tests_andrea/");
            // testIdfRetrieval();

            MyUtils.writeToFile("/Users/Chaiyong/Desktop/", "query.csv", queries, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String queryTermSelection(String inputFolder) {
        File folder = new File(inputFolder);
        List<File> listOfFiles = (List<File>) FileUtils.listFiles(folder, extensions, true);

        int count = 0;
        String returnString = "";

        for (File file : listOfFiles) {
            returnString += file.getAbsolutePath().toString()
                    .replace(inputFolder, "")
                    .replace("/" + file.getName(), "");
            // reset the output buffer
            // parse each file into method (if possible)
            JavaMethodParser methodParser = new JavaMethodParser(
                    file.getAbsolutePath(),
                    Experiment.prefixToRemove,
                    Settings.MethodParserType.METHOD,
                    false
                    );
            ArrayList<Method> methodList;
            String query = "";

            try {
                methodList = methodParser.parseMethods();
                // check if there's a method
                if (methodList.size() > 0) {
                    for (Method method : methodList) {
                        String tmpQuery = tokenize(method.getSrc());
                        // get selected terms
                        query = getSelectedTerms(tmpQuery, 5);

                        returnString += "," + query + "\n";
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getCause());
            }
        }
        return returnString;
    }

    /***
     * Read idf of each term in the query directly from Lucene index
     * @param terms query containing search terms
     * @param selectedSize size of the selected terms
     * @return selected top-selectedSize terms
     */
    private static String getSelectedTerms(String terms, int selectedSize) {
        String[] termsArr = terms.split(" ");
        // ArrayList<Main.SelectedTerm> selectedTermIndexes = new ArrayList<>();
        String selectedTerms = "";
        SelectedTerm firstMinTerm = new SelectedTerm("x", 9999999);
        SelectedTerm secondMinTerm = new SelectedTerm("x", 9999999);
        SelectedTerm thirdMinTerm = new SelectedTerm("x", 9999999);

        try {
            for (int i = 0; i < termsArr.length; i++) {
                String term = termsArr[i];
                // TODO: get rid of the blank term (why?)
                if (!term.equals("")) {
                    Term t = new Term("src", term);
                    int freq = reader.docFreq(t);

                    if (freq < firstMinTerm.getFrequency()) {
                        firstMinTerm.setFrequency(freq);
                        firstMinTerm.setTerm(term);
                    } else if (!term.equals(firstMinTerm.getTerm()) &&
                            freq < secondMinTerm.getFrequency()) {
                        secondMinTerm.setFrequency(freq);
                        secondMinTerm.setTerm(term);
                    } else if (!term.equals(firstMinTerm.getTerm()) &&
                            !term.equals(secondMinTerm.getTerm()) &&
                            freq < thirdMinTerm.getFrequency()) {
                        thirdMinTerm.setFrequency(freq);
                        thirdMinTerm.setTerm(term);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        selectedTerms = "\"" + firstMinTerm.getTerm().replace("\"","&quot;") + "\"," + firstMinTerm.getFrequency() +
                ",\"" + secondMinTerm.getTerm().replace("\"","&quot;") + "\"," + secondMinTerm.getFrequency() +
                ",\"" + thirdMinTerm.getTerm().replace("\"","&quot;") + "\"," + thirdMinTerm.getFrequency();

        return selectedTerms;
    }

    private static String printArray(ArrayList<String> arr, boolean pretty) {
        String s = "";
        for (String anArr : arr) {
            if (pretty && anArr.equals("\n")) {
                System.out.print(anArr);
                continue;
            }
            s += anArr + " ";
        }
        return s;
    }

    private static String tokenize(String sourcecode) throws Exception {
        String src;
        JavaTokenizer tokenizer = new JavaTokenizer();
        // generate tokens
        ArrayList<String> tokens = tokenizer.getTokensFromString(sourcecode);
        src = printArray(tokens, false);
        src = printArray(ngen.generateNGramsFromJavaTokens(tokens), false);
        return src;
    }

    public static class SelectedTerm {
        private String term;
        private int frequency;

        public SelectedTerm(String term, int frequency) {
            this.term = term;
            this.frequency = frequency;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}

