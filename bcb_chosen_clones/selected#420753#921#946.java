    public void deleteRole(AuthSession authSession, RoleBean roleBean) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            if (roleBean.getRoleId() == null) throw new IllegalArgumentException("role id is null");
            String sql = "delete from WM_AUTH_ACCESS_GROUP where ID_ACCESS_GROUP=? ";
            ps = dbDyn.prepareStatement(sql);
            RsetTools.setLong(ps, 1, roleBean.getRoleId());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of deleted records - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error delete role";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
