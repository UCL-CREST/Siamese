    public static int deleteOrderStatusHis(String likePatten) {
        Connection conn = null;
        PreparedStatement psmt = null;
        StringBuffer SQL = new StringBuffer(200);
        int deleted = 0;
        SQL.append(" DELETE FROM JHF_ORDER_STATUS_HISTORY ").append(" WHERE   ORDER_ID LIKE  ? ");
        try {
            conn = JdbcConnectionPool.mainConnection();
            conn.setAutoCommit(false);
            conn.setReadOnly(false);
            psmt = conn.prepareStatement(SQL.toString());
            psmt.setString(1, "%" + likePatten + "%");
            deleted = psmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    System.out.println(" error when roll back !");
                }
            }
        } finally {
            try {
                if (null != psmt) {
                    psmt.close();
                    psmt = null;
                }
                if (null != conn) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                System.out.println(" error  when psmt close or conn close .");
            }
        }
        return deleted;
    }
