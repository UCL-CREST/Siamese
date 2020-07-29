    public int addLocationInfo(int id, double lattitude, double longitude) {
        int ret = 0;
        Connection conn = null;
        PreparedStatement psmt = null;
        try {
            String sql = "insert into kddb.location_info (user_id, latitude, longitude) values(?, ?, ?)";
            conn = getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, id);
            psmt.setDouble(2, lattitude);
            psmt.setDouble(3, longitude);
            ret = psmt.executeUpdate();
            if (ret == 1) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            log.error("[addLocationInfo]", ex);
        } finally {
            endProsess(conn, psmt, null, null);
        }
        return ret;
    }
