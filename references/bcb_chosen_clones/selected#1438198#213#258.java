    public void processDeleteHolding(Holding holdingBean, AuthSession authSession) {
        if (authSession == null) {
            return;
        }
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            if (holdingBean.getId() == null) throw new IllegalArgumentException("holdingId is null");
            processDeleteRelatedCompany(dbDyn, holdingBean, authSession);
            String sql = "delete from WM_LIST_HOLDING " + "where  ID_HOLDING=? and ID_HOLDING in ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedHoldingId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql += "(select z1.ID_ROAD from v$_read_list_road z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement(sql);
            RsetTools.setLong(ps, 1, holdingBean.getId());
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
            String es = "Error delete holding";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
