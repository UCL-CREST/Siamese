    public void createTable() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilename);
        Statement stat = conn.createStatement();
        stat.executeUpdate("drop table if exists " + TABLE_DURATION + ";");
        stat.executeUpdate("create table " + TABLE_DURATION + " (date, file, target, duration);");
    }
