    public static boolean update(Departamento objDepartamento) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            String sql = "update departamento set nome = ?, sala = ?, telefone = ?, id_orgao = ? where id_departamento= ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, objDepartamento.getNome());
            pst.setString(2, objDepartamento.getSala());
            pst.setString(3, objDepartamento.getTelefone());
            pst.setLong(4, (objDepartamento.getOrgao()).getCodigo());
            pst.setInt(5, objDepartamento.getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[DepartamentoDAO.update] Erro ao atualizar -> " + e.getMessage());
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
