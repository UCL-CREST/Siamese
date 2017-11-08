package crest.isics.main;

import crest.isics.helpers.EvalResult;

public class BCBExperiment {

    public static void main(String[] args) {

        if (args.length < 3) {
            // If missing some arguments, show the help
            System.out.println("Usage: java BCBExperiment " +
                    "<input folder> " +
                    "<working dir> " +
                    "<error measure [arp/map]>");
            System.exit(-1);

        } else {

            String outputFile = "";
            String mode = args[0];
            String inputDir = args[1];
            String workingDir = args[2];

        }
    }

    private static EvalResult tfidfTextExp(String inputDir, String workingDir, boolean isPrint) {
        String discO = "true";
        ISiCS isics = new ISiCS("config.properties");
        String indexSettings = "";
        String normMode = "x";
        String ngramSize = "1";

        if (!discO.equals("false"))
            indexSettings = "{ \"number_of_shards\": 1, " +
                    "\"similarity\": { \"tfidf_similarity\": " +
                    "{ \"type\": \"default\", \"discount_overlaps\": \"" + discO + "\" } } , " +
                    "\"analysis\": { " +
                    "\"analyzer\": { " +
                    "\"default\": { " +
                    "\"type\": \"whitespace\"" +
                    "} } } }";
        else {
            indexSettings = "{ \"analyzer\" : { \"default\" : { \"type\" : \"whitespace\" } } }";
        }

        String mappingStr = "{ \"properties\": { \"src\": " +
                "{ \"type\": \"string\",\"similarity\": \"tfidf_similarity\" } } } } }";

        return new EvalResult("map", 0.0);
    }
}
