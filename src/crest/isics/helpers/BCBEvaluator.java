package crest.isics.helpers;

import java.sql.Connection;

public class BCBEvaluator extends Evaluator  {
    public BCBEvaluator() {
        super();
    }

    public BCBEvaluator(String clonePairFile, String index, String outputDir, boolean isPrint) {
//        super(clonePairFile, index, outputDir, isPrint);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver Registered!");
        Connection connection = null;
    }
}
