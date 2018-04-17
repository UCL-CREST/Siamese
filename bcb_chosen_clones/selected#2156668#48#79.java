    @Override
    public void updateItems(List<InputQueueItem> toUpdate) throws DatabaseException {
        if (toUpdate == null) throw new NullPointerException("toUpdate");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        try {
            PreparedStatement deleteSt = getConnection().prepareStatement(DELETE_ALL_ITEMS_STATEMENT);
            PreparedStatement selectCount = getConnection().prepareStatement(SELECT_NUMBER_ITEMS_STATEMENT);
            ResultSet rs = selectCount.executeQuery();
            rs.next();
            int totalBefore = rs.getInt(1);
            int deleted = deleteSt.executeUpdate();
            int updated = 0;
            for (InputQueueItem item : toUpdate) {
                updated += getItemInsertStatement(item).executeUpdate();
            }
            if (totalBefore == deleted && updated == toUpdate.size()) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + selectCount + "\" and \"" + deleteSt + "\".");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + selectCount + "\" and \"" + deleteSt + "\".");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection();
        }
    }
