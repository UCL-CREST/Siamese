    public StockTrackerDB() throws ClassNotFoundException, SQLException {
        if (con == null) {
            String url = "jdbc:odbc:StockTracker";
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            } catch (ClassNotFoundException ex) {
                throw new ClassNotFoundException(ex.getMessage() + "\nCannot locate sun.jdbc.odbc.JdbcOdbcDriver");
            }
            try {
                con = DriverManager.getConnection(url);
            } catch (SQLException ex) {
                throw new SQLException(ex.getMessage() + "\nCannot open database connection for " + url);
            }
        }
    }
