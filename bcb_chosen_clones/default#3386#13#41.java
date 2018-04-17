    public static void main(String args[]) {
        System.out.println("JDBC BLOB Streaming Test");
        System.out.println("java.class.path: " + System.getProperty("java.class.path"));
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root&password=&enableBlobStreaming=true");
            execute(conn, "CREATE TABLE IF NOT EXISTS mybs_test_tab (n_id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT, n_text LONGBLOB) ENGINE=PBXT");
            funcTest(conn);
            bigDataTest(conn);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e2) {
                }
            }
        }
    }
