    private void handleNodeLostService(long eventID, long nodeID, String ipAddr, long serviceID, String eventTime) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (eventID == -1 || nodeID == -1 || ipAddr == null || serviceID == -1) {
            log.warn(EventConstants.NODE_REGAINED_SERVICE_EVENT_UEI + " ignored - info incomplete - eventid/nodeid/ip/svc: " + eventID + "/" + nodeID + "/" + ipAddr + "/" + serviceID);
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            if (openOutageExists(dbConn, nodeID, ipAddr, serviceID)) {
                log.warn("\'" + EventConstants.NODE_LOST_SERVICE_EVENT_UEI + "\' for " + nodeID + "/" + ipAddr + "/" + serviceID + " ignored - table already  has an open record ");
            } else {
                PreparedStatement getNextOutageIdStmt = dbConn.prepareStatement(OutageManagerConfigFactory.getInstance().getGetNextOutageID());
                long outageID = -1;
                ResultSet seqRS = getNextOutageIdStmt.executeQuery();
                if (seqRS.next()) {
                    outageID = seqRS.getLong(1);
                }
                seqRS.close();
                try {
                    dbConn.setAutoCommit(false);
                } catch (SQLException sqle) {
                    log.error("Unable to change database AutoCommit to FALSE", sqle);
                    return;
                }
                PreparedStatement newOutageWriter = null;
                if (log.isDebugEnabled()) log.debug("handleNodeLostService: creating new outage entry...");
                newOutageWriter = dbConn.prepareStatement(OutageConstants.DB_INS_NEW_OUTAGE);
                newOutageWriter.setLong(1, outageID);
                newOutageWriter.setLong(2, eventID);
                newOutageWriter.setLong(3, nodeID);
                newOutageWriter.setString(4, ipAddr);
                newOutageWriter.setLong(5, serviceID);
                newOutageWriter.setTimestamp(6, convertEventTimeIntoTimestamp(eventTime));
                newOutageWriter.executeUpdate();
                newOutageWriter.close();
                try {
                    dbConn.commit();
                    if (log.isDebugEnabled()) log.debug("nodeLostService : " + nodeID + "/" + ipAddr + "/" + serviceID + " recorded in DB");
                } catch (SQLException se) {
                    log.warn("Rolling back transaction, nodeLostService could not be recorded  for nodeid/ipAddr/service: " + nodeID + "/" + ipAddr + "/" + serviceID, se);
                    try {
                        dbConn.rollback();
                    } catch (SQLException sqle) {
                        log.warn("SQL exception during rollback, reason", sqle);
                    }
                }
            }
        } catch (SQLException sqle) {
            log.warn("SQL exception while handling \'nodeLostService\'", sqle);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }
