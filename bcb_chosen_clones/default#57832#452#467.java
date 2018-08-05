    protected void dbCloseConnection() {
        String protocol = properties.getProperty("protocol");
        try {
            if (protocol.indexOf("derby") != -1) {
                connection.close();
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } else {
                if (protocol.indexOf("hsqldb") != -1) {
                    connection.createStatement().execute(peek(properties, "shutdown"));
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
