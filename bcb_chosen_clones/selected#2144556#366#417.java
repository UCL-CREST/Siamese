    @Override
    public synchronized void deleteHardDiskStatistics(String contextName, String path, Date dateFrom, Date dateTo) throws DatabaseException {
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            String queryString = "DELETE " + this.getHardDiskInvocationsSchemaAndTableName() + " FROM " + this.getHardDiskInvocationsSchemaAndTableName() + " INNER JOIN " + this.getHardDiskElementsSchemaAndTableName() + " ON " + this.getHardDiskElementsSchemaAndTableName() + ".element_id =  " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id WHERE ";
            if (contextName != null) {
                queryString = queryString + " context_name LIKE ? AND ";
            }
            if (path != null) {
                queryString = queryString + " path LIKE ? AND ";
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
            if (path != null) {
                preparedStatement.setString(indexCounter, path);
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
            throw new DatabaseException("Error deleting disk statistics.", e);
        } finally {
            this.releaseConnection(connection);
        }
    }
