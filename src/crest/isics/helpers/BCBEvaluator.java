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
    public ArrayList<Integer> getType1CloneIds(int limit, int minClonePairs) {
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
                            "FROM clones\n" +
                            "WHERE syntactic_type=1\n" +
                            "GROUP BY function_id_one\n" +
                            "-- ORDER BY random()\n" +
                            "LIMIT " + limit + ";");
            while (rs.next()) {
                int id1 = rs.getInt("function_id_one");
//                int count = rs.getInt("count");
                cloneList.add(id1);
//                System.out.println(id1 + ", " + count);
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
                    "SELECT A.id as id1, A.name as file1, A.startline as start1, A.endline as end1,\n" +
                            "B.id as id2, B.name as file2, B.startline as start2, B.endline as end2\n" +
                            "FROM clones\n" +
                            "  INNER JOIN functions AS A ON function_id_one = A.id\n" +
                            "  INNER JOIN functions AS B ON function_id_two = B.id\n" +
                            "WHERE syntactic_type=1\n" +
                            "  AND A.endline - A.startline + 1 >= " + minCloneSize + "\n" +
                            "  AND B.endline - B.startline + 1 >= " + minCloneSize + "\n" +
                            "AND function_id_one=" + cloneId + ";");
            while (rs.next()) {
                query.setId(String.valueOf(rs.getInt("id1")));
                query.setFile(rs.getString("file1"));
                query.setStartline(rs.getInt("start1"));
                query.setEndline(rs.getInt("end1"));

                Document d = new Document();
                int id = rs.getInt("id2");
                String file = rs.getString("file2");
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
            if (resultSplit[0].substring(resultSplit[0].indexOf("_")).equals(d.getFile()) &&
                    Integer.valueOf(resultSplit[1]) == d.getStartLine() &&
                    Integer.valueOf(resultSplit[2]) == d.getEndLine()) {
                return i;
            }
        }
        return -1;
    }

    public double evaluateType1Clones(ArrayList<Document> groundTruth, String outputFile, String errMeasure) {
        System.out.println("Evaluating MAP ...");
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
                // increase query count
                noOfQueries++;

                // check the results with the key
                for (int i = 1; i <= groundTruth.size() - 1; i++) {
                    // check if we still have results to process
                    // (some searches do not return all results.
                    if (i < nextLine.length) {
                        if (!nextLine[i].equals("")) {
                            if (checkResults(nextLine[i], groundTruth) != -1) {
                                tp++;
                                // calculate precision every time a relevant result is obtained.
                                float precision = (float) tp / (i - 1);
                                sumPrecision += precision;
                            }
                        }
                    }

                    // found all relevant results, stop
                    if (tp == groundTruth.size() - 1)
                        break;
                }

                double averagePrec = sumPrecision / (groundTruth.size() - 1);
                sumAvgPrec += averagePrec;
            }

            // calculate MAP
            map = sumAvgPrec/noOfQueries;
            System.out.println("No. of processed queries = " + noOfQueries);
            System.out.println("MAP = " + map + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
