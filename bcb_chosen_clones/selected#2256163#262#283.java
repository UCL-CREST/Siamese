    public boolean actualizarNumeroRondas(int idDivision, int numFechas) {
        int intResult = 0;
        String sql = "UPDATE divisionxTorneo " + " SET numFechas = " + numFechas + " WHERE idDivisionxTorneo = " + idDivision;
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
