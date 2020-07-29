    public void readData(RowSetInternal caller) throws SQLException {
        Connection con = null;
        try {
            CachedRowSet crs = (CachedRowSet) caller;
            if (crs.getPageSize() == 0 && crs.size() > 0) {
                crs.close();
            }
            writerCalls = 0;
            userCon = false;
            con = this.connect(caller);
            if (con == null || crs.getCommand() == null) throw new SQLException(resBundle.handleGetObject("crsreader.connecterr").toString());
            try {
                con.setTransactionIsolation(crs.getTransactionIsolation());
            } catch (Exception ex) {
                ;
            }
            PreparedStatement pstmt = con.prepareStatement(crs.getCommand());
            decodeParams(caller.getParams(), pstmt);
            try {
                pstmt.setMaxRows(crs.getMaxRows());
                pstmt.setMaxFieldSize(crs.getMaxFieldSize());
                pstmt.setEscapeProcessing(crs.getEscapeProcessing());
                pstmt.setQueryTimeout(crs.getQueryTimeout());
            } catch (Exception ex) {
                throw new SQLException(ex.getMessage());
            }
            if (crs.getCommand().toLowerCase().indexOf("select") != -1) {
                ResultSet rs = pstmt.executeQuery();
                if (crs.getPageSize() == 0) {
                    crs.populate(rs);
                } else {
                    pstmt = con.prepareStatement(crs.getCommand(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    decodeParams(caller.getParams(), pstmt);
                    try {
                        pstmt.setMaxRows(crs.getMaxRows());
                        pstmt.setMaxFieldSize(crs.getMaxFieldSize());
                        pstmt.setEscapeProcessing(crs.getEscapeProcessing());
                        pstmt.setQueryTimeout(crs.getQueryTimeout());
                    } catch (Exception ex) {
                        throw new SQLException(ex.getMessage());
                    }
                    rs = pstmt.executeQuery();
                    crs.populate(rs, startPosition);
                }
                rs.close();
            } else {
                pstmt.executeUpdate();
            }
            pstmt.close();
            try {
                con.commit();
            } catch (SQLException ex) {
                ;
            }
            if (getCloseConnection() == true) con.close();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try {
                if (con != null && getCloseConnection() == true) {
                    try {
                        if (!con.getAutoCommit()) {
                            con.rollback();
                        }
                    } catch (Exception dummy) {
                    }
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
            }
        }
    }
