    public void updatePortletName(PortletName portletNameBean) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            String sql = "update WM_PORTAL_PORTLET_NAME " + "set    TYPE=? " + "where  ID_SITE_CTX_TYPE=?";
            ps = dbDyn.prepareStatement(sql);
            ps.setString(1, portletNameBean.getPortletName());
            RsetTools.setLong(ps, 2, portletNameBean.getPortletId());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of updated record - " + i1);
            dbDyn.commit();
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error save portlet name";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
