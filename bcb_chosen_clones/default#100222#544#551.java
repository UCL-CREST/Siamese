        public Connection getConnection() throws SQLException {
            try {
                Class.forName(JDBC_DRIVER_CLASS);
            } catch (Exception e) {
                throw new SQLException("Driver class " + JDBC_DRIVER_CLASS + " not found");
            }
            return DriverManager.getConnection(JDBC_CONNECTION_STRING, JDBC_USER_NAME, JDBC_PASSWORD);
        }
