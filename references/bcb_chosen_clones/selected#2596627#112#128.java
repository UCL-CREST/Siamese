    public void alterarQuestaoMultiplaEscolha(QuestaoMultiplaEscolha q) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "UPDATE multipla_escolha SET texto=?, gabarito=? WHERE id_questao=?";
        try {
            for (Alternativa alternativa : q.getAlternativa()) {
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, alternativa.getTexto());
                stmt.setBoolean(2, alternativa.getGabarito());
                stmt.setInt(3, q.getIdQuestao());
                stmt.executeUpdate();
                conexao.commit();
            }
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }
