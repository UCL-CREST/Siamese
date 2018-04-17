    private int saveToTempTable(ArrayList cons, String tempTableName, boolean truncateFirst) throws SQLException {
        if (truncateFirst) {
            this.executeUpdate("TRUNCATE TABLE " + tempTableName);
            Categories.dataDb().debug("TABLE " + tempTableName + " TRUNCATED.");
        }
        PreparedStatement ps = null;
        int rows = 0;
        try {
            String insert = "INSERT INTO " + tempTableName + " VALUES (?)";
            ps = this.conn.prepareStatement(insert);
            for (int i = 0; i < cons.size(); i++) {
                ps.setLong(1, ((Long) cons.get(i)).longValue());
                rows = ps.executeUpdate();
                if ((i % 500) == 0) {
                    this.conn.commit();
                }
            }
            this.conn.commit();
        } catch (SQLException sqle) {
            this.conn.rollback();
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return rows;
    }
