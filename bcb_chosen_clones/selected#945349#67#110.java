    @Override
    public void remove(int disciplinaId) {
        try {
            this.criaConexao(false);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "delete from Disciplina where id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = this.getConnection().prepareStatement(sql);
            stmt.setInt(1, disciplinaId);
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                this.getConnection().rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de remover dados de Revendedor no banco!");
            }
            this.getConnection().commit();
        } catch (SQLException e) {
            try {
                this.getConnection().rollback();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                throw e;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                stmt.close();
                this.fechaConexao();
            } catch (SQLException e) {
                try {
                    throw e;
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
