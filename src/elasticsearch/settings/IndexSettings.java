package elasticsearch.settings;

/**
 * Created by Chaiyong on 5/24/16.
 */
public class IndexSettings {
    public class TFIDF {
        public String[] discountOverlap = { "no", "true", "false" };
        public String genIndexSettings(String discO) {
            return "{ \"similarity\": { \"tfidf_similarity\": { \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                    "\"analysis\": { " +
                    "\"analyzer\": { " +
                    "\"default\": { " +
                    "\"type\": \"whitespace\"" +
                    "} } } }";
        }
        public String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";
    }

    public class BM25 {
        public String[] k1s = {"0.0", "0.6", "1.2", "1.8", "2.4"};
        public String[] bs = {"0.0", "0.25", "0.50", "0.75", "1.00"};
        public String[] discountOverlaps = {"true", "false"};
        public String getIndexSettings(String k1, String b, String discO) {
            return "{ \"similarity\": "
                    + "{ \"bm25_similarity\": { \"type\": \"BM25\", \"k1\": \"" + k1 + "\", \"b\": \"" + b + "\", \"discount_overlap\": \"" + discO + "\" } }, "
                    + "\"analysis\": { \"analyzer\": { \"default\": { \"type\": \"whitespace\" } } } }";
        }
        public String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"bm25_similarity\" } } } }";
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

    public class IB {
        public String[] distributions = { "ll", "spl" };
        public String[] lambdas = { "df", "ttf" };
        public String[] ibNormalizationArr = { "no", "h1", "h2", "h3", "z" };
        public String getIndexSettings(String dist, String lamb, String ibNorm) {
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

        public String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"ib_similarity\" } } } } }";
    }

    public class LMD {
        public String[] mus = { "500", "750", "1000", "1250", "1500",
                "1750", "2000", "2250", "2500", "2750", "3000" };
        public String getIndexSettings(String mu) {
            return "{ \"similarity\": "
                    + "{ \"lmd_similarity\" : "
                    + "{ \"type\": \"LMDirichlet\", "
                    + "\"mu\": \"" + mu + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        }
        public String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmd_similarity\" } } } } }";
    }

    public class LMJK {
        public String[] lambdas = { "0.9" };
        public String getIndexSettings(String lambda) {
            return "{ \"similarity\": "
                    + "{ \"lmj_similarity\" : "
                    + "{ \"type\": \"LMJelinekMercer\", "
                    + "\"lambda\": \"" + lambda + "\""
                    + "} "
                    + "}, "
                    + "\"analysis\" : { \"analyzer\" : "
                    + "{ \"default\" : { \"type\" : \"whitespace\" } } } }";
        }
        public String mappingStr = "{ \"properties\": { \"src\": { \"type\": \"string\",\"similarity\": \"lmj_similarity\" } } } } }";
    }
}
