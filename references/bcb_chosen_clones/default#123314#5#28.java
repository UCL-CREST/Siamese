    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@big:1521:orcl", "chirico", "mike");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from jobs");
            ResultSetMetaData rsmd = rs.getMetaData();
            String s = "";
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    System.out.print(s + rs.getString(i));
                    s = ",";
                }
                s = "";
                System.out.println("");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Oops! Got a SQL error: " + e.getMessage());
            System.exit(1);
        }
    }
