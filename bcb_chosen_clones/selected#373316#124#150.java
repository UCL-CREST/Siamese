    public int deleteFile(Integer[] fileID) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = null;
        int nbrow = 0;
        try {
            DataSource ds = getDataSource(DEFAULT_DATASOURCE);
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            if (log.isDebugEnabled()) {
                log.debug("FileDAOImpl.deleteFile() " + DELETE_FILES_LOGIC);
            }
            for (int i = 0; i < fileID.length; i++) {
                pstmt = conn.prepareStatement(DELETE_FILES_LOGIC);
                pstmt.setInt(1, fileID[i].intValue());
                nbrow = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            conn.rollback();
            log.error("FileDAOImpl.deleteFile() : erreur technique", e);
            throw e;
        } finally {
            conn.commit();
            closeRessources(conn, pstmt, rs);
        }
        return nbrow;
    }
