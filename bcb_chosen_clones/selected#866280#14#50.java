    @Override
    public void incluir(Casa_festas casa_festas) throws Exception {
        Connection connection = criaConexao(false);
        String sql = "insert into casa_festas ? as idlocal, ? as area, ? as realiza_cerimonia, ? as tipo_principal, ? as idgrupo;";
        String sql2 = "SELECT MAX(idlocal) FROM Local";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt2 = connection.prepareStatement(sql2);
            rs = stmt2.executeQuery();
            stmt.setInt(1, rs.getInt("max"));
            stmt.setDouble(2, casa_festas.getArea());
            stmt.setBoolean(3, casa_festas.getRealizaCerimonias());
            stmt.setBoolean(4, casa_festas.getTipoPrincipal());
            stmt.setInt(5, casa_festas.getIdGrupo());
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
                stmt2.close();
                rs.close();
                this.fechaConexao();
            } catch (SQLException e) {
                throw e;
            }
        }
    }
