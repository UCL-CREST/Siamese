    protected void downgradeHistory(Collection<String> versions) {
        Assert.notEmpty(versions);
        try {
            Connection connection = this.database.getDefaultConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE " + this.logTableName + " SET RESULT = 'DOWNGRADED' WHERE TYPE = 'B' AND TARGET = ? AND RESULT = 'COMPLETE'");
            boolean commit = false;
            try {
                for (String version : versions) {
                    statement.setString(1, version);
                    int modified = statement.executeUpdate();
                    Assert.isTrue(modified <= 1, "Expecting not more than 1 record to be updated, not " + modified);
                }
                commit = true;
            } finally {
                statement.close();
                if (commit) connection.commit(); else connection.rollback();
            }
        } catch (SQLException e) {
            throw new SystemException(e);
        }
    }
