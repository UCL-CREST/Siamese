    @Override
    public void delArtista(Integer numeroInscricao) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = C3P0Pool.getConnection();
            String sql = "delete from artista where numeroinscricao = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroInscricao);
            ps.executeUpdate();
            delEndereco(conn, ps, numeroInscricao);
            delObras(conn, ps, numeroInscricao);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            close(conn, ps);
        }
    }
