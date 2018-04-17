    private void salvarCategoria(Categoria cat) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = C3P0Pool.getConnection();
            String sql = "insert into categoria VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setNull(1, Types.INTEGER);
            ps.setString(2, cat.getNome());
            ps.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            close(conn, ps);
        }
    }
