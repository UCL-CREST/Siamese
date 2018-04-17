    @Override
    public synchronized void deleteHttpSessionStatistics(String contextName, String project, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getHttpSessionInvocationsSchemaAndTableName() + " FROM " + this.getHttpSessionInvocationsSchemaAndTableName() + " INNER JOIN " + this.getHttpSessionElementsSchemaAndTableName() + " ON " + this.getHttpSessionElementsSchemaAndTableName() + ".element_id =  " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id WHERE ";
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if (project != null) {
                queryString = queryString + " project LIKE ? AND ";
            }
            if (dateFrom != null) {
                queryString = queryString + " start_timestamp >= ? AND ";
            }
            if (dateTo != null) {
                queryString = queryString + " start_timestamp <= ? AND ";
            }
            queryString = DefaultDatabaseHandler.removeOrphanWhereAndAndFromSelect(queryString);
            final PreparedStatement preparedStatement = DebugPreparedStatement.prepareStatement(connection, queryString);
            int indexCounter = 1;
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if (project != null) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateFrom.getTime()));
                indexCounter = indexCounter + 1;
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(indexCounter, new Timestamp(dateTo.getTime()));
                indexCounter = indexCounter + 1;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (final SQLException e) {
            try {
                connection.rollback();
            } catch (final SQLException ex) {
                JeeObserverServerContext.logger.log(Level.SEVERE, "Transaction rollback error.", ex);
            }
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage());
            throw new DatabaseException("Error deleting HTTP session statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }
