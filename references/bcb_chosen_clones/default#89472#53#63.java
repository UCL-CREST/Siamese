    public double getLastDuration(String antFilename, String target) throws Exception {
        double duration = 0;
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilename);
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from " + TABLE_DURATION + " where (file LIKE '" + antFilename + "' AND target LIKE '" + target + "') ORDER by date DESC;");
        duration = rs.getDouble("duration");
        rs.close();
        conn.close();
        return duration;
    }
