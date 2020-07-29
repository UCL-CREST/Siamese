    static void createStatements(Map map, String basename) throws Exception {
        FileOutputStream sqlFile;
        String[] createStrings;
        String url = "jdbc:postgresql:sports?user=nobody&password=PASSWORD", driver = "org.postgresql.Driver";
        Connection conn;
        Class.forName(driver);
        conn = DriverManager.getConnection(url);
        map.setConnection(conn);
        sqlFile = new FileOutputStream(basename + ".sql");
        createStrings = map.getCreateTableStrings();
        for (int i = 0; i < createStrings.length; i++) {
            sqlFile.write(createStrings[i].getBytes());
            sqlFile.write(RETURN);
        }
        sqlFile.close();
    }
