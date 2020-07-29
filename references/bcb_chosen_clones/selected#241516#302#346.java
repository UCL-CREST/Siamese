    public void processDeleteCompany(Company companyBean, AuthSession authSession) {
        if (authSession == null) {
            return;
        }
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            if (companyBean.getId() == null) throw new IllegalArgumentException("companyId is null");
            String sql = "update WM_LIST_COMPANY set is_deleted = 1 " + "where  ID_FIRM = ? and ID_FIRM in ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql += "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement(sql);
            RsetTools.setLong(ps, 1, companyBean.getId());
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(2, authSession.getUserLogin());
                    break;
            }
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of deleted records - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error delete company";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
