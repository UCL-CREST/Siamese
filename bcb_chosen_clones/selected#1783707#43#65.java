    public int executar(String sql, Boolean retornaAutoIncremento) {
        int autoIncremento = 0;
        try {
            for (Connection conn : conexoes) {
                stm = conn.createStatement();
                stm.executeUpdate(sql);
            }
            for (Connection conn : conexoes) {
                conn.commit();
            }
        } catch (Exception ex) {
            try {
                for (Connection conn : conexoes) {
                    conn.rollback();
                }
                return 0;
            } catch (SQLException Sqlex) {
                Logger.getLogger(Persistencia.class.getName()).log(Level.SEVERE, null, Sqlex);
            }
        }
        if (retornaAutoIncremento) autoIncremento = getUltimoIdentificador();
        return autoIncremento;
    }
