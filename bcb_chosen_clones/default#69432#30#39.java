    public static java.sql.Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String surl = getConnectionUrl();
            con = java.sql.DriverManager.getConnection(surl, ConConfig.getInst().getUserName(), ConConfig.getInst().getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
