    public void alterarQuestaoDiscursiva(QuestaoDiscursiva q) throws SQLException {
        PreparedStatement stmt = null;
        String sql = "UPDATE discursiva SET  gabarito=? WHERE id_questao=?";
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, q.getGabarito());
            stmt.setInt(2, q.getIdQuestao());
            stmt.executeUpdate();
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        }
    }
