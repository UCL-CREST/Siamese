    public void addUser(String strUserName, String strPass) {
        String datetime = Function.getSysTime().toString();
        String time = datetime.substring(0, 4) + datetime.substring(5, 7) + datetime.substring(8, 10) + datetime.substring(11, 13) + datetime.substring(14, 16) + datetime.substring(17, 19) + datetime.substring(20, 22) + "0";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DbForumFactory.getConnection();
            con.setAutoCommit(false);
            int userID = DbSequenceManager.nextID(DbSequenceManager.USER);
            pstmt = con.prepareStatement(INSERT_USER);
            pstmt.setString(1, strUserName);
            pstmt.setString(2, SecurityUtil.md5ByHex(strPass));
            pstmt.setString(3, time);
            pstmt.setString(4, "");
            pstmt.setString(5, "");
            pstmt.setString(6, "");
            pstmt.setString(7, "");
            pstmt.setInt(8, userID);
            pstmt.executeUpdate();
            pstmt.clearParameters();
            pstmt = con.prepareStatement(INSERT_USERPROPS);
            pstmt.setString(1, "");
            pstmt.setString(2, "");
            pstmt.setString(3, "");
            pstmt.setInt(4, 0);
            pstmt.setString(5, "");
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.setString(8, "");
            pstmt.setString(9, "");
            pstmt.setString(10, "");
            pstmt.setInt(11, 0);
            pstmt.setInt(12, 0);
            pstmt.setInt(13, 0);
            pstmt.setInt(14, 0);
            pstmt.setString(15, "");
            pstmt.setString(16, "");
            pstmt.setString(17, "");
            pstmt.setString(18, "");
            pstmt.setString(19, "");
            pstmt.setString(20, "");
            pstmt.setString(21, "");
            pstmt.setString(22, "");
            pstmt.setString(23, "");
            pstmt.setInt(24, 0);
            pstmt.setInt(25, 0);
            pstmt.setInt(26, userID);
            pstmt.executeUpdate();
            pstmt.clearParameters();
            pstmt = con.prepareStatement(INSTER_USERGROUP);
            pstmt.setInt(1, 4);
            pstmt.setInt(2, userID);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
            }
            log.error("insert user Error: " + e.toString());
        } finally {
            DbForumFactory.closeDB(null, pstmt, null, con);
        }
    }
