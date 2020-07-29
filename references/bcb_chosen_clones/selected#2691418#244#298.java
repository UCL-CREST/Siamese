    public Long createSite(Site site, List<String> hosts) {
        if (log.isDebugEnabled()) {
            log.debug("site: " + site);
            if (site != null) {
                log.debug("    language: " + site.getDefLanguage());
                log.debug("    country: " + site.getDefCountry());
                log.debug("    variant: " + site.getDefVariant());
                log.debug("    companyId: " + site.getCompanyId());
            }
        }
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_LIST_SITE");
            seq.setTableName("WM_PORTAL_LIST_SITE");
            seq.setColumnName("ID_SITE");
            Long siteId = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_PORTAL_LIST_SITE (" + "ID_SITE, ID_FIRM, DEF_LANGUAGE, DEF_COUNTRY, DEF_VARIANT, " + "NAME_SITE, ADMIN_EMAIL, IS_CSS_DYNAMIC, CSS_FILE, " + "IS_REGISTER_ALLOWED " + ")values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	?," + "	? " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            int num = 1;
            RsetTools.setLong(ps, num++, siteId);
            RsetTools.setLong(ps, num++, site.getCompanyId());
            ps.setString(num++, site.getDefLanguage());
            ps.setString(num++, site.getDefCountry());
            ps.setString(num++, site.getDefVariant());
            ps.setString(num++, site.getSiteName());
            ps.setString(num++, site.getAdminEmail());
            ps.setInt(num++, site.getCssDynamic() ? 1 : 0);
            ps.setString(num++, site.getCssFile());
            ps.setInt(num++, site.getRegisterAllowed() ? 1 : 0);
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            if (hosts != null) {
                for (String s : hosts) {
                    VirtualHost host = new VirtualHostBean(null, siteId, s);
                    InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(dbDyn, host);
                }
            }
            dbDyn.commit();
            return siteId;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new site";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
