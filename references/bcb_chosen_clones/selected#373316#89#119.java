    public FileBean create(MimeTypeBean mimeType, SanBean san) throws SQLException {
        long fileId = 0;
        DataSource ds = getDataSource(DEFAULT_DATASOURCE);
        Connection conn = ds.getConnection();
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute(NEXT_FILE_ID);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                fileId = rs.getLong(NEXTVAL);
            }
            PreparedStatement pstmt = conn.prepareStatement(INSERT_FILE);
            pstmt.setLong(1, fileId);
            pstmt.setLong(2, mimeType.getId());
            pstmt.setLong(3, san.getId());
            pstmt.setLong(4, WORKFLOW_ATTENTE_VALIDATION);
            int nbrow = pstmt.executeUpdate();
            if (nbrow == 0) {
                throw new SQLException();
            }
            conn.commit();
            closeRessources(conn, pstmt);
        } catch (SQLException e) {
            log.error("Can't FileDAOImpl.create " + e.getMessage());
            conn.rollback();
            throw e;
        }
        FileBean fileBean = new FileBean();
        return fileBean;
    }
