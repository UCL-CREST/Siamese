    private void populateAPI(API api) {
        try {
            if (api.isPopulated()) {
                log.traceln("Skipping API " + api.getName() + " (already populated)");
                return;
            }
            api.setPopulated(true);
            String sql = "update API set populated=1 where name=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, api.getName());
            pstmt.executeUpdate();
            pstmt.close();
            storePackagesAndClasses(api);
            conn.commit();
        } catch (SQLException ex) {
            log.error("Store (api: " + api.getName() + ") failed!");
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try {
                conn.rollback();
            } catch (SQLException inner_ex) {
                log.error("rollback failed!");
            }
        }
    }
