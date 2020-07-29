    public QueryOutput run() throws Exception {
        boolean success = false;
        QueryOutput output = null;
        if (correlator != null || inMemMaster != null || customMatcher != null) {
            List<Object[]> rows = inMemMaster == null ? (correlator == null ? customMatcher.onCycleEnd() : correlator.onCycleEnd()) : inMemMaster.onCycleEnd();
            if (rows.isEmpty()) {
                success = true;
                return null;
            }
            output = new DirectQueryOutput(rows);
        } else {
            connection = queryContext.createConnection();
            try {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                thePreparedStatement = connection.prepareStatement(thePreparedStatementSQL);
                RowStatusHelper.setStatusValues(statusAndPositions, thePreparedStatement, queryContext.getRunCount());
                long newResultIdsAfter = lastRowIdInsertedNow;
                int rows = thePreparedStatement.executeUpdate();
                if (rows <= 0) {
                    success = true;
                    return null;
                }
                lastRowIdInsertedNow = getLastRowIdInResultTable(newResultIdsAfter, rows);
                output = new DBQueryOutput(newResultIdsAfter, lastRowIdInsertedNow, rows, timeKeeper.getTimeMsecs());
                success = true;
            } finally {
                if (connection != null) {
                    if (success) {
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                }
            }
        }
        return output;
    }
