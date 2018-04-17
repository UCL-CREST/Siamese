    static final void saveStatus(JWAIMStatus status, DBConnector connector) throws IOException {
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        try {
            con = connector.getDB();
            con.setAutoCommit(false);
            st = con.createStatement();
            st.executeUpdate("DELETE FROM status");
            ps = con.prepareStatement("INSERT INTO status VALUES (?, ?)");
            ps.setString(1, "jwaim.status");
            ps.setBoolean(2, status.getJWAIMStatus());
            ps.addBatch();
            ps.setString(1, "logging.status");
            ps.setBoolean(2, status.getLoggingStatus());
            ps.addBatch();
            ps.setString(1, "stats.status");
            ps.setBoolean(2, status.getStatsStatus());
            ps.addBatch();
            ps.executeBatch();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new IOException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignore) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
