    @Override
    public RoleItem addUserRole(RoleItem role, long userId) throws DatabaseException {
        if (role == null) throw new NullPointerException("role");
        if (role.getName() == null || "".equals(role.getName())) throw new NullPointerException("role.getName()");
        if (hasRole(role.getName(), userId)) {
            return new RoleItem(role.getName(), "", "exist");
        }
        RoleItem defaultRole = new RoleItem(role.getName(), "", "exist");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        String retID = "exist";
        String roleDesc = "";
        PreparedStatement seqSt = null, roleDescSt = null;
        try {
            int modified = 0;
            PreparedStatement insSt = getConnection().prepareStatement(INSERT_USER_IN_ROLE_STATEMENT);
            insSt.setLong(1, userId);
            insSt.setString(2, role.getName());
            modified = insSt.executeUpdate();
            seqSt = getConnection().prepareStatement(USER_ROLE_VALUE);
            ResultSet rs = seqSt.executeQuery();
            while (rs.next()) {
                retID = rs.getString(1);
            }
            roleDescSt = getConnection().prepareStatement(SELECT_ROLE_DESCRIPTION);
            roleDescSt.setString(1, role.getName());
            ResultSet rs2 = roleDescSt.executeQuery();
            while (rs2.next()) {
                roleDesc = rs2.getString(1);
            }
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + seqSt + "\" and \"" + roleDescSt + "\"");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + seqSt + "\" and \"" + roleDescSt + "\"");
                retID = "error";
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            retID = "error";
        } finally {
            closeConnection();
        }
        defaultRole.setId(retID);
        defaultRole.setDescription(roleDesc);
        return defaultRole;
    }
