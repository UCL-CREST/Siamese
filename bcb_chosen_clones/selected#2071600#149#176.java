    @Override
    public void incluir(Cliente cliente) throws Exception {
        Connection connection = criaConexao(false);
        String sql = "insert into cliente select nextval('sq_cliente') as cod_cliente, ? as nome,  ? as sexo, ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSexo());
            stmt.setInt(3, cliente.getCidade().getCodCidade());
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
