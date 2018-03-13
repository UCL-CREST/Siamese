import com.google.common.collect.Iterables;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Evaluator {
    public static void main(String[] args) {
        String mrrPlot = "";
        for (int n=1; n<=20; n++) {
//        for (int n=70; n<=70; n++) {
            System.out.print(n + ",");
            String resultFile = n + ".csv";
            Iterable<CSVRecord> groundtruthRecords = readCSV("bellon_clones.csv");
            int size = Iterables.size(groundtruthRecords);
            String[][] groundtruth = new String[size][];
            groundtruthRecords = readCSV("bellon_clones.csv");
            int count = 0;
            for (CSVRecord record : groundtruthRecords) {
                String[] r = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    r[i] = record.get(i);
                }
                groundtruth[count] = r;
                count++;
            }

            Iterable<CSVRecord> resultRecords = readCSV(resultFile);
            int total = Iterables.size(resultRecords);
            double mrrSum = 0;
            int type1Count = 0;
            int type2Count = 0;
            int type3Count = 0;

            resultRecords = readCSV(resultFile);
            for (CSVRecord record : resultRecords) {
                int[] cloneResult = checkResult(groundtruth, record);
                if (cloneResult[0] != 0) {
                    mrrSum += 1 / cloneResult[0];
                    if (cloneResult[1] == 1)
                        type1Count++;
                    else if (cloneResult[1] == 2)
                        type2Count++;
                    else if (cloneResult[1] == 3)
                        type3Count++;
                    else
                        System.out.println("Wrong clone type.");
                }
            }
//        System.out.println("Found: " + found);
//        System.out.println("MRR: " + mrrSum/total);
            mrrPlot += mrrSum / total + ",";
            System.out.println(mrrSum / total + "," + "," +
                    (type1Count + type2Count + type3Count) + "," +
                    type1Count + "," + type2Count + "," + type3Count);

        }

        System.out.println(mrrPlot);
    }

    private static int[] checkResult(String[][] groundtruth, CSVRecord result) {
        int[] cloneResult = {0, 0};
        String query = result.get(0).split(".java")[0];
        boolean foundQueryFirst = false;
        for (int i=1; i<result.size(); i++) {
            // skip a no result query
            if (result.get(i).equals(""))
                continue;

            String[] answer1 = result.get(i).split(".java_");
            String[] location = answer1[1].split("#");
            String[] g = groundtruth[Integer.parseInt(query) - 1];

            // clone search result
            String rFile = answer1[0] + ".java";
            int rStart = Integer.parseInt(location[1]);
            int rEnd = Integer.parseInt(location[2]);
            // query
            String qFile = g[2];
            int qStart = Integer.parseInt(g[3]);
            int qEnd = Integer.parseInt(g[4]);
            // ground truth
            String gFile = g[5];
            int gStart = Integer.parseInt(g[6]);
            int gEnd = Integer.parseInt(g[7]);

            int type = Integer.parseInt(g[8]);

            if (i == 1 && qFile.equals(rFile) && rStart <= qStart && rEnd >= qEnd) {
                foundQueryFirst = true;
//                System.out.println("QUERY FIRST");
            }

            if (gFile.equals(rFile) && rStart <= gStart && rEnd >= gEnd) {
                int position = i;
                if (foundQueryFirst && i > 1)
                    position--;
//                System.out.println("CLONE NO. " + query);
//                System.out.println("POSITION: " + position);
//                System.out.println("QUERY: " + qFile + "," + qStart + "," + qEnd);
//                System.out.println("GROUNDTRUTH: " + gFile + "," + gStart + "," + gEnd);
//                System.out.println("ANSWER: " + answer1[0] + ".java" + "," + rStart + "," + rEnd);
//                System.out.println();
                cloneResult[0] = position;
                cloneResult[1] = type;
                return cloneResult;
            }
        }

        return cloneResult;
    }


    private static Iterable<CSVRecord> readCSV(String file) {
        Reader in = null;
        try {
            in = new FileReader(file);
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
            return records;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

