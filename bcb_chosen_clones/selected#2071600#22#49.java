    public void alterar(Cliente cliente) throws Exception {
        Connection connection = criaConexao(false);
        String sql = "update cliente set nome = ?, sexo = ?, cod_cidade = ? where cod_cliente = ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSexo());
            stmt.setInt(3, cliente.getCidade().getCodCidade());
            stmt.setLong(4, cliente.getId());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                connection.rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de alterar dados de cliente no banco!");
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
