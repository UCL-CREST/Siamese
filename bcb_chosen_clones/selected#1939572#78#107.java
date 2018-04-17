    public Long addPortletName(PortletNameBean portletNameBean) {
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_PORTLET_NAME");
            seq.setTableName("WM_PORTAL_PORTLET_NAME");
            seq.setColumnName("ID_SITE_CTX_TYPE");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_PORTAL_PORTLET_NAME " + "( ID_SITE_CTX_TYPE, TYPE ) " + "values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + " ?, ?" + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            RsetTools.setLong(ps, 1, sequenceValue);
            ps.setString(2, portletNameBean.getPortletName());
            ps.executeUpdate();
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new portlet name ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
