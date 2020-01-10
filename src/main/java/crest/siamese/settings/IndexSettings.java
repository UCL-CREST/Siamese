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

package crest.siamese.settings;

/**
 * Created by Chaiyong on 5/24/16.
 */
public class IndexSettings {
    public static class TFIDF {
        public static class DisCountOverlap {
            public static String NO = "no";
            public static String TRUE = "true";
            public static String FALSE = "false";
        }

        public static String getIndexSettings(String discO) {
            return "{ \"number_of_shards\": 1, " +
                    "\"similarity\": { \"tfidf_similarity\": { \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                    "\"analysis\": { " +
                    "\"analyzer\": { " +
                    "\"default\": { " +
                    "\"type\": \"whitespace\"" +
                    "} } } }";
        }
        public static String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
    }

    public static class BM25 {
        public static String[] k1s = {"0.0", "0.6", "1.2", "1.8", "2.4"};
        public static String[] bs = {"0.0", "0.25", "0.50", "0.75", "1.00"};
        public static String[] discountOverlaps = {"true", "false"};

        public static String getDefaultIndexSettings() {
            String k1 = "1.2";
            String b = "0.75";
            String discO = "true";
            return "{ \"similarity\": "
                    + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
                    + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
        }

        public static String getIndexSettings(String k1, String b, String discO) {
            return "{ \"similarity\": "
                    + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
                    + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
        }
        public static String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
    }

    public static class DFR {
        public static String bmBE = "be";
        public static String bmD = "d";
        public static String bmG = "g";
        public static String bmIF = "if";

        public static String aeNo = "no";
        public static String aeB = "b";
        public static String aeL = "l";

        public static String normNo = "no";
        public static String normH1 = "h1";
        public static String normH2 = "h2";
        public static String normH3 = "h3";
        public static String normZ = "z";

        public static String getIndexSettings(String bm, String ae, String norm) {
            return "{ \"similarity\": { \"dfr_similarity\" : { \"type\": \"DFR\", \"basic_model\": \"" + bm + "\", \"after_effect\": \"" + ae + "\", "
                    + "\"normalization\": \"" + norm + "\", \"normalization.h2.c\": \"3.0\"} },  "
                    + "\"analysis\" : { \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } } }";
        }
        public static String mappingStr = "{ \"properties\": { \"file\": { \"type\": \"string\"}, \"src\": { \"type\": \"string\",\"similarity\": \"dfr_similarity\" } } } } }";
    }

    public static class IB {
        public static String distributionsLL = "ll";
        public static String distributionsSPL = "spl";
        public static String lambdasDF = "df";
        public static String lambdasTTF = "ttf";
        public static String normNo = "no";
        public static String normH1 = "h1";
        public static String normH2 = "h2";
        public static String normH3 = "h3";
        public static String normZ = "z";
        public static String getIndexSettings(String dist, String lamb, String ibNorm) {
            return "{ \"similarity\": "
                    + "{ \"ib_similarity\" : "
                    + "{ \"type\": \"IB\", "
                    + "\"distribution\": \"" + dist + "\", "
                    + "\"lambda\": \"" + lamb + "\", "
                    + "\"normalization\": \"" + ibNorm + "\""
                    + "} "
                    + "},  "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        }

        public static String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
    }

    public static class LMD {
        public static String getIndexSettings(String mu) {
            return "{ \"similarity\": "
                    + "{ \"lmd_similarity\" : "
                    + "{ \"type\": \"LMDirichlet\", "
                    + "\"mu\": \"" + mu + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        }
        public static String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
    }

    public static class LMJ {
        public static String getIndexSettings(String lambda) {
            return "{ \"similarity\": "
                    + "{ \"lmj_similarity\" : "
                    + "{ \"type\": \"LMJelinekMercer\", "
                    + "\"lambda\": \"" + lambda + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        }
        public static String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
    }
}
