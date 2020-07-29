    public static boolean doExecuteSQL(String sql) {
        session = currentSession();
        Connection conn = session.connection();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            log("[SmsManager] sql:" + sql);
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        } finally {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeHibernateSession();
        }
    }
