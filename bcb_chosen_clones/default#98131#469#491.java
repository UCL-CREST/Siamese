    public Connection connect(String userName, String password, String url) {
        Connection connection = null;
        boolean err = false;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            System.err.println("Error loading MySQL driver: " + e.getMessage());
            err = true;
        }
        if (!err) {
            try {
                connection = DriverManager.getConnection(url, userName, password);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                System.err.println(String.format("Unable to connect to database server using url '%s', user '%s', and password '%s': msg is: %s", url, userName, password, e.getMessage()));
                System.err.println("SQLState: " + e.getSQLState());
                System.err.println("VendorError: " + e.getErrorCode());
                err = true;
            }
        }
        if (err) System.exit(1);
        return connection;
    }
