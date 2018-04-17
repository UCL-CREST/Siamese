    public void excluirTopico(Integer idDisciplina) throws Exception {
        String sql = "DELETE from topico WHERE id_disciplina = ?";
        PreparedStatement stmt = null;
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idDisciplina);
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }
