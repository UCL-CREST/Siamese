    @Override
    public void update(DisciplinaDTO disciplina) {
        try {
            this.criaConexao(false);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOG.debug("Criou a conex�o!");
        String sql = "update Disciplina set nome = ? where id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = this.getConnection().prepareStatement(sql);
            LOG.debug("PreparedStatement criado com sucesso!");
            stmt.setString(1, disciplina.getNome());
            stmt.setInt(2, disciplina.getId());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                this.getConnection().rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de alterar dados de Revendedor no banco!");
            }
            LOG.debug("Confirmando as altera��es no banco.");
            this.getConnection().commit();
        } catch (SQLException e) {
            LOG.debug("Desfazendo as altera��es no banco.");
            try {
                this.getConnection().rollback();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            LOG.debug("Lan�ando a exce��o da camada de persist�ncia.");
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
