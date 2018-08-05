    public Long addRole(AuthSession authSession, RoleBean roleBean) {
        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_AUTH_ACCESS_GROUP");
            seq.setTableName("WM_AUTH_ACCESS_GROUP");
            seq.setColumnName("ID_ACCESS_GROUP");
            Long sequenceValue = dbDyn.getSequenceNextValue(seq);
            ps = dbDyn.prepareStatement("insert into WM_AUTH_ACCESS_GROUP " + "( ID_ACCESS_GROUP, NAME_ACCESS_GROUP ) values " + (dbDyn.getIsNeedUpdateBracket() ? "(" : "") + " ?, ? " + (dbDyn.getIsNeedUpdateBracket() ? ")" : ""));
            RsetTools.setLong(ps, 1, sequenceValue);
            ps.setString(2, roleBean.getName());
            int i1 = ps.executeUpdate();
            if (log.isDebugEnabled()) log.debug("Count of inserted records - " + i1);
            dbDyn.commit();
            return sequenceValue;
        } catch (Exception e) {
            try {
                if (dbDyn != null) dbDyn.rollback();
            } catch (Exception e001) {
            }
            String es = "Error add new role";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(dbDyn, ps);
            dbDyn = null;
            ps = null;
        }
    }
