    private static boolean insereTutorial(final Connection con, final Tutorial tut, final Autor aut, final Descricao desc) {
        try {
            con.setAutoCommit(false);
            Statement smt = con.createStatement();
            if (aut.getCodAutor() == 0) {
                GeraID.gerarCodAutor(con, aut);
                smt.executeUpdate("INSERT INTO autor VALUES(" + aut.getCodAutor() + ",'" + aut.getNome() + "','" + aut.getEmail() + "')");
            }
            GeraID.gerarCodDescricao(con, desc);
            GeraID.gerarCodTutorial(con, tut);
            String titulo = tut.getTitulo().replaceAll("['\"]", "");
            String coment = tut.getComentario().replaceAll("[']", "\"");
            String texto = desc.getTexto().replaceAll("[']", "\"");
            smt.executeUpdate("INSERT INTO descricao VALUES(" + desc.getCodDesc() + ",'" + texto + "')");
            smt.executeUpdate("INSERT INTO tutorial VALUES(" + tut.getCodigo() + ",'" + titulo + "','" + coment + "'," + desc.getCodDesc() + ")");
            smt.executeUpdate("INSERT INTO tut_aut VALUES(" + tut.getCodigo() + "," + aut.getCodAutor() + ")");
            con.commit();
            return (true);
        } catch (SQLException e) {
            try {
                JOptionPane.showMessageDialog(null, "Rolling back transaction", "TUTORIAL: Database error", JOptionPane.ERROR_MESSAGE);
                System.out.print(e.getMessage());
                con.rollback();
            } catch (SQLException e1) {
                System.err.print(e1.getSQLState());
            }
            return (false);
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e2) {
                System.err.print(e2.getSQLState());
            }
        }
    }
