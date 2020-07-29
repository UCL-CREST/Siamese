    @Override
    public synchronized void deletePersistenceQueryStatistics(Integer elementId, String contextName, String project, String name, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " FROM " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " INNER JOIN " + this.getPersistenceQueryElementsSchemaAndTableName() + " ON " + this.getPersistenceQueryElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id WHERE ";
            if (elementId != null) {
                queryString = queryString + " elementId = ? AND ";
            }
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if ((project != null)) {
                queryString = queryString + " project LIKE ? AND ";
            }
            if ((name != null)) {
                queryString = queryString + " name LIKE ? AND ";
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
            if (elementId != null) {
                preparedStatement.setLong(indexCounter, elementId.longValue());
                indexCounter = indexCounter + 1;
            }
            if (contextName != null) {
                preparedStatement.setString(indexCounter, contextName);
                indexCounter = indexCounter + 1;
            }
            if ((project != null)) {
                preparedStatement.setString(indexCounter, project);
                indexCounter = indexCounter + 1;
            }
            if ((name != null)) {
                preparedStatement.setString(indexCounter, name);
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
            throw new DatabaseException("Error deleting persistence query statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }
