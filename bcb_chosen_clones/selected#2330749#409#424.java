    public void desistirCandidatura(Atividade atividade) throws SQLException {
        Connection conn = null;
        String insert = "delete from Atividade_has_recurso_humano where atividade_idatividade=" + atividade.getIdAtividade() + " and usuario_idusuario=" + atividade.getRecursoHumano().getIdUsuario();
        try {
            conn = connectionFactory.getConnection(true);
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            Integer result = stmt.executeUpdate(insert);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
