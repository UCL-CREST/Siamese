package elasticsearch.main;

import au.com.bytecode.opencsv.CSVReader;
import elasticsearch.document.Method;
import elasticsearch.document.MethodClone;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static elasticsearch.main.Experiment.isPrint;

public class Evaluator {
    private String clonePairFile;
    private String index;
    private String outputDir;

    private HashMap<String, ArrayList<MethodClone>> cloneCluster;
    private HashMap<String, ArrayList<String>> searchKey;

    public Evaluator(String clonePairFile, String index, String outputDir, boolean isPrint) {
        this.clonePairFile = clonePairFile;
        this.index = index;
        this.outputDir = outputDir;

        ArrayList<MethodClone> clones = readCSV(clonePairFile);

        if (isPrint) {
            System.out.println("Reading clone cluster files ... ");
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
        generateSearchKey();
    }

    void generateSearchKey() {
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
                        + ":" + getMethodName(clone.getFullHeader()) + "/" + filename
                        + ":" + getMethodName(clone.getFullHeader()) + ".java_method";
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
                                + ":" + getMethodName(clones.get(j).getFullHeader())
                                + "/" + filename
                                + ":" + getMethodName(clones.get(j).getFullHeader()) + ".java_method";
                        relevantResults.add(result);
                        textToPrint += "," + result;
                    }
                }
                // add the query, and its relevant results
                searchKey.put(query, relevantResults);
                textToPrint += "\n";
            }
        }

        // writeToFile("resources", "searchkey.csv", textToPrint, false);
        System.out.println("Done generating search key ... ");
    }

    private String fixPath(String pathToFix) {
        return pathToFix.replace("/0_orig/", "/0_orig_")
                .replace("/1_artifice/", "/1_artifice_")
                .replace("/test_0_orig_no_krakatau/", "/test_0_orig_no_krakatau_")
                .replace("/test_0_orig_no_procyon/", "/test_0_orig_no_procyon_")
                .replace("/test_1_artifice_no_krakatau/", "/test_1_artifice_no_krakatau_")
                .replace("/test_1_artifice_no_procyon/", "/test_1_artifice_no_procyon_");
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
        Experiment.writeToFile("resources", "searchkey.csv", textToPrint, false);
    }

    public double evaluateARP(String outputFile, int r) {
        System.out.println("Evaluating " + r + "-precision from the output file: " + outputFile);
        String RPrecToPrint = "";
        double arp = 0.0;

        try {

            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(outputFile), ',', '"', 0);

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            double sumRPrec = 0.0;
            int noOfQueries = 0;

            while ((nextLine = reader.readNext()) != null) {
                int tp = 0;
                // increase query count
                noOfQueries++;

                String query = nextLine[0];

                // get the answer key of this query
                ArrayList<String> relevantResults = searchKey.get(query);

                int checkSize = r;
                if (nextLine.length < r)
                    checkSize = nextLine.length;

                // check the results with the key
                for (int i = 1; i <= r; i++) {

                    // limit the check to the number of relevant results obtained (nextLine.length)
                    if (i < nextLine.length) {
                        if (relevantResults.contains(nextLine[i]))
                            tp++;
                    }
                }

                // calculate r-precision up to the number of relevant results obtained (if <= r)
                float rprec = (float) tp/r;

                if (isPrint)
                    System.out.println("  " + r + "-prec = " + rprec);
                RPrecToPrint += rprec + "\n";

                // sum up r-precision
                sumRPrec += rprec;

            }

            // calculate average r-precision
            arp = sumRPrec/noOfQueries;

            if (isPrint)
                System.out.println("--> No. of query = " + noOfQueries);

            String outFile = "rprec_" + index + ".csv";
            Experiment.writeToFile(outputDir, outFile , RPrecToPrint, false);
            System.out.println("ARP = " + arp);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arp;
    }

    public double evaluateMAP(String outputFile, int size) {
        System.out.println("Evaluating MAP from the output file: " + outputFile);
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
                // increase query count
                noOfQueries++;
                String query = nextLine[0];
                sumPrecision = 0.0;

                // get the answer key of this query
                ArrayList<String> relevantResults = searchKey.get(query);

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

                mapToPrint += averagePrec + "\n";
                sumAvgPrec += averagePrec;
            }

            // calculate MAP
            map = sumAvgPrec/noOfQueries;
            System.out.println("--> No. of query = " + noOfQueries);

            String outFile = "map_" + index + ".csv";
            Experiment.writeToFile(outputDir, outFile , mapToPrint, false);
            System.out.println("MAP = " + map);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    private ArrayList<MethodClone> readCSV(String csvFile) {
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

    public boolean createDir(String location) {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
