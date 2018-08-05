    private void criarQuestaoMultiplaEscolha(QuestaoMultiplaEscolha q) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "INSERT INTO multipla_escolha (id_questao, texto, gabarito) VALUES (?,?,?)";
        try {
            for (Alternativa alternativa : q.getAlternativa()) {
                stmt = conexao.prepareStatement(sql);
                stmt.setInt(1, q.getIdQuestao());
                stmt.setString(2, alternativa.getTexto());
                stmt.setBoolean(3, alternativa.getGabarito());
                stmt.executeUpdate();
                conexao.commit();
            }
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }
