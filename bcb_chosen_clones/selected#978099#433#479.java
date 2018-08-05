    private void handleNodeUp(long eventID, long nodeID, String eventTime) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (eventID == -1 || nodeID == -1) {
            log.warn(EventConstants.NODE_UP_EVENT_UEI + " ignored - info incomplete - eventid/nodeid: " + eventID + "/" + nodeID);
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            int count = 0;
            if (openOutageExists(dbConn, nodeID)) {
                try {
                    dbConn.setAutoCommit(false);
                } catch (SQLException sqle) {
                    log.error("Unable to change database AutoCommit to FALSE", sqle);
                    return;
                }
                PreparedStatement outageUpdater = dbConn.prepareStatement(OutageConstants.DB_UPDATE_OUTAGES_FOR_NODE);
                outageUpdater.setLong(1, eventID);
                outageUpdater.setTimestamp(2, convertEventTimeIntoTimestamp(eventTime));
                outageUpdater.setLong(3, nodeID);
                count = outageUpdater.executeUpdate();
                outageUpdater.close();
            } else {
                log.warn("\'" + EventConstants.NODE_UP_EVENT_UEI + "\' for " + nodeID + " no open record.");
            }
            try {
                dbConn.commit();
                if (log.isDebugEnabled()) log.debug("nodeUp closed " + count + " outages for nodeid " + nodeID + " in DB");
            } catch (SQLException se) {
                log.warn("Rolling back transaction, nodeUp could not be recorded  for nodeId: " + nodeID, se);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.warn("SQL exception during rollback, reason", sqle);
                }
            }
        } catch (SQLException se) {
            log.warn("SQL exception while handling \'nodeRegainedService\'", se);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }
