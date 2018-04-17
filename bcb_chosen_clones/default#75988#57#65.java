    private Connection getConnection(String dbName) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:cloudscape:" + dbName + ";create=true");
        } catch (SQLException sqe) {
            System.err.println("Couldn't access " + dbName);
        }
        return con;
    }
