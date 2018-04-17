    protected int insertRecord(PutMetadataRequest request, PutMetadataInfo info) throws ImsServiceException, SQLException {
        Connection con = null;
        boolean autoCommit = true;
        PreparedStatement st = null;
        ResultSet rs = null;
        int nRows = 0;
        String sXml = info.getXml();
        String sUuid = info.getUuid();
        String sName = info.getName();
        String sThumbnailBinary = info.getThumbnailBinary();
        String sTable = this.getResourceTableName();
        String sDataTable = this.getResourceDataTableName();
        long id = doesRecordExist(sTable, sUuid);
        try {
            ManagedConnection mc = returnConnection();
            con = mc.getJdbcConnection();
            autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            if (id < 0) {
                StringBuffer sql = new StringBuffer();
                sql.append("INSERT INTO ").append(sTable);
                sql.append(" (");
                sql.append("DOCUUID,");
                sql.append("TITLE,");
                sql.append("OWNER");
                sql.append(")");
                sql.append(" VALUES(?,?,?)");
                logExpression(sql.toString());
                st = con.prepareStatement(sql.toString());
                int n = 1;
                st.setString(n++, sUuid);
                st.setString(n++, sName);
                st.setInt(n++, this.publisher.getLocalID());
                nRows = st.executeUpdate();
                closeStatement(st);
                if (nRows > 0) {
                    if (getIsDbCaseSensitive(this.getRequestContext())) {
                        st = con.prepareStatement("SELECT id FROM " + sTable + " WHERE UPPER(docuuid)=?");
                    } else {
                        st = con.prepareStatement("SELECT id FROM " + sTable + " WHERE docuuid=?");
                    }
                    st.setString(1, sUuid.toUpperCase());
                    rs = st.executeQuery();
                    rs.next();
                    id = rs.getLong(1);
                    closeStatement(st);
                    request.setActionStatus(ImsRequest.ACTION_STATUS_OK);
                    sql = new StringBuffer();
                    sql.append("INSERT INTO ").append(sDataTable);
                    sql.append(" (DOCUUID,ID,XML)");
                    sql.append(" VALUES(?,?,?)");
                    logExpression(sql.toString());
                    st = con.prepareStatement(sql.toString());
                    st.setString(1, sUuid);
                    st.setLong(2, id);
                    st.setString(3, sXml);
                    st.executeUpdate();
                }
            } else {
                this.authorize(request, sUuid);
                StringBuffer sql = new StringBuffer();
                sql.append("UPDATE ").append(sTable);
                sql.append(" SET ");
                if (!request.getLockTitle()) {
                    sql.append("TITLE=?, ");
                }
                sql.append("OWNER=?, ");
                sql.append("UPDATEDATE=?");
                sql.append(" WHERE DOCUUID=?");
                logExpression(sql.toString());
                st = con.prepareStatement(sql.toString());
                int n = 1;
                if (!request.getLockTitle()) {
                    st.setString(n++, sName);
                }
                st.setInt(n++, this.publisher.getLocalID());
                st.setTimestamp(n++, new Timestamp(System.currentTimeMillis()));
                st.setString(n++, sUuid);
                nRows = st.executeUpdate();
                if (nRows > 0) {
                    request.setActionStatus(ImsRequest.ACTION_STATUS_REPLACED);
                }
                closeStatement(st);
                sql = new StringBuffer();
                if (doesRecordExist(sDataTable, sUuid) >= 0) {
                    sql.append("UPDATE ").append(sDataTable);
                    sql.append(" SET DOCUUID=?, XML=?, THUMBNAIL=?");
                    sql.append(" WHERE ID=?");
                } else {
                    sql.append("INSERT INTO ").append(sDataTable);
                    sql.append(" (DOCUUID, XML,THUMBNAIL,ID)");
                    sql.append(" VALUES(?,?,?,?)");
                }
                logExpression(sql.toString());
                st = con.prepareStatement(sql.toString());
                st.setString(1, sUuid);
                st.setString(2, sXml);
                st.setBytes(3, null);
                st.setLong(4, id);
                st.executeUpdate();
            }
            con.commit();
        } catch (ImsServiceException ex) {
            if (con != null) {
                con.rollback();
            }
            throw ex;
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
        if ((sThumbnailBinary != null) && (sThumbnailBinary.length() > 0)) {
            this.updateThumbnail(sThumbnailBinary, sUuid);
        }
        return nRows;
    }
