    public boolean startConnection(String ip, String port, String databaseName, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(("jdbc:mysql://" + ip + ":" + port + "/" + databaseName), username, password);
            stmt = con.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
