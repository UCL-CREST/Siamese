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

import crest.siamese.document.MethodClone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MethodLevelEvaluator extends Evaluator {

    public MethodLevelEvaluator(String clonePairFile, String index, String outputDir, boolean isPrint) {
        super(clonePairFile, index, outputDir, isPrint);
    }

    @Override
    public int generateSearchKey() {
        searchKey = new HashMap<String, ArrayList<String>>();
        Iterator it = cloneCluster.entrySet().iterator();
        String textToPrint = "";

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<MethodClone> clones = (ArrayList<MethodClone>) pair.getValue();

            for (int i=0; i<clones.size(); i++) {

                MethodClone clone = clones.get(i);
                String filename = clone.getFile().substring(clone.getFile().lastIndexOf("/") + 1);
                String query = fixPath(clone.getFile())
                        + ":" + getMethodName(clone.getHeader()) + "/" + filename
                        + ":" + getMethodName(clone.getHeader()) + ".java_method";
                textToPrint += query;
                ArrayList<String> relevantResults = new ArrayList<String>();

                // add query as the first relevant result
                relevantResults.add(query);
                textToPrint += "," + query;

                for (int j=0; j<clones.size(); j++) {
                    // other relevant results
                    if (i!=j) {
                        filename = clones.get(j).getFile().substring(clones.get(j).getFile().lastIndexOf("/") + 1);
                        String result = fixPath(clones.get(j).getFile())
                                + ":" + getMethodName(clones.get(j).getHeader())
                                + "/" + filename
                                + ":" + getMethodName(clones.get(j).getHeader()) + ".java_method";

                        relevantResults.add(result);
                        textToPrint += "," + result;
                    }
                }
                // add the query, and its relevant results
                searchKey.put(query, relevantResults);
                textToPrint += "\n";
            }
        }

        MyUtils.writeToFile("resources", "searchkey.csv", textToPrint, false);
//        System.out.println("Done generating search key from " + this.clonePairFile + " ... ");

        return searchKey.size();
    }

    private String getMethodName(String methodHeader) {
        String[] headerSplit = methodHeader.split(" ");
        String methodName = "";
        for (String h: headerSplit) {
            if (h.contains("("))
                methodName = h;
        }
        return methodName.substring(0, methodName.indexOf("("));
    }

    private String fixPath(String pathToFix) {
        return pathToFix.replace("/0_orig/", "/0_orig_")
                .replace("/1_artifice/", "/1_artifice_")
                .replace("/test_0_orig_no_krakatau/", "/test_0_orig_no_krakatau_")
                .replace("/test_0_orig_no_procyon/", "/test_0_orig_no_procyon_")
                .replace("/test_1_artifice_no_krakatau/", "/test_1_artifice_no_krakatau_")
                .replace("/test_1_artifice_no_procyon/", "/test_1_artifice_no_procyon_");
    }
}
