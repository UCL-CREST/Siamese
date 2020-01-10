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

import au.com.bytecode.opencsv.CSVReader;
import crest.siamese.document.MethodClone;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Evaluator {
    protected String clonePairFile;
    protected String index;
    protected String outputDir;
    private boolean isPrint;

    protected HashMap<String, ArrayList<MethodClone>> cloneCluster;
    protected HashMap<String, ArrayList<String>> searchKey;

    public Evaluator() { }

    public Evaluator(String clonePairFile, String index, String outputDir, boolean isPrint) {
        this.clonePairFile = clonePairFile;
        this.index = index;
        this.outputDir = outputDir;
        this.isPrint = isPrint;
        ArrayList<MethodClone> clones = readCSV(clonePairFile);

        if (isPrint) {
            System.out.println("Reading clone cluster files ... " + clonePairFile);
            System.out.println("--> No. of clones = " + clones.size());
        }
        // setup a hash map to store clone cluster
        cloneCluster = new HashMap<String, ArrayList<MethodClone>>();
        for (MethodClone mc: clones) {
            // if cluster is empty, create a cluster
            if (cloneCluster.get(mc.getCluster()) == null) {
                ArrayList<MethodClone> cluster = new ArrayList<MethodClone>();
                cluster.add(mc);
                cloneCluster.put(mc.getCluster(), cluster);
            } else {
                ArrayList<MethodClone> cluster = cloneCluster.get(mc.getCluster());
                cluster.add(mc);
                cloneCluster.put(mc.getCluster(), cluster);
            }
        }
        if (isPrint)
            System.out.println("--> No. of clusters = " + cloneCluster.size());
    }

    public int generateSearchKey() {
        // to be overridden
        return 0;
    }

    public void printSearchKey() {
        String textToPrint = "";
        Iterator it = searchKey.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            textToPrint += pair.getKey().toString();
            for (String result: (ArrayList<String>) pair.getValue()) {
                textToPrint += "," + result;
            }
            textToPrint += "\n";
        }
        MyUtils.writeToFile("resources", "searchkey.csv", textToPrint, false);
    }

    public double evaluateARP(String outputFile, int r) {
        double arp = 0.0;
        try {
            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(outputFile), ',', '"', 0);
            // Read CSV line by line and use the string array as you want
            String[] nextLine;
            double sumRPrec = 0.0;
            int noOfQueries = 0;
            while ((nextLine = reader.readNext()) != null) {
                int tp = 0;
                String query = nextLine[0];
                // get the answer key of this query
                ArrayList<String> relevantResults = searchKey.get(query);
                // skip the one that's not in the search key
                if (relevantResults != null) {
                    // set r equals to the number of relevant results
                    r = relevantResults.size();
                    // increase query count
                    noOfQueries++;
                    // check the results with the key
                    for (int i = 1; i <= relevantResults.size(); i++) {
                        // limit the check to the number of relevant results obtained (nextLine.length)
                        if (i < nextLine.length) {
                            if (relevantResults.contains(nextLine[i])) { tp++; }
                        }
                    }
                    // calculate r-precision up to the number of relevant results obtained (if <= r)
                    float rprec = (float) tp / r;

                    if (isPrint)
                        System.out.println("  " + r + "-prec = " + rprec);
                    // TODO: uncomment when needed, for statistical tests
//                    System.out.println(rprec);
                    // sum up r-precision
                    sumRPrec += rprec;
                }
            }
            // calculate average r-precision
            arp = sumRPrec/noOfQueries;
            System.out.println("No. of processed queries = " + noOfQueries);
            System.out.println("ARP = " + arp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arp;
    }

    public double evaluateMAP(String outputFile, long size) {
        String mapToPrint = "";
        double map = 0.0;
        try {
            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(outputFile), ',', '"', 0);
            String[] nextLine;
            double sumPrecision;
            double sumAvgPrec = 0.0;
            int noOfQueries = 0;
            while ((nextLine = reader.readNext()) != null) {
                int tp = 0;
                String query = nextLine[0];
                sumPrecision = 0.0;
                // get the answer key of this query
                ArrayList<String> relevantResults = searchKey.get(query);
                // skip the one that's not in the search key
                if (relevantResults != null) {
                    // increase query count
                    noOfQueries++;
                    // check the results with the key
                    for (int i = 1; i <= size; i++) {
                        // check if we still have results to process
                        // (some searches do not return all results.
                        if (i < nextLine.length) {
                            if (relevantResults.contains(nextLine[i])) {
                                tp++;
                                // calculate precision every time a relevant result is obtained.
                                float precision = (float) tp / i;
                                sumPrecision += precision;
                            }
                        }
                        // found all relevant results, stop
                        if (tp == relevantResults.size())
                            break;
                    }
                    double averagePrec = sumPrecision / relevantResults.size();

                    if (isPrint)
                        System.out.println("avgprec = " + averagePrec);
                    // TODO: uncomment if needed, for statistical tests
//                    System.out.println(averagePrec);
                    mapToPrint += averagePrec + "\n";
                    sumAvgPrec += averagePrec;
                }
            }
            // calculate MAP
            map = sumAvgPrec/noOfQueries;
            System.out.println("No. of processed queries = " + noOfQueries);
            System.out.println("MAP = " + map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    protected ArrayList<MethodClone> readCSV(String csvFile) {
        ArrayList<MethodClone> clones = new ArrayList<MethodClone>();
        try {
            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(csvFile), ',', '"', 1);
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //Verifying the read data here
                if (nextLine.length == 3) {
                    // create a clone method
                    // fix the path name
                    MethodClone mc = new MethodClone(nextLine[0], nextLine[1], nextLine[2]);
                    // add to the list
                    clones.add(mc);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clones;
    }
}
