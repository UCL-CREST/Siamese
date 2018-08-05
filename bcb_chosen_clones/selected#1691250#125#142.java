    @Override
    public void delCategoria(Integer codigo) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = C3P0Pool.getConnection();
            String sql = "delete from categoria where id_categoria = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, codigo);
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            close(conn, ps);
        }
    }
