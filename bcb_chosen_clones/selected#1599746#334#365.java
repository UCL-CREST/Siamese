    public void updateDBModel(Tasks task) throws Exception {
        task.setOperationName("Updating DB Model..");
        int localVersion = getLocalModelVersion();
        int currentVersion = 0;
        try {
            currentVersion = getModelVersion();
        } catch (SQLException e) {
            if (e.getErrorCode() != 1146) {
                throw e;
            }
        }
        boolean autoCommit = getConn().getAutoCommit();
        try {
            getConn().setAutoCommit(false);
            for (int version = currentVersion + 1; version <= localVersion; version++) {
                task.setStatus("Updating to version " + version);
                InputStream in = EDACCApp.class.getClassLoader().getResourceAsStream("edacc/resources/db_version/" + version + ".sql");
                if (in == null) {
                    throw new SQLQueryFileNotFoundException();
                }
                executeSqlScript(task, in);
                Statement st = getConn().createStatement();
                st.executeUpdate("INSERT INTO `Version` VALUES (" + version + ", NOW())");
                st.close();
            }
        } catch (Exception e) {
            getConn().rollback();
            throw e;
        } finally {
            getConn().setAutoCommit(autoCommit);
        }
    }
