    public Long processAddHolding(Holding holdingBean, AuthSession authSession) {
        if (authSession == null) {
            return null;
        }
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_HOLDING");
            seq.setTableName("WM_LIST_HOLDING");
            seq.setColumnName("ID_HOLDING");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_LIST_HOLDING " + "( ID_HOLDING, full_name_HOLDING, NAME_HOLDING )" + "values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + " ?, ?, ? " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            int num = 1;
            RsetTools.setLong(ps, num++, sequenceValue);
            ps.setString(num++, holdingBean.getName());
            ps.setString(num++, holdingBean.getShortName());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            HoldingBean bean = new HoldingBean(holdingBean);
            bean.setId(sequenceValue);
            processInsertRelatedCompany(dbDyn, bean, authSession);
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new holding";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
