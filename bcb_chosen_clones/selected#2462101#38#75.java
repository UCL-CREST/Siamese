    public void insertDomain(final List<String> domains) {
        try {
            connection.setAutoCommit(false);
            new ProcessEnvelope().executeNull(new ExecuteProcessAbstractImpl(connection, false, false, true, true) {

                @Override
                public void executeProcessReturnNull() throws SQLException {
                    psImpl = connImpl.prepareStatement(sqlCommands.getProperty("domain.add"));
                    Iterator<String> iter = domains.iterator();
                    String domain;
                    while (iter.hasNext()) {
                        domain = iter.next();
                        psImpl.setString(1, domain);
                        psImpl.setString(2, domain.toLowerCase(locale));
                        psImpl.executeUpdate();
                    }
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
                    log.error(ex);
                }
            }
        }
    }
