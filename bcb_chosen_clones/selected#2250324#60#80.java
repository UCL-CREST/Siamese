    public void removerTopicos(Topicos topicos) throws ClassNotFoundException, SQLException {
        this.criaConexao(false);
        String sql = "DELETE FROM \"Topicos\"    " + "      WHERE \"id_Topicos\" =  ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, topicos.getIdTopicos());
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
