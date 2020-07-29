    public int deleteRecord(Publisher publisher, MmdQueryCriteria criteria) throws Exception {
        int nRows = 0;
        if (!publisher.getIsAdministrator()) {
            throw new ImsServiceException("DeleteRecordsRequest: not authorized.");
        }
        PreparedStatement st = null;
        ManagedConnection mc = returnConnection();
        Connection con = mc.getJdbcConnection();
        DatabaseMetaData dmt = con.getMetaData();
        String database = dmt.getDatabaseProductName().toLowerCase();
        boolean autoCommit = con.getAutoCommit();
        con.setAutoCommit(false);
        try {
            StringBuilder sbWhere = new StringBuilder();
            Map<String, Object> args = criteria.appendWherePhrase(null, sbWhere, publisher);
            StringBuilder sbData = new StringBuilder();
            if (database.contains("mysql")) {
                sbData.append(" DELETE ").append(getResourceDataTableName()).append(" FROM ").append(getResourceDataTableName());
                sbData.append(" LEFT JOIN ").append(getResourceTableName());
                sbData.append(" ON ").append(getResourceDataTableName()).append(".ID=").append(getResourceTableName()).append(".ID WHERE ").append(getResourceTableName()).append(".ID in (");
                sbData.append(" SELECT ID FROM ").append(getResourceTableName()).append(" ");
                if (sbWhere.length() > 0) {
                    sbData.append(" WHERE ").append(sbWhere.toString());
                }
                sbData.append(")");
            } else {
                sbData.append(" DELETE FROM ").append(getResourceDataTableName());
                sbData.append(" WHERE ").append(getResourceDataTableName()).append(".ID in (");
                sbData.append(" SELECT ID FROM ").append(getResourceTableName()).append(" ");
                if (sbWhere.length() > 0) {
                    sbData.append(" WHERE ").append(sbWhere.toString());
                }
                sbData.append(")");
            }
            st = con.prepareStatement(sbData.toString());
            criteria.applyArgs(st, 1, args);
            logExpression(sbData.toString());
            st.executeUpdate();
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("DELETE FROM ").append(getResourceTableName()).append(" ");
            if (sbWhere.length() > 0) {
                sbSql.append(" WHERE ").append(sbWhere.toString());
            }
            closeStatement(st);
            st = con.prepareStatement(sbSql.toString());
            criteria.applyArgs(st, 1, args);
            logExpression(sbSql.toString());
            nRows = st.executeUpdate();
            con.commit();
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            closeStatement(st);
            con.setAutoCommit(autoCommit);
        }
        return nRows;
    }
