    @Override
    public final boolean delete() throws RecordException {
        if (frozen) {
            throw new RecordException("The object is frozen.");
        }
        Connection conn = ConnectionManager.getConnection();
        LoggableStatement pStat = null;
        Class<? extends Record> actualClass = this.getClass();
        StatementBuilder builder = null;
        try {
            builder = new StatementBuilder("delete from " + TableNameResolver.getTableName(actualClass) + " where id = :id");
            Field f = FieldHandler.findField(this.getClass(), "id");
            builder.set("id", FieldHandler.getValue(f, this));
            pStat = builder.getPreparedStatement(conn);
            log.log(pStat.getQueryString());
            int i = pStat.executeUpdate();
            return i == 1;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
