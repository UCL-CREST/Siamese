    public int updateuser(User u) {
        int i = 0;
        Connection conn = null;
        PreparedStatement pm = null;
        try {
            conn = Pool.getConnection();
            conn.setAutoCommit(false);
            pm = conn.prepareStatement("update user set username=?,passwd=?,existstate=?,management=? where userid=?");
            pm.setString(1, u.getUsername());
            pm.setString(2, u.getPasswd());
            pm.setInt(3, u.getExiststate());
            pm.setInt(4, u.getManagement());
            pm.setString(5, u.getUserid());
            i = pm.executeUpdate();
            conn.commit();
            Pool.close(pm);
            Pool.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            Pool.close(pm);
            Pool.close(conn);
        } finally {
            Pool.close(pm);
            Pool.close(conn);
        }
        return i;
    }
