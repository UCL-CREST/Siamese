    public void removeRoom(int thisRoom) {
        DBConnection con = null;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            String query = "DELETE FROM cafe_Chat_Category WHERE cafe_Chat_Category_id=? ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, thisRoom);
            ps.executeUpdate();
            query = "DELETE FROM cafe_Chatroom WHERE cafe_chatroom_category=? ";
            ps = con.prepareStatement(query);
            ps.setInt(1, thisRoom);
            ps.executeUpdate();
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
    }
