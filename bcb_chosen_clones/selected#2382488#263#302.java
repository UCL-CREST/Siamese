    public static boolean doExecuteBatchSQL(List<String> sql) {
        session = currentSession();
        Connection conn = session.connection();
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            Iterator iter = sql.iterator();
            while (iter.hasNext()) {
                String sqlstr = (String) iter.next();
                log("[SmsManager] doing sql:" + sqlstr);
                ps = conn.prepareStatement(sqlstr);
                ps.executeUpdate();
            }
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
