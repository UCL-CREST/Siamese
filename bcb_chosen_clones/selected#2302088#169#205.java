    private void runUpdate(String sql, boolean transactional) {
        log().info("Vacuumd executing statement: " + sql);
        Connection dbConn = null;
        boolean commitRequired = false;
        boolean autoCommitFlag = !transactional;
        try {
            dbConn = getDataSourceFactory().getConnection();
            dbConn.setAutoCommit(autoCommitFlag);
            PreparedStatement stmt = dbConn.prepareStatement(sql);
            int count = stmt.executeUpdate();
            stmt.close();
            if (log().isDebugEnabled()) {
                log().debug("Vacuumd: Ran update " + sql + ": this affected " + count + " rows");
            }
            commitRequired = transactional;
        } catch (SQLException ex) {
            log().error("Vacuumd:  Database error execuating statement  " + sql, ex);
        } finally {
            if (dbConn != null) {
                try {
                    if (commitRequired) {
                        dbConn.commit();
                    } else if (transactional) {
                        dbConn.rollback();
                    }
                } catch (SQLException ex) {
                } finally {
                    if (dbConn != null) {
                        try {
                            dbConn.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }
