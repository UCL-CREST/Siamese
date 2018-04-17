    private void duplicateIndices(Connection scon, Connection dcon, String table) {
        try {
            String idx_name, idx_att, query;
            ResultSet idxs = scon.getMetaData().getIndexInfo(null, null, table, false, false);
            Statement stmt = dcon.createStatement();
            while (idxs.next()) {
                idx_name = idxs.getString(6);
                idx_att = idxs.getString(9);
                idx_name += "_" + idx_att + "_idx";
                logger.debug("Creating index " + idx_name);
                query = "CREATE INDEX " + idx_name + " ON " + table + "(" + idx_att + ")";
                stmt.executeUpdate(query);
                dcon.commit();
            }
        } catch (Exception e) {
            logger.error("Unable to copy indices " + e);
            try {
                dcon.rollback();
            } catch (SQLException e1) {
                logger.fatal(e1);
            }
        }
    }
