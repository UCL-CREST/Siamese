    @Override
    protected void removeOrphansElements() throws DatabaseException {
        this.getIdChache().clear();
        final Connection connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement;
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallElementsSchemaAndTableName() + " FROM " + this.getCallElementsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallElementsSchemaAndTableName() + ".element_id =  " + this.getCallInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallExceptionsSchemaAndTableName() + " FROM " + this.getCallExceptionsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallExceptionsSchemaAndTableName() + ".exception_id =  " + this.getCallInvocationsSchemaAndTableName() + ".exception_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".exception_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getCallPrincipalsSchemaAndTableName() + " FROM " + this.getCallPrincipalsSchemaAndTableName() + " LEFT JOIN " + this.getCallInvocationsSchemaAndTableName() + " ON " + this.getCallPrincipalsSchemaAndTableName() + ".principal_id =  " + this.getCallInvocationsSchemaAndTableName() + ".principal_id WHERE " + this.getCallInvocationsSchemaAndTableName() + ".principal_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHttpSessionElementsSchemaAndTableName() + " FROM " + this.getHttpSessionElementsSchemaAndTableName() + " LEFT JOIN " + this.getHttpSessionInvocationsSchemaAndTableName() + " ON " + this.getHttpSessionElementsSchemaAndTableName() + ".element_id =  " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getHttpSessionInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getJvmElementsSchemaAndTableName() + " FROM " + this.getJvmElementsSchemaAndTableName() + " LEFT JOIN " + this.getJvmInvocationsSchemaAndTableName() + " ON " + this.getJvmElementsSchemaAndTableName() + ".element_id =  " + this.getJvmInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getJvmInvocationsSchemaAndTableName() + ".element_id IS NULL");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceEntityElementsSchemaAndTableName() + " FROM " + this.getPersistenceEntityElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceEntityStatisticsSchemaAndTableName() + " ON " + this.getPersistenceEntityElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_id WHERE " + this.getPersistenceEntityStatisticsSchemaAndTableName() + ".element_id IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getPersistenceQueryElementsSchemaAndTableName() + " FROM " + this.getPersistenceQueryElementsSchemaAndTableName() + " LEFT JOIN " + this.getPersistenceQueryStatisticsSchemaAndTableName() + " ON " + this.getPersistenceQueryElementsSchemaAndTableName() + ".element_id =  " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id WHERE " + this.getPersistenceQueryStatisticsSchemaAndTableName() + ".element_id IS NULL ");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = DebugPreparedStatement.prepareStatement(connection, "DELETE " + this.getHardDiskElementsSchemaAndTableName() + " FROM " + this.getHardDiskElementsSchemaAndTableName() + " LEFT JOIN " + this.getHardDiskInvocationsSchemaAndTableName() + " ON " + this.getHardDiskElementsSchemaAndTableName() + ".element_id =  " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id WHERE " + this.getHardDiskInvocationsSchemaAndTableName() + ".element_id IS NULL ");
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
            throw new DatabaseException("Error cleaning database.", e);
        } finally {
            this.releaseConnection(connection);
        }
        return;
    }
