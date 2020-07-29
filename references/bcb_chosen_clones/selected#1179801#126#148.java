    public boolean update(int idPartida, partida partidaModificada) {
        int intResult = 0;
        String sql = "UPDATE partida " + "SET torneo_idTorneo = ?, " + " jugador_idJugadorNegras = ?, jugador_idJugadorBlancas = ?, " + " fecha = ?, " + " resultado = ?, " + " nombreBlancas = ?, nombreNegras = ?, eloBlancas = ?, eloNegras = ?, idApertura = ? " + " WHERE idPartida = " + idPartida;
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement2(partidaModificada);
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
