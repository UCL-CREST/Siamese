    public void savaUserPerm(String userid, Collection user_perm_collect) throws DAOException, SQLException {
        ConnectionProvider cp = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PrivilegeFactory factory = PrivilegeFactory.getInstance();
        Operation op = factory.createOperation();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(DEL_USER_PERM);
            pstmt.setString(1, userid);
            pstmt.executeUpdate();
            if ((user_perm_collect == null) || (user_perm_collect.size() <= 0)) {
                return;
            } else {
                conn.setAutoCommit(false);
                pstmt = conn.prepareStatement(ADD_USER_PERM);
                Iterator user_perm_ir = user_perm_collect.iterator();
                while (user_perm_ir.hasNext()) {
                    UserPermission userPerm = (UserPermission) user_perm_ir.next();
                    pstmt.setString(1, String.valueOf(userPerm.getUser_id()));
                    pstmt.setString(2, String.valueOf(userPerm.getResource_id()));
                    pstmt.setString(3, String.valueOf(userPerm.getResop_id()));
                    pstmt.executeUpdate();
                }
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw new DAOException();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }
