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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
