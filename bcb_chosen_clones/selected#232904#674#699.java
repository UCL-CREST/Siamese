    public boolean save(Object obj) {
        boolean bool = false;
        this.result = null;
        if (obj == null) return bool;
        Connection conn = null;
        try {
            conn = ConnectUtil.getConnect();
            conn.setAutoCommit(false);
            String sql = SqlUtil.getInsertSql(this.getCls());
            PreparedStatement ps = conn.prepareStatement(sql);
            setPsParams(ps, obj);
            ps.executeUpdate();
            ps.close();
            conn.commit();
            bool = true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
            }
            this.result = e.getMessage();
        } finally {
            this.closeConnectWithTransaction(conn);
        }
        return bool;
    }
