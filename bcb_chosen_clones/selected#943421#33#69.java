    public void insertJobLog(String userId, String[] checkId, String checkType, String objType) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preStm = null;
        String sql = "insert into COFFICE_JOBLOG_CHECKAUTH (USER_ID,CHECK_ID,CHECK_TYPE,OBJ_TYPE) values (?,?,?,?)";
        String cleanSql = "delete from COFFICE_JOBLOG_CHECKAUTH where " + "user_id = '" + userId + "' and check_type = '" + checkType + "' and obj_type = '" + objType + "'";
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            connection.setAutoCommit(false);
            preStm = connection.prepareStatement(cleanSql);
            int dCount = preStm.executeUpdate();
            String sHaveIns = ",";
            preStm = connection.prepareStatement(sql);
            for (int j = 0; j < checkId.length; j++) {
                if (sHaveIns.indexOf("," + checkId[j] + ",") < 0) {
                    preStm.setInt(1, Integer.parseInt(userId));
                    preStm.setInt(2, Integer.parseInt(checkId[j]));
                    preStm.setInt(3, Integer.parseInt(checkType));
                    preStm.setInt(4, Integer.parseInt(objType));
                    preStm.executeUpdate();
                    sHaveIns += checkId[j] + ",";
                }
            }
            connection.commit();
        } catch (Exception ex) {
            log.debug((new Date().toString()) + " ������Ȩ��ʧ��! ");
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw e;
            }
            throw ex;
        } finally {
            close(null, null, preStm, connection, dbo);
        }
    }
