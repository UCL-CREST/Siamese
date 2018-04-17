    public SQLResultsParser(WCConnection connection, String sqlString) throws SQLException {
        String sql = SQLUtils.stripComments(sqlString);
        sql = SQLUtils.stripWhiteSpace(sql);
        processSQL(sql);
        Matcher matches = mySelect.matcher(sql);
        if (!matches.find()) {
            throw new SQLException("ResultSetMetaData only works with SELECT queries. SQL: " + sql, "S1009");
        }
        try {
            Class<?> metaClass = Class.forName(connection.getDbPackagePath() + "SQLMetaGetaImp");
            Constructor<?> ct = metaClass.getConstructor(new Class[] { connection.getClass() });
            SQLMetaGeta metaG = (SQLMetaGeta) ct.newInstance(new Object[] { connection });
            fieldSet = metaG.getResultSetMetaData(sql, table2Column);
        } catch (Throwable e) {
            throw new SQLException("Could not construct a SQLMetaGeta Object", e);
        }
    }
