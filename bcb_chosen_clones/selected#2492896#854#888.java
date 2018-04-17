    private void duplicateTable(Connection scon, Connection dcon, String table) {
        logger.debug("Duplicating table " + table);
        Statement creTab, stmt;
        ResultSet tuples, columns, keys;
        int c;
        String insert = "";
        PreparedStatement insTup;
        try {
            columns = scon.getMetaData().getColumns(null, null, table, null);
            keys = scon.getMetaData().getPrimaryKeys(null, null, table);
            creTab = dcon.createStatement();
            creTab.execute(getCreateTableCommand(columns, keys));
            stmt = scon.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(50);
            tuples = stmt.executeQuery("SELECT * FROM " + table);
            c = tuples.getMetaData().getColumnCount();
            insert = "INSERT INTO " + table + " VALUES(";
            for (int j = 1; j <= c; j++) insert += "?,";
            insert = insert.substring(0, insert.length() - 1) + ")";
            logger.debug("Insert pattern " + insert);
            insTup = dcon.prepareStatement(insert);
            while (tuples.next()) {
                for (int j = 1; j <= c; j++) insTup.setObject(j, tuples.getObject(j));
                insTup.executeUpdate();
            }
            dcon.commit();
        } catch (Exception e) {
            logger.error("Unable to copy table " + table + ": " + e);
            try {
                dcon.rollback();
            } catch (SQLException e1) {
                logger.fatal(e1);
            }
        }
    }
