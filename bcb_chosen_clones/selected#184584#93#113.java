    public void removerDisciplina(Disciplina disciplina) throws ClassNotFoundException, SQLException {
        this.criaConexao(false);
        String sql = "DELETE FROM \"Disciplina\"    " + "      WHERE ID_Disciplina =  ? )";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, disciplina.getId());
            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            try {
                stmt.close();
                this.fechaConexao();
            } catch (SQLException e) {
                throw e;
            }
        }
    }
