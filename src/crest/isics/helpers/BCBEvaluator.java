package crest.isics.helpers;

import au.com.bytecode.opencsv.CSVReader;
import crest.isics.document.Document;
import crest.isics.settings.Settings;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BCBEvaluator extends Evaluator  {

    private Connection connection = null;
    private Statement stmt = null;

    public BCBEvaluator() {
        super();
        /* adapted from
        https://www.mkyong.com/jdbc/how-do-connect-to-postgresql-with-jdbc-driver-java/
         */
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
    }

    public boolean connectDB() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/bigclonebench",
                    "postgres",
                    "postgres");

            if (connection == null) {
                System.out.println("Failed to make connection!");
            }

            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");
            return true;
        } catch(SQLException e) {
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

    /***
     * Get the type-1 clone ids from BCB database
     * @param limit maximum no. of clone ids to retrieve
     * @param minClonePairs threshold of the minimum no. of clone pairs for each clone id
     * @return a list of ids having clones
     */
    public ArrayList<Integer> getType1CloneIds(int limit, int minClonePairs, int minCloneSize) {
        if (connection == null) {
            connectDB();
        }
        ArrayList<Integer> cloneList = new ArrayList<>();
        /* adapted from
        https://www.tutorialspoint.com/postgresql/postgresql_java.htm
         */
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT function_id_one, count(*) AS count\n" +
                            "FROM clones INNER JOIN functions ON clones.function_id_one = functions.id\n" +
                            "WHERE syntactic_type=1\n" +
                            "  AND functions.endline - functions.startline + 1 >= " + minCloneSize + "\n" +
                            "GROUP BY function_id_one\n" +
                            "LIMIT " + limit + ";");
//
//            ResultSet rs = stmt.executeQuery(
//                    "SELECT function_id_one, count(*) AS count\n" +
//                            "FROM clones INNER JOIN functions ON clones.function_id_one = functions.id\n" +
//                            "WHERE syntactic_type=1\n" +
//                            "  AND functions.endline - functions.startline + 1 >= 10\n" +
//                            "  AND functions.id = 574279\n" +
//                            "GROUP BY function_id_one;"
//            );
            while (rs.next()) {
                int id1 = rs.getInt("function_id_one");
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

    public ArrayList<Document> getCloneGroup(int cloneId, int minCloneSize) {
        if (connection == null) {
            connectDB();
        }
        ArrayList<Document> cloneList = new ArrayList<>();
        Document query = new Document();
            /* adapted from
            https://www.tutorialspoint.com/postgresql/postgresql_java.htm
             */
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT A.id as id1, A.name as file1, A.type as type1, A.startline as start1, A.endline as end1,\n" +
                            "  B.id as id2, B.name as file2, B.type as type2, B.startline as start2, B.endline as end2\n" +
                            "FROM clones\n" +
                            "  INNER JOIN functions AS A ON function_id_one = A.id\n" +
                            "  INNER JOIN functions AS B ON function_id_two = B.id\n" +
                            "WHERE syntactic_type <= 1\n" +
                            "  AND A.endline - A.startline + 1 >= " + minCloneSize + "\n" +
                            "  AND B.endline - B.startline + 1 >= " + minCloneSize + "\n" +
                            "  AND function_id_one=" + cloneId + "\n" +
                            "ORDER BY clones.syntactic_type ASC, B.name ASC;");
            while (rs.next()) {
                query.setId(String.valueOf(rs.getInt("id1")));
                query.setFile(rs.getString("type1") + "/" + rs.getString("file1"));
                query.setStartline(rs.getInt("start1"));
                query.setEndline(rs.getInt("end1"));

                Document d = new Document();
                int id = rs.getInt("id2");
                String file = rs.getString("type2") + "/" + rs.getString("file2");
                int start = rs.getInt("start2");
                int end = rs.getInt("end2");
                d.setId(String.valueOf(id));
                d.setFile(file);
                d.setStartline(start);
                d.setEndline(end);

                cloneList.add(d);
            }

            // add the query in front of the list
            cloneList.add(0, query);
            rs.close();
            stmt.close();

            return cloneList;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }
    }

    private int checkResults(String result, ArrayList<Document> groundTruth) {
        String[] resultSplit = result.split("#");
        for (int i=1; i<groundTruth.size(); i++) {
            Document d = groundTruth.get(i);
//            System.out.println(resultSplit[0].substring(0, resultSplit[0].indexOf("_")));
            if (resultSplit[0].substring(0, resultSplit[0].indexOf("_")).equals(d.getFile()) &&
                    Integer.valueOf(resultSplit[1]) == d.getStartLine() &&
                    Integer.valueOf(resultSplit[2]) == d.getEndLine()) {
                return i;
            }
        }
        return -1;
    }

    public double evaluateType1Query(ArrayList<Document> groundTruth, String outputFile, String errMeasure) {
//        System.out.println("Evaluating Average Precision ...");
        double averagePrec = -1;
        Document groundTruthQuery = groundTruth.get(0);

        try {
            /* copied from http://howtodoinjava.com/3rd-party/parse-read-write-csv-files-opencsv-tutorial/ */
            CSVReader reader = new CSVReader(new FileReader(outputFile), ',', '"', 0);
            String[] nextLine;
            double sumPrecision;
            int queryInResult = 0;
            int checkedResultCount = 0;

            while ((nextLine = reader.readNext()) != null) {
                int tp = 0;
                String[] query = nextLine[0].split("#");

                // only consider the one in the ground truth, discard other
                if (getQueryFileName(query[0]).equals(groundTruthQuery.getFile())
                        && Integer.valueOf(query[1]) == groundTruthQuery.getStartLine()
                        && Integer.valueOf(query[2]) == groundTruthQuery.getEndLine()) {

                    sumPrecision = 0.0;

                    // check the results with the key
                    for (int i = 1; i <= groundTruth.size(); i++) {

                        // check if we still have results to process
                        // (some searches do not return all results).
                        if (i < nextLine.length) {
                            // skip blank result and skip the result of the query itself
                            if (!nextLine[i].equals("")) {
                                if (!isQuery(groundTruthQuery, nextLine[i])) {
                                    checkedResultCount++;
                                    if (checkResults(nextLine[i], groundTruth) != -1) {
                                        tp++;
                                        // calculate precision every time a relevant result is obtained.
                                        float precision = (float) tp / checkedResultCount;
                                        sumPrecision += precision;
                                        System.out.println("correct output#" + i + ": " + nextLine[i]);
                                    } else {
                                        System.out.println("wrong output#" + i + ": " + nextLine[i]);
                                    }
                                }
                            } else {
                                System.out.println("found query, skip.");
                            }
                        }
                        // found all relevant results, stop
                        if (tp == groundTruth.size() - 1)
                            break;
                    }
                    System.out.println("Additional output#: " + nextLine[groundTruth.size() + 1]);
                    averagePrec = sumPrecision / checkedResultCount;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return averagePrec;
    }

    private String getQueryFileName(String fileWithMethodName) {
        return fileWithMethodName.split("_")[0].trim();
    }

    private boolean isQuery(Document query, String result) {
        String[] resultSplit = result.split("#");
        return getQueryFileName(query.getFile()).equals(getQueryFileName(resultSplit[0].trim())) &&
                query.getStartLine() == Integer.valueOf(resultSplit[1]) &&
                query.getEndLine() == Integer.valueOf(resultSplit[2]);
    }

    public void printGroundTruth(ArrayList<Document> groundTruth) {
        Document d = groundTruth.get(0);
        System.out.println("Q: " + d.getFile() + "(" + d.getStartLine() + "," + d.getEndLine() + ")");
//        for (int i=1; i<groundTruth.size(); i++) {
//            d = groundTruth.get(i);
//            System.out.println(d.getFile() + "(" + d.getStartLine() + "," + d.getEndLine() + ")");
//        }
    }
}
