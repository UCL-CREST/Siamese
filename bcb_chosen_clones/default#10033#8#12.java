    public Errors() throws ClassNotFoundException, SQLException {
        String url = "jdbc:odbc:StockTracker";
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        con = DriverManager.getConnection(url);
    }
