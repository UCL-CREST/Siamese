    private static void initConnection() throws SQLException {
        String host, name, user, password, url;
        if (connection != null) return;
        try {
            host = Configuration.getOption("database", "host");
            name = Configuration.getOption("database", "name");
            user = Configuration.getOption("database", "user");
            password = Configuration.getOption("database", "password");
            url = String.format("jdbc:mysql://%s/%s", host, name);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new SQLException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
    }
