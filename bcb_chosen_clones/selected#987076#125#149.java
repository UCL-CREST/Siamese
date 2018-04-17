    protected int doExecuteInsert(PreparedStatement statement, Table data) throws SQLException {
        ResultSet rs = null;
        int result = -1;
        try {
            lastError = null;
            result = statement.executeUpdate();
            if (!isAutoCommit()) connection.commit();
            rs = statement.getGeneratedKeys();
            while (rs.next()) {
                FieldUtils.setValue(data, data.key, rs.getObject(1));
            }
        } catch (SQLException ex) {
            if (!isAutoCommit()) {
                lastError = ex;
                connection.rollback();
                LogUtils.log(Level.SEVERE, "Transaction is being rollback. Error: " + ex.toString());
            } else {
                throw ex;
            }
        } finally {
            if (statement != null) statement.close();
            if (rs != null) rs.close();
        }
        return result;
    }
