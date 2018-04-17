    public static void connection() throws Exception {
        try {
            String userName = "<db_user>";
            String password = "<password>";
            String url = "jdbc:mysql://<hostname>/<databasename>";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = (Connection) DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            System.out.println("Cannot connect to database server");
            throw e;
        }
    }
