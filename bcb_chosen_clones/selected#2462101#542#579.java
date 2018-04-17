    public void setDefaultMailBox(final int domainId, final int userId) {
        final EmailAddress defaultMailbox = cmDB.getDefaultMailbox(domainId);
        try {
            connection.setAutoCommit(false);
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty(defaultMailbox == null ? "domain.setDefaultMailbox" : "domain.updateDefaultMailbox"));
                    if (defaultMailbox == null) {
                        psImpl.setInt(1, domainId);
                        psImpl.setInt(2, userId);
                    } else {
                        psImpl.setInt(1, userId);
                        psImpl.setInt(2, domainId);
                    }
                    psImpl.executeUpdate();
                }
            });
            connection.commit();
            cmDB.updateDomains(null, null);
        } catch (SQLException sqle) {
            log.error(sqle);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
            }
        }
    }
