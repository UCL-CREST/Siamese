package crest.isics.helpers;

import crest.isics.document.Method;
import crest.isics.settings.Settings;
import crest.isics.settings.NormalizerMode;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaiyong on 3/28/17.
 */
public class ISICSIndexReader {
    private static String[] extensions = { "java" };
    private static NormalizerMode modes = new NormalizerMode();
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
        setTokenizerMode(normMode);
        try {
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexFile)));
            // IndexSearcher searcher = new IndexSearcher(reader);
            String queries = queryTermSelection("/Users/Chaiyong/Documents/phd/2016/cloplag/tests_andrea/", "tfidf_sw_1");
            // testIdfRetrieval();

            MyUtils.writeToFile("/Users/Chaiyong/Desktop/", "query.csv", queries, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String queryTermSelection(String inputFolder, String index) {
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
            MethodParser methodParser = new MethodParser(
                    file.getAbsolutePath(),
                    crest.isics.main.Experiment.prefixToRemove,
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

                    // System.out.println(term + ": " + freq);
                    // first term, just insert
//                if (selectedTermIndexes.size() == 0) {
//                    selectedTermIndexes.add(new Main.SelectedTerm(termsArr[i], freq));
//                } else {
//                    int size = selectedTermIndexes.size();
//                    // if not the first one, add it according to its frequency (ascending)
//                    for (int j = 0; j < size; j++) {
//                        Main.SelectedTerm sti = selectedTermIndexes.get(j);
//                        if (freq < sti.getFrequency()) {
//                            Main.SelectedTerm newSti = new Main.SelectedTerm(termsArr[i], freq);
//                            selectedTermIndexes.add(j, newSti);
//                            break;
//                        }
//                    }
//                }

                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

//        selectedTerms = selectedTermIndexes.get(0).getTerm();
//        for (int i=1; i<selectedSize; i++) {
//            selectedTerms += " " + selectedTermIndexes.get(i).getTerm();
//        }

        selectedTerms = "\"" + firstMinTerm.getTerm().replace("\"","&quot;") + "\"," + firstMinTerm.getFrequency() +
                ",\"" + secondMinTerm.getTerm().replace("\"","&quot;") + "\"," + secondMinTerm.getFrequency() +
                ",\"" + thirdMinTerm.getTerm().replace("\"","&quot;") + "\"," + thirdMinTerm.getFrequency();

        return selectedTerms;
    }

    public static void testIdfRetrieval() {
        String[] terms = {"public", "static", "void"};
        /***
         * Code is copied from
         * http://stackoverflow.com/questions/35925482/lucene-using-fsdirectory
         */
        // directory where your index is stored
        String indexFile =  "/Users/Chaiyong/elasticsearch-2.2.0/data/stackoverflow/nodes/0/indices/tfidf_sw_1/0/index";

        IndexReader reader = null;
        try {
            Directory dir = FSDirectory.open(Paths.get(indexFile), NativeFSLockFactory.INSTANCE);
            reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            for (String term : terms) {
                Term t = new Term("src", term);
                int freq = reader.docFreq(t);
                System.out.println(term + ": " + freq);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String tokenize(File file) throws Exception {
        String src = "";
        JavaTokenizer tokenizer = new JavaTokenizer();
        JavaNormalizer normalizer = new JavaNormalizer(modes);

        if (modes.getEscape() == Settings.Normalize.ESCAPE_ON) {
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    ArrayList<String> tokens = normalizer.noNormalizeAToken(escapeString(line).trim());
                    src += printArray(tokens, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // generate tokens
            ArrayList<String> tokens = tokenizer.getTokensFromFile(file.getAbsolutePath());
            src = printArray(tokens, false);
        }
        return src;
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

    private static String escapeString(String input) {
        String output = "";
        output += input.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/").replace("\b", "\\b")
                .replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        return output;
    }

    private static void setTokenizerMode(char[] normOptions) {
        for (char c : normOptions) {
            // setting all normalisation options: w, d, j, p, k, v, s
            if (c == 'w')
                modes.setWord(Settings.Normalize.WORD_NORM_ON);
            else if (c == 'd')
                modes.setDatatype(Settings.Normalize.DATATYPE_NORM_ON);
            else if (c == 'j')
                modes.setJavaClass(Settings.Normalize.JAVACLASS_NORM_ON);
            else if (c == 'p')
                modes.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_ON);
            else if (c == 'k')
                modes.setKeyword(Settings.Normalize.KEYWORD_NORM_ON);
            else if (c == 'v')
                modes.setValue(Settings.Normalize.VALUE_NORM_ON);
            else if (c == 's')
                modes.setString(Settings.Normalize.STRING_NORM_ON);
            else if (c == 'x') {
                modes.setWord(Settings.Normalize.WORD_NORM_OFF);
                modes.setDatatype(Settings.Normalize.DATATYPE_NORM_OFF);
                modes.setJavaClass(Settings.Normalize.JAVACLASS_NORM_OFF);
                modes.setJavaPackage(Settings.Normalize.JAVAPACKAGE_NORM_OFF);
                modes.setKeyword(Settings.Normalize.KEYWORD_NORM_OFF);
                modes.setValue(Settings.Normalize.VALUE_NORM_OFF);
                modes.setValue(Settings.Normalize.STRING_NORM_OFF);
            } else if (c == 'e') {
                modes.setEscape(Settings.Normalize.ESCAPE_ON);
            }
        }
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

