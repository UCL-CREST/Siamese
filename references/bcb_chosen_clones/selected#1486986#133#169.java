    @Override
    public boolean putDescription(String uuid, String description) throws DatabaseException {
        if (uuid == null) throw new NullPointerException("uuid");
        if (description == null) throw new NullPointerException("description");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        boolean found = true;
        try {
            PreparedStatement findSt = getConnection().prepareStatement(SELECT_COMMON_DESCRIPTION_STATEMENT);
            PreparedStatement updSt = null;
            findSt.setString(1, uuid);
            ResultSet rs = findSt.executeQuery();
            found = rs.next();
            int modified = 0;
            updSt = getConnection().prepareStatement(found ? UPDATE_COMMON_DESCRIPTION_STATEMENT : INSERT_COMMON_DESCRIPTION_STATEMENT);
            updSt.setString(1, description);
            updSt.setString(2, uuid);
            modified = updSt.executeUpdate();
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + findSt + "\" and \"" + updSt + "\"");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + findSt + "\" and \"" + updSt + "\"");
                found = false;
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            found = false;
        } finally {
            closeConnection();
        }
        return found;
    }
