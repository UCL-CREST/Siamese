    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
            createTables();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
