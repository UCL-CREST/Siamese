    public void processSaveCompany(Company companyBean, AuthSession authSession) {
        if (authSession == null) {
            return;
        }
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "UPDATE WM_LIST_COMPANY " + "SET " + "	full_name = ?, " + "	short_name = ?, " + "	address = ?, " + "	telefon_buh = ?, " + "	telefon_chief = ?, " + "	chief = ?, " + "	buh = ?, " + "	fax = ?, " + "	email = ?, " + "	icq = ?, " + "	short_client_info = ?, " + "	url = ?, " + "	short_info = ? " + "WHERE ID_FIRM = ? and ID_FIRM in ";
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
            int num = 1;
            ps.setString(num++, companyBean.getName());
            ps.setString(num++, companyBean.getShortName());
            ps.setString(num++, companyBean.getAddress());
            ps.setString(num++, "");
            ps.setString(num++, "");
            ps.setString(num++, companyBean.getCeo());
            ps.setString(num++, companyBean.getCfo());
            ps.setString(num++, "");
            ps.setString(num++, "");
            RsetTools.setLong(ps, num++, null);
            ps.setString(num++, "");
            ps.setString(num++, companyBean.getWebsite());
            ps.setString(num++, companyBean.getInfo());
            RsetTools.setLong(ps, num++, companyBean.getId());
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(num++, authSession.getUserLogin());
                    break;
            }
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save company";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
