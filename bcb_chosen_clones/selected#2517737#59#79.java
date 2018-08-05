    @Override
    public void alterar(Disciplina t) throws Exception {
        PreparedStatement stmt = null;
        String sql = "UPDATE disciplina SET nm_disciplina = ?, cod_disciplina = ? WHERE id_disciplina = ?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, t.getNomeDisciplina());
            stmt.setString(2, t.getCodDisciplina());
            stmt.setInt(3, t.getIdDisciplina());
            stmt.executeUpdate();
            conexao.commit();
            int id_disciplina = t.getIdDisciplina();
            excluirTopico(t.getIdDisciplina());
            for (Topico item : t.getTopicos()) {
                criarTopico(item, id_disciplina);
            }
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }
