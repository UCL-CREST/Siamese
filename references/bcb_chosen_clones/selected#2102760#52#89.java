    public static boolean update(Funcionario objFuncionario) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "update funcionario " + " set nome = ? , cpf = ? , telefone = ? , email = ?, senha = ?, login = ?, id_cargo = ?" + " where id_funcionario = ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, objFuncionario.getNome());
            pst.setString(2, objFuncionario.getCpf());
            pst.setString(3, objFuncionario.getTelefone());
            pst.setString(4, objFuncionario.getEmail());
            pst.setString(5, objFuncionario.getSenha());
            pst.setString(6, objFuncionario.getLogin());
            pst.setLong(7, (objFuncionario.getCargo()).getCodigo());
            pst.setLong(8, objFuncionario.getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                System.out.println("[FuncionarioDAO.update] Erro ao atualizar -> " + e1.getMessage());
            }
            System.out.println("[FuncionarioDAO.update] Erro ao atualizar -> " + e.getMessage());
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
