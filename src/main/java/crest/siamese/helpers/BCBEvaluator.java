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
import crest.siamese.document.BCBDocument;
import crest.siamese.document.Document;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BCBEvaluator extends Evaluator  {

    private Connection connection = null;
    private Statement stmt = null;

    public BCBEvaluator() {
        super();
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your H2 JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
//            System.exit(0);
        }
    }

    public boolean connectDB() {
        try {
            connection = DriverManager.
                    getConnection("jdbc:h2:/Users/Chaiyong/Downloads/bcb_db/bcb", "sa", "");
                        if (connection == null) {
                System.out.println("Failed to make connection!");
            }
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR: cannot connect to the database.");
            return false;
        }
    }

    public void closeDBConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BCBDocument> getCloneFragments(String sql, String IDField, boolean cloneType) {
        if (connection == null) {
            connectDB();
        }
        ArrayList<BCBDocument> cloneList = new ArrayList<>();
        /* adapted from
        https://www.tutorialspoint.com/postgresql/postgresql_java.htm
         */
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 1;
            while (rs.next()) {
                int id = rs.getInt(IDField);
                String type = rs.getString("TYPE");
                String name = rs.getString("NAME");
                int start = rs.getInt("STARTLINE");
                int end = rs.getInt("ENDLINE");
//                System.out.println(count + " " + id + " " + type + "/" + name + "(" + start + "/" + end + ")");
                BCBDocument d = new BCBDocument();
                d.setId(id);
                d.setFile(type + "/" + name);
                d.setStartline(start);
                d.setEndline(end);
                if (cloneType) {
                    d.setSyntacticType(rs.getInt("SYNTACTIC_TYPE"));
                }
                cloneList.add(d);
                count++;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        return cloneList;
    }

    /***
     * Get the type-1 clone ids from BCB database
     * @param limit maximum no. of clone ids to retrieve
     * @param minClonePairs threshold of the minimum no. of clone pairs for each clone id
     * @return a list of ids having clones
     */
    @Deprecated
    public ArrayList<Integer> getCloneIds(int limit, int minClonePairs, int minCloneSize) {
        if (connection == null) {
            connectDB();
        }
        ArrayList<Integer> cloneList = new ArrayList<>();
        /* adapted from
        https://www.tutorialspoint.com/postgresql/postgresql_java.htm
         */
        try {
            stmt = connection.createStatement();
            String sql = "SELECT * FROM\n" +
                    "  (\n" +
                    "    SELECT\n" +
                    "      function_id_one,\n" +
                    "      sum(syntactic_type) AS typesum,\n" +
                    "      count(*)            AS rowcount\n" +
                    "    FROM (clones\n" +
                    "      INNER JOIN functions ON clones.function_id_one = functions.id)\n" +
                    "    WHERE\n" +
                    "      functions.endline - functions.startline + 1 >= 10\n" +
                    "      AND (similarity_token >= 0.7 OR similarity_line >= 0.7)\n" +
                    "    GROUP BY function_id_one\n" +
                    "  ) AS c\n" +
                    "WHERE c.typesum < 3 * c.rowcount\n" +
                    "  AND c.rowcount >= 10\n" +
                    "  AND c.rowcount <= 20\n" +
                    "LIMIT " + limit + "\n" +
                    ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id1 = rs.getInt("function_id_one");
                System.out.println("Query ID: " + id1);
                cloneList.add(id1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        return cloneList;
    }

    @Deprecated
    public Document getQuery(int cloneId) {
        Document query = new Document();
        if (connection == null) {
            connectDB();
        }
        /* adapted from
            https://www.tutorialspoint.com/postgresql/postgresql_java.htm
             */
        try {
            stmt = connection.createStatement();
            String sql = "SELECT *\n" +
                    "FROM functions\n" +
                    "WHERE id = " + cloneId + ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                query.setId(rs.getInt("id"));
                query.setFile(rs.getString("type") + "/" + rs.getString("name"));
                query.setStartline(rs.getInt("startline"));
                query.setEndline(rs.getInt("endline"));
            }
            rs.close();
            stmt.close();

            return query;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public ArrayList<BCBDocument> getCloneGroup(int cloneId, int minCloneSize) {
        if (connection == null) {
            connectDB();
        }
        ArrayList<BCBDocument> cloneList = new ArrayList<>();
        Document query = new Document();
            /* adapted from
            https://www.tutorialspoint.com/postgresql/postgresql_java.htm
             */
        try {
            stmt = connection.createStatement();
            String sql = "SELECT A.id as id1, A.name as file1, A.type as type1, A.startline as start1, A.endline as end1,\n" +
                    "  B.id as id2, B.name as file2, B.type as type2, B.startline as start2, B.endline as end2, clones.syntactic_type\n" +
                    "FROM clones\n" +
                    "  INNER JOIN functions AS A ON function_id_one = A.id\n" +
                    "  INNER JOIN functions AS B ON function_id_two = B.id\n" +
                    "WHERE A.endline - A.startline + 1 >= 10\n" +
                    "  AND (similarity_token >= 0.7 OR similarity_line >= 0.7)\n" +
                    "  AND function_id_one = " + cloneId + "\n" +
                    "ORDER BY clones.syntactic_type ASC, B.name ASC\n" +
                    ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                BCBDocument d = new BCBDocument();
                int id = rs.getInt("id2");
                String file = rs.getString("type2") + "/" + rs.getString("file2");
                int start = rs.getInt("start2");
                int end = rs.getInt("end2");
                d.setId(id);
                d.setFile(file);
                d.setStartline(start);
                d.setEndline(end);
                d.setSyntacticType(rs.getInt("syntactic_type"));

                cloneList.add(d);
            }
            rs.close();
            stmt.close();

            return cloneList;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    private int checkResults(String result, ArrayList<BCBDocument> groundTruth) {
        String[] resultSplit = result.split("#");
        for (int i=0; i<groundTruth.size(); i++) {
            BCBDocument d = groundTruth.get(i);
            if (resultSplit[0].substring(0, resultSplit[0].indexOf("_")).trim().equals(d.getFile().trim()) &&
                    Integer.valueOf(resultSplit[1]) == d.getStartLine() &&
                    Integer.valueOf(resultSplit[2]) == d.getEndLine()) {
                return d.getSyntacticType();
            }
        }
        return -1;
    }

    @Deprecated
    public boolean evaluateCloneQuery(
            Document groundTruthQuery,
            ArrayList<BCBDocument> groundTruth,
            int resultSize,
            String outputFile,
            boolean computeSimilarity,
            String bcbLocation) {

        int checkedResultCount = 0;
        int tp = 0;
        int type1 = 0, type2 = 0, type3 = 0;
        String outToFile = "";

        try {
            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(outputFile), ',', '"', 0);
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                String[] query = nextLine[0].split("#");

                // only consider the one in the ground truth, discard other
                if (getQueryFileName(query[0]).equals(groundTruthQuery.getFile())
                        && Integer.valueOf(query[1]) == groundTruthQuery.getStartLine()
                        && Integer.valueOf(query[2]) == groundTruthQuery.getEndLine()) {

                    outToFile = "0,Q," + query[0] + "," + query[1] + "," + query[2] + "\n";
                    MyUtils.writeToFile("results", "groundtruth.csv", query[0] + "," + query[1] + "," + query[2] + ",", true);

                    // check the results with the key
                    for (int i = 1; i <= resultSize; i++) {

                        // check if we still have results to process
                        // (some searches do not return all results).
                        if (i < nextLine.length) {
                            // skip blank result and skip the result of the query itself
                            if (!nextLine[i].equals("")) {

                                if (!bcbLocation.endsWith("/"))
                                    bcbLocation += "/";
                                // remove the prefix of BCB location
                                nextLine[i] = nextLine[i].replace(bcbLocation, "");

                                if (!isQuery(groundTruthQuery, nextLine[i])) {

                                    checkedResultCount++;
                                    String[] doc = nextLine[i].split("#");

                                    int matchedCloneType = checkResults(nextLine[i], groundTruth);
                                    if (matchedCloneType != -1) {
                                        tp++;

                                        if (matchedCloneType == 1)
                                            type1++;
                                        else if (matchedCloneType == 2)
                                            type2++;
                                        else if (matchedCloneType == 3)
                                            type3++;
                                        else
                                            System.out.println("ERROR: wrong clone type.");

                                        System.out.println(checkedResultCount + "[T]: " + nextLine[i]);

                                        outToFile += checkedResultCount + ",R," + doc[0] + "," + doc[1] + "," + doc[2];
                                        if (computeSimilarity)
                                                outToFile += "," + doc[3];
                                        outToFile += ",T" + matchedCloneType + "\n";
                                    } else {
                                        System.out.println(checkedResultCount + "[F]: " + nextLine[i]);

                                        outToFile += checkedResultCount + ",R," + doc[0] + "," + doc[1] + "," + doc[2];
                                        if (computeSimilarity)
                                            outToFile += "," + doc[3];
                                        outToFile += ",F\n";
                                    }
                                } else {
                                    System.out.println("found query, skip.");
                                }
                            }
                        }

//                        // found all relevant results, stop
//                        if (tp == groundTruth.size()) {
//                            String o = printGroundTruth(groundTruth, tp, type1, type2, type3);
//                            MyUtils.writeToFile("results", "search_results.txt", outToFile + "\n" + o, true);
//                            return true;
//                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String o = printGroundTruth(groundTruth, tp, type1, type2, type3);
        MyUtils.writeToFile("results", "groundtruth.csv", o + "\n", true);
        MyUtils.writeToFile("results", "search_results.csv", outToFile + "\n", true);

        return false;
    }

    @Deprecated
    private String printGroundTruth(ArrayList<BCBDocument> groundTruth, int tp, int type1, int type2, int type3) {
        int gtype1 = 0, gtype2 = 0, gtype3 = 0;
        String outToFile = "";

        for (BCBDocument doc: groundTruth) {
            if (doc.getSyntacticType() == 1) {
                gtype1++;
            } else if (doc.getSyntacticType() == 2)
                gtype2++;
            else if (doc.getSyntacticType() == 3)
                gtype3++;
        }

        DecimalFormat df = new DecimalFormat(".##");

        System.out.println("TP     : " + tp + "/" + groundTruth.size() + "[" + df.format((double) (tp * 100)/groundTruth.size()) + "%]");
        outToFile += tp + "," + groundTruth.size() +  ",";
        if (gtype1 == 0) {
            System.out.println("Type1  : " + type1 + "/" + gtype1 + " [N/A%]");
            outToFile += type1 + "," + gtype1 + ",";
        } else {
            System.out.println("Type1  : " + type1 + "/" + gtype1 + " [" + df.format((double) (type1 * 100) / gtype1) + "%]");
            outToFile += type1 + "," + gtype1 + ",";
        }
        if (gtype2 == 0) {
            System.out.println("Type2  : " + type2 + "/" + gtype2 + " [N/A%]");
            outToFile += type2 + "," + gtype2 + ",";
        } else {
            System.out.println("Type2  : " + type2 + "/" + gtype2 + " [" + df.format((double) (type2 * 100) / gtype2) + "%]");
            outToFile += type2 + "," + gtype2 + ",";
        }
        if (gtype3 == 0) {
            System.out.println("Type3  : " + type3 + "/" + gtype3 + " [N/A%]");
            outToFile += type3 + "," + gtype3 + ",";
        }
        else {
            System.out.println("Type3  : " + type3 + "/" + gtype3 + " [" + df.format((double) (type3 * 100) / gtype3) + "%]");
            outToFile += type3 + "," + gtype3;
        }

        return outToFile;
    }

    @Deprecated
    private String getQueryFileName(String fileWithMethodName) {
        return fileWithMethodName.split("_")[0].trim();
    }

    @Deprecated
    private boolean isQuery(Document query, String result) {
        String[] resultSplit = result.split("#");
        return getQueryFileName(query.getFile()).equals(getQueryFileName(resultSplit[0].trim())) &&
                query.getStartLine() == Integer.valueOf(resultSplit[1]) &&
                query.getEndLine() == Integer.valueOf(resultSplit[2]);
    }
}
