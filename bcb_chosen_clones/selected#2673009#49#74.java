    public int setData(String mysql) {
        int mycount = 0;
        try {
            conn.setAutoCommit(false);
            mycount = 0;
            stmt = conn.createStatement();
            mycount = stmt.executeUpdate(mysql);
        } catch (Exception e) {
            mycount = -1;
        } finally {
            if (mycount > 0) {
                try {
                    conn.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return mycount;
    }
