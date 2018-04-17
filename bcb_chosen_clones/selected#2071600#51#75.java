    public void excluir(Cliente cliente) throws Exception {
        Connection connection = criaConexao(false);
        String sql = "delete from cliente where cod_cliente = ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, cliente.getId());
            int retorno = stmt.executeUpdate();
            if (retorno == 0) {
                connection.rollback();
                throw new SQLException("Ocorreu um erro inesperado no momento de remover dados de cliente no banco!");
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
