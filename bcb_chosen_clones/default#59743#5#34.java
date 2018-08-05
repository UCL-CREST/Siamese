    public static void main(String[] args) {
        try {
            String hostname = "localhost";
            if (args.length != 0) hostname = args[0];
            System.out.println("Loading database on '" + hostname + "'");
            System.out.println(FidoDatabase.getDescription());
            System.out.println();
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + hostname + "/template1", "postgres", "");
            Statement stmt = conn.createStatement();
            FidoDatabase.dropDatabase(stmt);
            FidoDatabase.dropFidoUser(stmt);
            FidoDatabase.createDatabase(stmt);
            FidoDatabase.createFidoUser(stmt);
            stmt.close();
            conn.close();
            conn = DriverManager.getConnection("jdbc:postgresql://" + hostname + "/fido", "fido", "");
            stmt = conn.createStatement();
            System.out.println();
            System.out.println("Creating tables in main fido database");
            System.out.println();
            FidoDatabase.createTables(stmt);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error instantiating PostgreSQL JDBC driver: " + e.getMessage());
        }
    }
