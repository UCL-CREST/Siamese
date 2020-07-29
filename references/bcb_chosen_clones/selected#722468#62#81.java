    public int next() {
        int sequenceValue = current();
        try {
            Update update = dbi.getUpdate();
            update.setTableName(sequenceTable);
            update.assignValue("SEQUENCE_VALUE", --sequenceValue);
            Search search = new Search();
            search.addAttributeCriteria(sequenceTable, "SEQUENCE_NAME", Search.EQUAL, sequenceName);
            update.where(search);
            int affectedRows = dbi.getConnection().createStatement().executeUpdate(update.toString());
            if (affectedRows == 1) {
                dbi.getConnection().commit();
            } else {
                dbi.getConnection().rollback();
            }
        } catch (SQLException sqle) {
            System.err.println("SQLException occurred in current(): " + sqle.getMessage());
        }
        return sequenceValue;
    }
