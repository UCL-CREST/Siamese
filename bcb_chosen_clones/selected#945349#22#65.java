    @Override
    public void create(DisciplinaDTO disciplina) {
        try {
            this.criaConexao(false);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PostgresqlDisciplinaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "insert into Disciplina select nextval('sq_Disciplina') as id, ? as nome";
        PreparedStatement stmt = null;
        try {
            stmt = this.getConnection().prepareStatement(sql);
            stmt.setString(1, disciplina.getNome());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                this.getConnection().rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de inserir dados de Disciplina no banco!");
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
