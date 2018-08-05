    public double getAverage(String antFilename, String target) throws Exception {
        int duration = 0;
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilename);
        Statement stat = conn.createStatement();
        String select = " WHERE (target = '" + target + "' AND file = '" + antFilename + "')";
        String func = "avg";
        String sql = "select " + func + "(duration) as '" + func + "' from " + TABLE_DURATION + " " + select + ";";
        ResultSet rs = stat.executeQuery(sql);
        double ret = rs.getDouble(1);
        rs.close();
        conn.close();
        return ret;
    }
