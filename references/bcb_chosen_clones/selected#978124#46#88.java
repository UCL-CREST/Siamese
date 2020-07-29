    public static ResultSet execute(String commands) {
        ResultSet rs = null;
        BufferedReader reader = new BufferedReader(new StringReader(commands));
        String sqlCommand = null;
        Connection conn = ConnPool.getConnection();
        try {
            Statement stmt = conn.createStatement();
            while ((sqlCommand = reader.readLine()) != null) {
                sqlCommand = sqlCommand.toLowerCase().trim();
                if (sqlCommand.equals("") || sqlCommand.startsWith("#")) {
                    continue;
                }
                if (dmaLogger.isInfoEnabled(SqlExecutor.class)) {
                    dmaLogger.logInfo("Executing SQL: " + sqlCommand, SqlExecutor.class);
                }
                long currentTimeMillis = System.currentTimeMillis();
                if (sqlCommand.startsWith("select")) {
                    rs = stmt.executeQuery(sqlCommand);
                } else {
                    stmt.executeUpdate(sqlCommand);
                }
                dmaLogger.logInfo(DateUtil.getElapsedTime("SQL execution of " + sqlCommand + "  took: ", (System.currentTimeMillis() - currentTimeMillis)), SqlExecutor.class);
            }
            if (rs == null) {
                stmt.close();
            }
            return rs;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException se) {
            }
            throw new RuntimeException("Execution of " + sqlCommand + " failed:" + e.getMessage(), e);
        } catch (IOException e) {
            try {
                conn.rollback();
            } catch (SQLException se) {
            }
            throw new RuntimeException("Execution of " + sqlCommand + " failed:", e);
        } finally {
            ConnPool.releaseConnection(conn);
        }
    }
