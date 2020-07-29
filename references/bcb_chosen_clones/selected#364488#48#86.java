    public void setTypeRefs(Connection conn) {
        log.traceln("\tProcessing " + table + " references..");
        try {
            String query = " select distinct c.id, c.qualifiedname from " + table + ", CLASSTYPE c " + " where " + table + "." + reffield + " is null and " + table + "." + classnamefield + " = c.qualifiedname";
            PreparedStatement pstmt = conn.prepareStatement(query);
            long start = new Date().getTime();
            ResultSet rset = pstmt.executeQuery();
            long queryTime = new Date().getTime() - start;
            log.debug("query time: " + queryTime + " ms");
            String update = "update " + table + " set " + reffield + "=? where " + classnamefield + "=? and " + reffield + " is null";
            PreparedStatement pstmt2 = conn.prepareStatement(update);
            int n = 0;
            start = new Date().getTime();
            while (rset.next()) {
                n++;
                pstmt2.setInt(1, rset.getInt(1));
                pstmt2.setString(2, rset.getString(2));
                pstmt2.executeUpdate();
            }
            queryTime = new Date().getTime() - start;
            log.debug("total update time: " + queryTime + " ms");
            log.debug("number of times through loop: " + n);
            if (n > 0) log.debug("avg update time: " + (queryTime / n) + " ms");
            pstmt2.close();
            rset.close();
            pstmt.close();
            conn.commit();
            log.verbose("Updated (committed) " + table + " references");
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
