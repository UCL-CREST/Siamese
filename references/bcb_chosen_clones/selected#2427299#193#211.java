    protected int deleteBitstreamInfo(int id, Connection conn) {
        PreparedStatement stmt = null;
        int numDeleted = 0;
        try {
            stmt = conn.prepareStatement(DELETE_BITSTREAM_INFO);
            stmt.setInt(1, id);
            numDeleted = stmt.executeUpdate();
            if (numDeleted > 1) {
                conn.rollback();
                throw new IllegalStateException("Too many rows deleted! Number of rows deleted: " + numDeleted + " only one row should be deleted for bitstream id " + id);
            }
        } catch (SQLException e) {
            LOG.error("Problem deleting bitstream. " + e.getMessage(), e);
            throw new RuntimeException("Problem deleting bitstream. " + e.getMessage(), e);
        } finally {
            cleanup(stmt);
        }
        return numDeleted;
    }
