    public static Connection getConnection() throws Exception {
        Class.forName(JDBC_DRIVER_CLASS);
        return DriverManager.getConnection(JDBC_CONNECTION_STRING, JDBC_USER_NAME, JDBC_PASSWORD);
    }
