package crest.siamese.experiment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultChecker {

    public static void main(String[] args) {
        SearchResultChecker checker = new SearchResultChecker();
        try {
            checker.run(
                    "/Users/Chaiyong/Desktop/so.csv",
                    "/Users/Chaiyong/Desktop/112_A_true_clones.csv",
                    "/Users/Chaiyong/Desktop/results.csv",
                    "/Users/Chaiyong/Desktop/queries/",
                    "/Users/Chaiyong/Downloads/stackoverflow/stackoverflow_formatted/"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(String resultFile,
                    String trueCloneFile,
                    String outputFile,
                    String queryPrefix,
                    String resultPrefix) throws IOException {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        StringBuilder results = new StringBuilder();
        FileWriter fw = new FileWriter(new File(outputFile), true);
        BufferedWriter bw = new BufferedWriter(fw);
        ArrayList<String> correctResultList = new ArrayList<>();
        ArrayList<List<String>> resultList = new ArrayList<>();

        int found = 0;
        int notFound = 0;

        try {
            br = new BufferedReader(new FileReader(trueCloneFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] fragment = line.split(cvsSplitBy);
                // add true clones
                correctResultList.add(fragment[0]);
            }

            br.close();

            System.out.println("True clones size (" + trueCloneFile + "): " + correctResultList.size());
            br = new BufferedReader(new FileReader(resultFile));
            System.out.println("### Start checking ... ###");

            while ((line = br.readLine()) != null) {
                String[] fragment = line.split(cvsSplitBy);
                List<String> searchResult = new ArrayList<>();
                searchResult.addAll(Arrays.asList(fragment));
                resultList.add(searchResult);
            }

            System.out.println("Search file size (" + resultFile + "): " + resultList.size());
            br.close();

            // start searching
            for (List<String> sResult : resultList) {
                String[] query = sResult.get(0)
                        .replace("\uFEFF", "")
                        .replace(queryPrefix, "")
                        .replace(resultPrefix, "")
                        .split(".java");

                String correctResult = correctResultList.get(Integer.parseInt(query[0]) - 1);
                String matchedResults = "";

                // skip the first one which is a query
                for (int j=1; j<sResult.size(); j++) {
                    if (sResult.get(j).startsWith(correctResult)) {
                        matchedResults += "(" + j + ")" + sResult.get(j) + ",";
                    }
                }

                if (!matchedResults.equals("")) {
                    bw.write(sResult.get(0) + "," + matchedResults + "\n");
                }
            }

            System.out.println("Done. Result is at: " + outputFile);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
