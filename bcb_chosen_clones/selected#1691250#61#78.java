    private void alterarCategoria(Categoria cat) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = C3P0Pool.getConnection();
            String sql = "UPDATE categoria SET nome_categoria = ? where id_categoria = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, cat.getNome());
            ps.setInt(2, cat.getCodigo());
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            close(conn, ps);
        }
    }
