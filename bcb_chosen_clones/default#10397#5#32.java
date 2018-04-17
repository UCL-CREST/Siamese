    public static void main(String[] Args) {
        Connection conn = null;
        try {
            String dbURL = "jdbc:hsqldb:/tmp/yacht";
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection(dbURL, "sa", "");
        } catch (Exception e) {
            System.out.println("ERROR: Could not connect to database");
            System.out.print(e);
        }
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("create table news (news object)");
            stmt.executeUpdate("create table roster (player object)");
            stmt.executeUpdate("create table enemies (enemy object)");
            stmt.executeUpdate("create table exp (exp object)");
            stmt.executeUpdate("create table map (map object)");
            stmt.executeUpdate("create table mapside (mapside object)");
            stmt.executeUpdate("create table role (role object)");
            stmt.executeUpdate("create table server (server object)");
            stmt.executeUpdate("create table battleplan (battleplan object)");
            stmt.executeUpdate("create table calendar (match object)");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
