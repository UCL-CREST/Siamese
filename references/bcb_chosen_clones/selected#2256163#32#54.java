    public boolean crear() {
        int result = 0;
        String sql = "insert into divisionxTorneo" + "(torneo_idTorneo, tipoTorneo_idTipoTorneo, nombreDivision, descripcion, numJugadores, numFechas, terminado, tipoDesempate, rondaActual, ptosxbye)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement();
            result = ps.executeUpdate();
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
        return (result > 0);
    }
