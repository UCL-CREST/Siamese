    public void setInternalReferences() {
        for (int i = 0; i < REFSPECS.length; i++) {
            REFSPECS[i].setTypeRefs(conn);
        }
        String sql, sql2;
        try {
            String[][] params2 = { { "PACKAGE", "name" }, { "CLASSTYPE", "qualifiedname" }, { "MEMBER", "qualifiedname" }, { "EXECMEMBER", "fullyqualifiedname" } };
            for (int i = 0; i < params2.length; i++) {
                log.traceln("\tProcessing seetag " + params2[i][0] + " references..");
                sql = "select r.sourcedoc_id, " + params2[i][0] + ".id, " + params2[i][0] + "." + params2[i][1] + " from REFERENCE r, " + params2[i][0] + " where r.refdoc_name = " + params2[i][0] + "." + params2[i][1] + " and r.refdoc_id is null";
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery(sql);
                sql2 = "update REFERENCE set refdoc_id=? where sourcedoc_id=? and refdoc_name=?";
                PreparedStatement pstmt = conn.prepareStatement(sql2);
                while (rset.next()) {
                    pstmt.clearParameters();
                    pstmt.setInt(1, rset.getInt(2));
                    pstmt.setInt(2, rset.getInt(1));
                    pstmt.setString(3, rset.getString(3));
                    pstmt.executeUpdate();
                }
                pstmt.close();
                rset.close();
                stmt.close();
                conn.commit();
            }
        } catch (SQLException ex) {
            log.error("Internal Reference Update Failed!");
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try {
                conn.rollback();
            } catch (SQLException inner_ex) {
                log.error("rollback failed!");
            }
        }
    }
