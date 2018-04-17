    public void registerSchema(String newSchemaName, String objectControlller, long boui, String expression, String schema) throws SQLException {
        Connection cndef = null;
        PreparedStatement pstm = null;
        try {
            cndef = this.getRepositoryConnection(p_ctx.getApplication(), "default", 2);
            String friendlyName = MessageLocalizer.getMessage("SCHEMA_CREATED_BY_OBJECT") + " [" + objectControlller + "] " + MessageLocalizer.getMessage("WITH_BOUI") + " [" + boui + "]";
            pstm = cndef.prepareStatement("DELETE FROM NGTDIC WHERE TABLENAME=? and objecttype='S'");
            pstm.setString(1, newSchemaName);
            pstm.executeUpdate();
            pstm.close();
            pstm = cndef.prepareStatement("INSERT INTO NGTDIC (SCHEMA,OBJECTNAME,OBJECTTYPE,TABLENAME, " + "FRIENDLYNAME, EXPRESSION) VALUES (" + "?,?,?,?,?,?)");
            pstm.setString(1, schema);
            pstm.setString(2, newSchemaName);
            pstm.setString(3, "S");
            pstm.setString(4, newSchemaName);
            pstm.setString(5, friendlyName);
            pstm.setString(6, expression);
            pstm.executeUpdate();
            pstm.close();
            cndef.commit();
        } catch (Exception e) {
            cndef.rollback();
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (Exception e) {
                }
            }
        }
    }
