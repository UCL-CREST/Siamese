    public void doDelete(Role role) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strEdQuery = "SELECT authority_id from " + Common.ROLE_AUTHORITY_TABLE + " WHERE role_id = ?";
        String strQuery = "DELETE FROM " + Common.ROLE_AUTHORITY_TABLE + " WHERE role_id = ?";
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        try {
            try {
                con = dbo.getConnection();
                con.setAutoCommit(false);
                ps = con.prepareStatement(strEdQuery);
                ps.setInt(1, role.getRoleID());
                result = ps.executeQuery();
                while (result.next()) {
                    int authID = result.getInt("authority_id");
                    Authority auth = new Authority(authID);
                    auth.load();
                    AssignLog aLog = new AssignLog();
                    aLog.setLogNO(aLog.getNewLogNo());
                    aLog.setID(authID);
                    aLog.setName(auth.getAuthorityName());
                    aLog.setAssignType(AssignLog.ASSIGN_AUTHORITY);
                    aLog.setAssignDate(Translate.getSysTime());
                    aLog.setAssignFrom(this.provider.getUserID());
                    aLog.setAssignFromName(this.provider.getUserName());
                    aLog.setAssignTo(role.getRoleID());
                    aLog.setAssignToName(role.getRoleName());
                    aLog.setReceiverType(AssignLog.RECEIVER_ROLE);
                    aLog.setInfo("ɾ���ɫ�е�Ȩ��");
                    aLog.setPath("");
                    aLog.setPathName("");
                    aLog.doUpdateOrNew(con);
                }
                ps = con.prepareStatement(strQuery);
                ps.setInt(1, role.getRoleID());
                int resultCount = ps.executeUpdate();
                if (resultCount < 0) {
                    con.rollback();
                    throw new CesSystemException("RoleAuthority.doDelete(role): ERROR deleting data in T_SYS_ROLE_AUTHORITY!! " + "resultCount = " + resultCount);
                }
                con.commit();
            } catch (SQLException se) {
                con.rollback();
                throw new CesSystemException("RoleAuthority.doDelete(role): SQLException while deleting Role_authority; " + " role_id = " + role.getRoleID() + " :\n\t" + se);
            } finally {
                con.setAutoCommit(true);
                close(dbo, ps, result);
            }
        } catch (SQLException sqle) {
            throw new CesSystemException("RoleAuthority.doDelete(role): SQLException while committing or rollback");
        }
    }
