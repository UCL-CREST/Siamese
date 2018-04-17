    public static synchronized String getSequenceNumber(String SequenceName) {
        String result = "0";
        Connection conn = null;
        Statement ps = null;
        ResultSet rs = null;
        try {
            conn = TPCW_Database.getConnection();
            conn.setAutoCommit(false);
            String sql = "select num from sequence where name='" + SequenceName + "'";
            ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = ps.executeQuery(sql);
            long num = 0;
            while (rs.next()) {
                num = rs.getLong(1);
                result = new Long(num).toString();
            }
            num++;
            sql = "update sequence set num=" + num + " where name='" + SequenceName + "'";
            int res = ps.executeUpdate(sql);
            if (res == 1) {
                conn.commit();
            } else conn.rollback();
        } catch (Exception e) {
            System.out.println("Error Happens when trying to obtain the senquence number");
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return result;
    }
