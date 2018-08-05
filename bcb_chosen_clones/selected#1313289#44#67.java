    public Leilao insertLeilao(Leilao leilao) throws SQLException {
        Connection conn = null;
        String insert = "insert into Leilao (idleilao, atividade_idatividade, datainicio, datafim) " + "values " + "(nextval('seq_leilao'), " + leilao.getAtividade().getIdAtividade() + ", '" + leilao.getDataInicio() + "', '" + leilao.getDataFim() + "')";
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            if (result == 1) {
                String sqlSelect = "select last_value from seq_leilao";
                ResultSet rs = stmt.executeQuery(sqlSelect);
                while (rs.next()) {
                    leilao.setIdLeilao(rs.getInt("last_value"));
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
        return null;
    }
