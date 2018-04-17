    @Override
    public void incluir(Local local) throws Exception {
        Connection connection = criaConexao(false);
        String sql = "insert into local select nextval('local_idlocal_seq') as idlocal, ? as numlocal,  ? as nome, ? as idbairro";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, local.getNumeroLocal());
            stmt.setString(2, local.getNome());
            stmt.setInt(3, local.getBairro().getIdBairro());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                connection.rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de inserir dados de cliente no banco!");
            }
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
