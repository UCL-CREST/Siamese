    public boolean crear() {
        int result = 0;
        String sql = "insert into partida" + "(torneo_idTorneo, jugador_idJugadorNegras, jugador_idJugadorBlancas, registrado, fecha," + " movs, resultado, nombreBlancas, nombreNegras, eloBlancas, eloNegras, idApertura)" + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            populatePreparedStatement(unaPartida);
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
