    public boolean update(int idTorneo, torneo torneoModificado) {
        int intResult = 0;
        String sql = "UPDATE torneo " + "SET nombreTorneo = ?, ciudad = ?, fechaInicio = ?, fechaFinal = ?, " + " organizador = ? " + " WHERE idTorneo = " + idTorneo;
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement2(torneoModificado);
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
