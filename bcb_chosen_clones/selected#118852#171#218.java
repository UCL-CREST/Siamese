    @Override
    public String addUser(UserInfoItem user) throws DatabaseException {
        if (user == null) throw new NullPointerException("user");
        if (user.getSurname() == null || "".equals(user.getSurname())) throw new NullPointerException("user.getSurname()");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        String retID = "exist";
        PreparedStatement insSt = null, updSt = null, seqSt = null;
        try {
            int modified = 0;
            if (user.getId() != null) {
                long id = Long.parseLong(user.getId());
                updSt = getConnection().prepareStatement(UPDATE_USER_STATEMENT);
                updSt.setString(1, user.getName());
                updSt.setString(2, user.getSurname());
                updSt.setLong(3, id);
                modified = updSt.executeUpdate();
            } else {
                insSt = getConnection().prepareStatement(INSERT_USER_STATEMENT);
                insSt.setString(1, user.getName());
                insSt.setString(2, user.getSurname());
                insSt.setBoolean(3, "m".equalsIgnoreCase(user.getSex()));
                modified = insSt.executeUpdate();
                seqSt = getConnection().prepareStatement(USER_CURR_VALUE);
                ResultSet rs = seqSt.executeQuery();
                while (rs.next()) {
                    retID = rs.getString(1);
                }
            }
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + seqSt + "\" and \"" + (user.getId() != null ? updSt : insSt) + "\"");
            } else {
                getConnection().rollback();
                LOGGER.debug("DB has not been updated. -> rollback! Queries: \"" + seqSt + "\" and \"" + (user.getId() != null ? updSt : insSt) + "\"");
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
