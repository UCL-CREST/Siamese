    public boolean update(int idJugador, jugador jugadorModificado) {
        int intResult = 0;
        String sql = "UPDATE jugador " + "SET apellidoPaterno = ?, apellidoMaterno = ?, nombres = ?, fechaNacimiento = ?, " + " pais = ?, rating = ?, sexo = ? " + " WHERE idJugador = " + idJugador;
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement(jugadorModificado);
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
