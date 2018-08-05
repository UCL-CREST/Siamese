    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class driver = Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, USER_ID, PASSWORD);
        return conn;
    }
