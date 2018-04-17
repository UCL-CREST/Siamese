    public void processSaveHolding(Holding holdingBean, AuthSession authSession) {
        if (authSession == null) {
            return;
        }
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "UPDATE WM_LIST_HOLDING " + "SET " + "   full_name_HOLDING=?, " + "   NAME_HOLDING=? " + "WHERE ID_HOLDING = ? and ID_HOLDING in ";
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
            int num = 1;
            ps.setString(num++, holdingBean.getName());
            ps.setString(num++, holdingBean.getShortName());
            RsetTools.setLong(ps, num++, holdingBean.getId());
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            processDeleteRelatedCompany(dbDyn, holdingBean, authSession);
            processInsertRelatedCompany(dbDyn, holdingBean, authSession);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save holding";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
