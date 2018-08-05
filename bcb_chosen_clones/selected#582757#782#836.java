    public void updateSuccessStatus(ArrayList<THLEventStatus> succeededEvents, ArrayList<THLEventStatus> skippedEvents) throws THLException {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            if (succeededEvents != null && succeededEvents.size() > 0) {
                stmt = conn.createStatement();
                String seqnoList = buildCommaSeparatedList(succeededEvents);
                stmt.executeUpdate("UPDATE " + history + " SET status = " + THLEvent.COMPLETED + ", processed_tstamp = " + conn.getNowFunction() + " WHERE seqno in " + seqnoList);
            }
            if (skippedEvents != null && skippedEvents.size() > 0) {
                pstmt = conn.prepareStatement("UPDATE " + history + " SET status = ?, comments = ?," + " processed_tstamp = ? WHERE seqno = ?");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                for (THLEventStatus event : skippedEvents) {
                    pstmt.setShort(1, THLEvent.SKIPPED);
                    pstmt.setString(2, truncate(event.getException() != null ? event.getException().getMessage() : "Unknown event failure", commentLength));
                    pstmt.setTimestamp(3, now);
                    pstmt.setLong(4, event.getSeqno());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                pstmt.close();
            }
            conn.commit();
        } catch (SQLException e) {
            THLException exception = new THLException("Failed to update events status");
            exception.initCause(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                THLException exception2 = new THLException("Failed to rollback after failure while updating events status");
                e1.initCause(exception);
                exception2.initCause(e1);
                exception = exception2;
            }
            throw exception;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignore) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ignore) {
                }
            }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }
