    public static void updatePicInfo(Connection conn, int nr, int lock, DBPicInfo picInfo) throws SQLException {
        String sql = "";
        PreparedStatement pstmt = null;
        try {
            if (!picInfo.getName().equals("")) {
                sql = "update DBPic set name=? where bnr=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, picInfo.getName());
                pstmt.setInt(2, nr);
                pstmt.executeUpdate();
            }
            if (picInfo.getRate() != 0) {
                sql = "update DBPic set rate=? where bnr=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, picInfo.getRate());
                pstmt.setInt(2, nr);
                pstmt.executeUpdate();
            }
            sql = "update DBThumb set thumb_lock=? where bnr=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lock);
            pstmt.setInt(2, nr);
            pstmt.executeUpdate();
            if (picInfo.getCategories() != null) {
                sql = "delete from Zuordnen where bnr=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nr);
                pstmt.executeUpdate();
                DaoUpdate.insertPicInCategories(conn, nr, picInfo.getCategories());
            }
            if (picInfo.getKeywords() != null) {
                sql = "delete from Haben where bnr=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, nr);
                pstmt.executeUpdate();
                DaoUpdate.insertPicInKeywords(conn, nr, picInfo.getKeywords());
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            pstmt.close();
        }
    }
