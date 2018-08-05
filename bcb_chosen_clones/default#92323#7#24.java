    public static boolean getConnection(String dsn) {
        Connection con = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String url = "jdbc:odbc:" + dsn;
            con = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            return false;
        } catch (SQLException e) {
            return false;
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
