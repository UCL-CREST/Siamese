    public void excluirCliente(String cpf) {
        PreparedStatement pstmt = null;
        String sql = "delete from cliente where cpf = ?";
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, cpf);
            pstmt.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                throw new RuntimeException("Erro ao exclir ciente.", ex1);
            }
            throw new RuntimeException("Erro ao excluir cliente.", ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Ocorreu um erro no banco de dados.", ex);
            }
        }
    }
