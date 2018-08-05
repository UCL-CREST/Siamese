    private static void salvarArtista(Artista artista) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = C3P0Pool.getConnection();
            String sql = "insert into artista VALUES (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, artista.getNumeroInscricao());
            ps.setString(2, artista.getNome());
            ps.setBoolean(3, artista.isSexo());
            ps.setString(4, artista.getEmail());
            ps.setString(5, artista.getObs());
            ps.setString(6, artista.getTelefone());
            ps.setNull(7, Types.INTEGER);
            ps.executeUpdate();
            salvarEndereco(conn, ps, artista);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            close(conn, ps);
        }
    }
