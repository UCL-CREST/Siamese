    public void insertUser(final User user) throws IOException {
        try {
            Connection conn = null;
            boolean autoCommit = false;
            try {
                conn = pool.getConnection();
                autoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                final PreparedStatement insertUser = conn.prepareStatement("insert into users (userId, mainRoleId) values (?,?)");
                log.finest("userId= " + user.getUserId());
                insertUser.setString(1, user.getUserId());
                log.finest("mainRole= " + user.getMainRole().getId());
                insertUser.setInt(2, user.getMainRole().getId());
                insertUser.executeUpdate();
                final PreparedStatement insertRoles = conn.prepareStatement("insert into userRoles (userId, roleId) values (?,?)");
                for (final Role role : user.getRoles()) {
                    insertRoles.setString(1, user.getUserId());
                    insertRoles.setInt(2, role.getId());
                    insertRoles.executeUpdate();
                }
                conn.commit();
            } catch (Throwable t) {
                if (conn != null) conn.rollback();
                log.log(Level.SEVERE, t.toString(), t);
                throw new SQLException(t.toString());
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(autoCommit);
                    conn.close();
                }
            }
        } catch (final SQLException sqle) {
            log.log(Level.SEVERE, sqle.toString(), sqle);
            throw new IOException(sqle.toString());
        }
    }
