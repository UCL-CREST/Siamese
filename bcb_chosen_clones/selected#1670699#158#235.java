    private void insert(Connection c) throws SQLException {
        if (m_fromDb) throw new IllegalStateException("The record already exists in the database");
        StringBuffer names = new StringBuffer("INSERT INTO ifServices (nodeID,ipAddr,serviceID");
        StringBuffer values = new StringBuffer("?,?,?");
        if ((m_changed & CHANGED_IFINDEX) == CHANGED_IFINDEX) {
            values.append(",?");
            names.append(",ifIndex");
        }
        if ((m_changed & CHANGED_STATUS) == CHANGED_STATUS) {
            values.append(",?");
            names.append(",status");
        }
        if ((m_changed & CHANGED_LASTGOOD) == CHANGED_LASTGOOD) {
            values.append(",?");
            names.append(",lastGood");
        }
        if ((m_changed & CHANGED_LASTFAIL) == CHANGED_LASTFAIL) {
            values.append(",?");
            names.append(",lastFail");
        }
        if ((m_changed & CHANGED_SOURCE) == CHANGED_SOURCE) {
            values.append(",?");
            names.append(",source");
        }
        if ((m_changed & CHANGED_NOTIFY) == CHANGED_NOTIFY) {
            values.append(",?");
            names.append(",notify");
        }
        if ((m_changed & CHANGED_QUALIFIER) == CHANGED_QUALIFIER) {
            values.append(",?");
            names.append(",qualifier");
        }
        names.append(") VALUES (").append(values).append(')');
        if (log().isDebugEnabled()) log().debug("DbIfServiceEntry.insert: SQL insert statment = " + names.toString());
        PreparedStatement stmt = null;
        PreparedStatement delStmt = null;
        final DBUtils d = new DBUtils(getClass());
        try {
            stmt = c.prepareStatement(names.toString());
            d.watch(stmt);
            names = null;
            int ndx = 1;
            stmt.setInt(ndx++, m_nodeId);
            stmt.setString(ndx++, m_ipAddr.getHostAddress());
            stmt.setInt(ndx++, m_serviceId);
            if ((m_changed & CHANGED_IFINDEX) == CHANGED_IFINDEX) stmt.setInt(ndx++, m_ifIndex);
            if ((m_changed & CHANGED_STATUS) == CHANGED_STATUS) stmt.setString(ndx++, new String(new char[] { m_status }));
            if ((m_changed & CHANGED_LASTGOOD) == CHANGED_LASTGOOD) {
                stmt.setTimestamp(ndx++, m_lastGood);
            }
            if ((m_changed & CHANGED_LASTFAIL) == CHANGED_LASTFAIL) {
                stmt.setTimestamp(ndx++, m_lastFail);
            }
            if ((m_changed & CHANGED_SOURCE) == CHANGED_SOURCE) stmt.setString(ndx++, new String(new char[] { m_source }));
            if ((m_changed & CHANGED_NOTIFY) == CHANGED_NOTIFY) stmt.setString(ndx++, new String(new char[] { m_notify }));
            if ((m_changed & CHANGED_QUALIFIER) == CHANGED_QUALIFIER) stmt.setString(ndx++, m_qualifier);
            int rc;
            try {
                rc = stmt.executeUpdate();
            } catch (SQLException e) {
                log().warn("ifServices DB insert got exception; will retry after " + "deletion of any existing records for this ifService " + "that are marked for deletion.", e);
                c.rollback();
                String delCmd = "DELETE FROM ifServices WHERE status = 'D' " + "AND nodeid = ? AND ipAddr = ? AND serviceID = ?";
                delStmt = c.prepareStatement(delCmd);
                d.watch(delStmt);
                delStmt.setInt(1, m_nodeId);
                delStmt.setString(2, m_ipAddr.getHostAddress());
                delStmt.setInt(3, m_serviceId);
                rc = delStmt.executeUpdate();
                rc = stmt.executeUpdate();
            }
            log().debug("insert(): SQL update result = " + rc);
        } finally {
            d.cleanUp();
        }
        m_fromDb = true;
        m_changed = 0;
    }
