    public int saveRoom(String name, String icon, String stringid) {
        DBConnection con = null;
        int categoryId = -1;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            String query = "INSERT INTO cafe_Chat_Category " + "(cafe_Chat_Category_pid,cafe_Chat_Category_name, cafe_Chat_Category_icon) " + "VALUES (null,?,?) ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, icon);
            ps.executeUpdate();
            query = "SELECT cafe_Chat_Category_id FROM cafe_Chat_Category " + "WHERE cafe_Chat_Category_name=? ";
            ps = con.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                query = "INSERT INTO cafe_Chatroom (cafe_chatroom_category, cafe_chatroom_name, cafe_chatroom_stringid) " + "VALUES (?,?,?) ";
                ps = con.prepareStatement(query);
                ps.setInt(1, rs.getInt("cafe_Chat_Category_id"));
                categoryId = rs.getInt("cafe_Chat_Category_id");
                ps.setString(2, name);
                ps.setString(3, stringid);
                ps.executeUpdate();
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException sqle) {
            }
        } finally {
            if (con != null) con.release();
        }
        return categoryId;
    }
