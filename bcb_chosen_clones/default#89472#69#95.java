    public void dumpTable(String query) throws Exception {
        Class.forName("org.sqlite.JDBC");
        String SEPARATOR = ";";
        String sqlQuery = "select * from " + TABLE_DURATION + " ORDER by date;";
        if (!query.equals("")) {
            sqlQuery = query;
        }
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilename);
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sqlQuery);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        String columns = "";
        int columnCount = rsMetaData.getColumnCount();
        for (int c = 1; c <= columnCount; c++) {
            columns += rsMetaData.getColumnName(c);
            if (c < columnCount) columns += SEPARATOR;
        }
        System.out.println(columns);
        while (rs.next()) {
            columns = "";
            for (int c = 1; c <= columnCount; c++) {
                columns += rs.getString(c);
                if (c < rsMetaData.getColumnCount()) columns += SEPARATOR;
            }
            System.out.println(columns);
        }
    }
