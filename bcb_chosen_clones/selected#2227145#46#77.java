    public static boolean update(Cargo cargo) {
        int result = 0;
        Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            String sql = "update cargo set nome = (?) where id_cargo= ?";
            pst = c.prepareStatement(sql);
            pst.setString(1, cargo.getNome());
            pst.setInt(2, cargo.getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[CargoDAO.update] Erro ao atualizar -> " + e.getMessage());
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
