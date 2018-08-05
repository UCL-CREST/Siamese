    public boolean actualizarEstadoDivision(division div) {
        int intResult = 0;
        String sql = "UPDATE divisionxTorneo " + " SET terminado = '1' " + " WHERE idDivisionxTorneo = " + div.getidDivision();
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            intResult = ps.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exe) {
                exe.printStackTrace();
            }
        } finally {
            conexionBD.close(ps);
            conexionBD.close(connection);
        }
        return (intResult > 0);
    }
