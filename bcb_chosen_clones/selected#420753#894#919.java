    public void updateRole(AuthSession authSession, RoleBean roleBean) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "update WM_AUTH_ACCESS_GROUP " + "set    NAME_ACCESS_GROUP=? " + "WHERE  ID_ACCESS_GROUP=? ";
            ps = dbDyn.prepareStatement(sql);
            ps.setString(1, roleBean.getName());
            ps.setLong(2, roleBean.getRoleId());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save role";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
