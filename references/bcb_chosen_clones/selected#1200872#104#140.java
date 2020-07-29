    public void save(Connection conn, boolean commit) throws SQLException {
        PreparedStatement stmt = null;
        if (!isValid()) {
            String errorMessage = "Unable to save invalid DAO '" + getClass().getName() + "'!";
            if (log.isErrorEnabled()) {
                log.error(errorMessage);
            }
            throw new SQLException(errorMessage);
        }
        try {
            if (isNew()) {
                primaryKey = createNewPrimaryKey();
                stmt = conn.prepareStatement(getInsertSql());
            } else {
                stmt = conn.prepareStatement(getUpdateSql());
            }
            setValues(stmt);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1) {
                primaryKey = OvUuid.NULL_UUID;
                if (commit) {
                    conn.rollback();
                }
                String errorMessage = "Invalid number of rows changed!";
                if (log.isErrorEnabled()) {
                    log.error(errorMessage);
                }
                throw new SQLException(errorMessage);
            } else {
                if (commit) {
                    conn.commit();
                }
            }
        } finally {
            OvJdbcUtils.closeStatement(stmt);
        }
    }
