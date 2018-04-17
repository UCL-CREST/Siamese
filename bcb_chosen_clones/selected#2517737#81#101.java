    @Override
    public void excluir(Disciplina t) throws Exception {
        PreparedStatement stmt = null;
        String sql = "DELETE from disciplina where id_disciplina = ?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, t.getIdDisciplina());
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            try {
                stmt.close();
                conexao.close();
            } catch (SQLException e) {
                throw e;
            }
        }
    }
