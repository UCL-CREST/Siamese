    public static boolean insert(final Departamento ObjDepartamento) {
        int result = 0;
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "insert into departamento " + "(nome, sala, telefone, id_orgao)" + " values (?, ?, ?, ?)";
            pst = c.prepareStatement(sql);
            pst.setString(1, ObjDepartamento.getNome());
            pst.setString(2, ObjDepartamento.getSala());
            pst.setString(3, ObjDepartamento.getTelefone());
            pst.setInt(4, (ObjDepartamento.getOrgao()).getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[DepartamentoDAO.insert] Erro ao inserir -> " + e.getMessage());
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
