    public int deleteRecord(String uuid) throws SQLException, CatalogIndexException {
        Connection con = null;
        boolean autoCommit = true;
        PreparedStatement st = null;
        ResultSet rs = null;
        int nRows = 0;
        boolean cancelTask = false;
        StringSet fids = new StringSet();
        if (cswRemoteRepository.isActive()) {
            StringSet uuids = new StringSet();
            uuids.add(uuid);
            fids = queryFileIdentifiers(uuids);
        }
        try {
            con = returnConnection().getJdbcConnection();
            autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            String sSql = "SELECT COUNT(*) FROM " + getResourceTableName() + " WHERE DOCUUID=? AND PROTOCOL_TYPE IS NOT NULL AND PROTOCOL_TYPE<>''";
            logExpression(sSql);
            st = con.prepareStatement(sSql);
            st.setString(1, uuid);
            rs = st.executeQuery();
            if (rs.next()) {
                cancelTask = rs.getInt(1) > 0;
            }
            closeStatement(st);
            sSql = "DELETE FROM " + getResourceTableName() + " WHERE DOCUUID=?";
            logExpression(sSql);
            st = con.prepareStatement(sSql);
            st.setString(1, uuid);
            nRows = st.executeUpdate();
            closeStatement(st);
            sSql = "DELETE FROM " + getResourceDataTableName() + " WHERE DOCUUID=?";
            logExpression(sSql);
            st = con.prepareStatement(sSql);
            st.setString(1, uuid);
            st.executeUpdate();
            CollectionDao colDao = new CollectionDao(this.getRequestContext());
            if (colDao.getUseCollections()) {
                closeStatement(st);
                sSql = "DELETE FROM " + colDao.getCollectionMemberTableName() + " WHERE DOCUUID=?";
                logExpression(sSql);
                st = con.prepareStatement(sSql);
                st.setString(1, uuid);
                st.executeUpdate();
            }
            con.commit();
        } catch (SQLException ex) {
            if (con != null) {
                con.rollback();
            }
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(st);
            if (con != null) {
                con.setAutoCommit(autoCommit);
            }
        }
        CatalogIndexAdapter indexAdapter = getCatalogIndexAdapter();
        if (indexAdapter != null) {
            indexAdapter.deleteDocument(uuid);
            if (cswRemoteRepository.isActive()) {
                if (fids.size() > 0) cswRemoteRepository.onRecordsDeleted(fids);
            }
        }
        if (cancelTask && getRequestContext() != null) {
            getRequestContext().getApplicationContext().getHarvestingEngine().cancel(getRequestContext(), uuid);
        }
        return nRows;
    }
