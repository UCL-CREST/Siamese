    private void handleNodeDown(long eventID, long nodeID, String eventTime) {
        Category log = ThreadCategory.getInstance(OutageWriter.class);
        if (eventID == -1 || nodeID == -1) {
            log.warn(EventConstants.NODE_DOWN_EVENT_UEI + " ignored - info incomplete - eventid/nodeid: " + eventID + "/" + nodeID);
            return;
        }
        Connection dbConn = null;
        try {
            dbConn = DatabaseConnectionFactory.getInstance().getConnection();
            try {
                dbConn.setAutoCommit(false);
            } catch (SQLException sqle) {
                log.error("Unable to change database AutoCommit to FALSE", sqle);
                return;
            }
            PreparedStatement activeSvcsStmt = dbConn.prepareStatement(OutageConstants.DB_GET_ACTIVE_SERVICES_FOR_NODE);
            PreparedStatement openStmt = dbConn.prepareStatement(OutageConstants.DB_OPEN_RECORD);
            PreparedStatement newOutageWriter = dbConn.prepareStatement(OutageConstants.DB_INS_NEW_OUTAGE);
            PreparedStatement getNextOutageIdStmt = dbConn.prepareStatement(OutageManagerConfigFactory.getInstance().getGetNextOutageID());
            newOutageWriter = dbConn.prepareStatement(OutageConstants.DB_INS_NEW_OUTAGE);
            if (log.isDebugEnabled()) log.debug("handleNodeDown: creating new outage entries...");
            activeSvcsStmt.setLong(1, nodeID);
            ResultSet activeSvcsRS = activeSvcsStmt.executeQuery();
            while (activeSvcsRS.next()) {
                String ipAddr = activeSvcsRS.getString(1);
                long serviceID = activeSvcsRS.getLong(2);
                if (openOutageExists(dbConn, nodeID, ipAddr, serviceID)) {
                    if (log.isDebugEnabled()) log.debug("handleNodeDown: " + nodeID + "/" + ipAddr + "/" + serviceID + " already down");
                } else {
                    long outageID = -1;
                    ResultSet seqRS = getNextOutageIdStmt.executeQuery();
                    if (seqRS.next()) {
                        outageID = seqRS.getLong(1);
                    }
                    seqRS.close();
                    newOutageWriter.setLong(1, outageID);
                    newOutageWriter.setLong(2, eventID);
                    newOutageWriter.setLong(3, nodeID);
                    newOutageWriter.setString(4, ipAddr);
                    newOutageWriter.setLong(5, serviceID);
                    newOutageWriter.setTimestamp(6, convertEventTimeIntoTimestamp(eventTime));
                    newOutageWriter.executeUpdate();
                    if (log.isDebugEnabled()) log.debug("handleNodeDown: Recording outage for " + nodeID + "/" + ipAddr + "/" + serviceID);
                }
            }
            activeSvcsRS.close();
            try {
                dbConn.commit();
                if (log.isDebugEnabled()) log.debug("Outage recorded for all active services for " + nodeID);
            } catch (SQLException se) {
                log.warn("Rolling back transaction, nodeDown could not be recorded  for nodeId: " + nodeID, se);
                try {
                    dbConn.rollback();
                } catch (SQLException sqle) {
                    log.warn("SQL exception during rollback, reason", sqle);
                }
            }
            activeSvcsStmt.close();
            openStmt.close();
            newOutageWriter.close();
        } catch (SQLException sqle) {
            log.warn("SQL exception while handling \'nodeDown\'", sqle);
        } finally {
            try {
                if (dbConn != null) dbConn.close();
            } catch (SQLException e) {
                log.warn("Exception closing JDBC connection", e);
            }
        }
    }
