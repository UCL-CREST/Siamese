    private void connectDBase() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
        }
        try {
            con = DriverManager.getConnection(d1 + filename + d2);
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
