    private void handleInterfaceUp(long eventID, long nodeID, String ipAddr, String eventTime) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (eventID == -1 || nodeID == -1 || ipAddr == null) {
            log.warn(EventConstants.INTERFACE_UP_EVENT_UEI + " ignored - info incomplete - eventid/nodeid/ipAddr: " + eventID + "/" + nodeID + "/" + ipAddr);
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            if (openOutageExists(dbConn, nodeID, ipAddr)) {
                try {
                    dbConn.setAutoCommit(false);
                } catch (SQLException sqle) {
                    log.error("Unable to change database AutoCommit to FALSE", sqle);
                    return;
                }
                PreparedStatement outageUpdater = dbConn.prepareStatement(OutageConstants.DB_UPDATE_OUTAGES_FOR_INTERFACE);
                outageUpdater.setLong(1, eventID);
                outageUpdater.setTimestamp(2, convertEventTimeIntoTimestamp(eventTime));
                outageUpdater.setLong(3, nodeID);
                outageUpdater.setString(4, ipAddr);
                int count = outageUpdater.executeUpdate();
                outageUpdater.close();
                try {
                    dbConn.commit();
                    if (log.isDebugEnabled()) log.debug("handleInterfaceUp: interfaceUp closed " + count + " outages for nodeid/ip " + nodeID + "/" + ipAddr + " in DB");
                } catch (SQLException se) {
                    log.warn("Rolling back transaction, interfaceUp could not be recorded for nodeId/ipaddr: " + nodeID + "/" + ipAddr, se);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.warn("SQL exception during rollback, reason: ", sqle);
                    }
                }
            } else {
                log.warn("\'" + EventConstants.INTERFACE_UP_EVENT_UEI + "\' for " + nodeID + "/" + ipAddr + " ignored.");
            }
        } catch (SQLException se) {
            log.warn("SQL exception while handling \'interfaceUp\'", se);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }
