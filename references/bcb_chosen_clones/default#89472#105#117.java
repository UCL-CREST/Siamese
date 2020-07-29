    public void addValues(String date, String antFilename, String target, double duration) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilename);
        Statement stat = conn.createStatement();
        PreparedStatement prep = conn.prepareStatement("insert into execution values (?, ?, ?, ?);");
        prep.setString(1, date);
        prep.setString(2, antFilename);
        prep.setString(3, target);
        prep.setDouble(4, duration);
        prep.addBatch();
        prep.executeBatch();
        conn.close();
    }
