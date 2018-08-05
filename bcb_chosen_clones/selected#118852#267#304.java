    @Override
    public String addUserIdentity(OpenIDItem identity, long userId) throws DatabaseException {
        if (identity == null) throw new NullPointerException("identity");
        if (identity.getIdentity() == null || "".equals(identity.getIdentity())) throw new NullPointerException("identity.getIdentity()");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        String retID = "exist";
        PreparedStatement insSt = null, seqSt = null;
        try {
            int modified = 0;
            insSt = getConnection().prepareStatement(INSERT_IDENTITY_STATEMENT);
            insSt.setLong(1, userId);
            insSt.setString(2, identity.getIdentity());
            modified = insSt.executeUpdate();
            seqSt = getConnection().prepareStatement(USER_IDENTITY_VALUE);
            ResultSet rs = seqSt.executeQuery();
            while (rs.next()) {
                retID = rs.getString(1);
            }
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + seqSt + "\" and \"" + insSt + "\"");
            } else {
                getConnection().rollback();
                LOGGER.debug("DB has not been updated -> rollback! Queries: \"" + seqSt + "\" and \"" + insSt + "\"");
                retID = "error";
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            retID = "error";
        } finally {
            closeConnection();
        }
        return retID;
    }
