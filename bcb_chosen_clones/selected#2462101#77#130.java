    public void deleteDomain(final List<Integer> domainIds) {
        try {
            connection.setAutoCommit(false);
            final int defaultDomainId = ((DomainDb) cmDB.getDefaultDomain()).getDomainId();
            boolean defaultDomainDeleted = (Boolean) new ProcessEnvelope().executeObject(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public Object executeProcessReturnObject() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("domain.delete"));
                    Iterator<Integer> iter = domainIds.iterator();
                    int domainId;
                    boolean defaultDomainDeleted = false;
                    while (iter.hasNext()) {
                        domainId = iter.next();
                        if (!defaultDomainDeleted) defaultDomainDeleted = defaultDomainId == domainId;
                        psImpl.setInt(1, domainId);
                        psImpl.executeUpdate();
                    }
                    return defaultDomainDeleted;
                }
            });
            if (defaultDomainDeleted) {
                new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                    @Override
                    public void executeProcessReturnNull() throws SQLException {
                        psImpl = connImpl.prepareStatement(sqlCommands.getProperty("domain.setDefaultDomainId"));
                        psImpl.setInt(1, -1);
                        psImpl.executeUpdate();
                    }
                });
            }
            connection.commit();
            cmDB.updateDomains(null, null);
            if (defaultDomainDeleted) {
                cm.updateDefaultDomain();
            }
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
