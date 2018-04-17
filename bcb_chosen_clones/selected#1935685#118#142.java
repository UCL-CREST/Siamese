    public void modify(String strName, String strNewPass) {
        String str = "update jb_user set V_PASSWORD =? where V_USERNAME =?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbForumFactory.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(str);
            pstmt.setString(1, SecurityUtil.md5ByHex(strNewPass));
            pstmt.setString(2, strName);
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e1) {
            }
        } finally {
            try {
                DbForumFactory.closeDB(null, pstmt, null, con);
            } catch (Exception e) {
            }
        }
    }
