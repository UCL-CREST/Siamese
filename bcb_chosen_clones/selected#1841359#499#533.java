    public void doUpdateByLoginID() throws Exception {
        if (!isValidate()) {
            throw new CesSystemException("User_session.doUpdateByLoginID(): Illegal data values for update");
        }
        Connection con = null;
        PreparedStatement ps = null;
        String strQuery = "UPDATE " + Common.USER_SESSION_TABLE + " SET " + "session_id = ?, user_id = ?, begin_date = ? , " + "ip_address = ?, mac_no = ? " + "WHERE  login_id= ?";
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        try {
            con = dbo.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(strQuery);
            ps.setString(1, this.sessionID);
            ps.setInt(2, this.user.getUserID());
            ps.setTimestamp(3, this.beginDate);
            ps.setString(4, this.ipAddress);
            ps.setString(5, this.macNO);
            ps.setString(6, this.loginID);
            int resultCount = ps.executeUpdate();
            if (resultCount != 1) {
                con.rollback();
                throw new CesSystemException("User_session.doUpdateByLoginID(): ERROR updating data in T_SYS_USER_SESSION!! " + "resultCount = " + resultCount);
            }
            con.commit();
        } catch (SQLException se) {
            if (con != null) {
                con.rollback();
            }
            throw new CesSystemException("User_session.doUpdateByLoginID(): SQLException while updating user_session; " + "session_id = " + this.sessionID + " :\n\t" + se);
        } finally {
            con.setAutoCommit(true);
            closePreparedStatement(ps);
            closeConnection(dbo);
        }
    }
