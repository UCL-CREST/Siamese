    public boolean startConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(("jdbc:mysql://" + MYSQL_IP + ":" + MYSQL_PORT + "/" + MYSQL_DATABASE_NAME), MYSQL_USERNAME, MYSQL_PASSWORD);
            stmt = con.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
