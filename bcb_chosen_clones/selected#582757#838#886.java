    public void updateFailedStatus(THLEventStatus failedEvent, ArrayList<THLEventStatus> events) throws THLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            if (events != null && events.size() > 0) {
                String seqnoList = buildCommaSeparatedList(events);
                stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE history SET status = " + THLEvent.FAILED + ", comments = 'Event was rollbacked due to failure while processing event#" + failedEvent.getSeqno() + "'" + ", processed_tstamp = " + conn.getNowFunction() + " WHERE seqno in " + seqnoList);
            }
            pstmt = conn.prepareStatement("UPDATE history SET status = ?" + ", comments = ?" + ", processed_tstamp = ?" + " WHERE seqno = ?");
            pstmt.setShort(1, THLEvent.FAILED);
            pstmt.setString(2, truncate(failedEvent.getException() != null ? failedEvent.getException().getMessage() : "Unknown failure", commentLength));
            pstmt.setTimestamp(3, now);
            pstmt.setLong(4, failedEvent.getSeqno());
            pstmt.executeUpdate();
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
