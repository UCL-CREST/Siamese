    public static boolean insereCapitulo(final Connection con, Capitulo cap, Autor aut, Descricao desc) {
        try {
            con.setAutoCommit(false);
            Statement smt = con.createStatement();
            if (aut.getCodAutor() == 0) {
                GeraID.gerarCodAutor(con, aut);
                smt.executeUpdate("INSERT INTO autor VALUES(" + aut.getCodAutor() + ",'" + aut.getNome() + "','" + aut.getEmail() + "')");
            }
            GeraID.gerarCodDescricao(con, desc);
            GeraID.gerarCodCapitulo(con, cap);
            String text = desc.getTexto().replaceAll("[']", "\"");
            String titulo = cap.getTitulo().replaceAll("['\"]", "");
            String coment = cap.getComentario().replaceAll("[']", "\"");
            smt.executeUpdate("INSERT INTO descricao VALUES(" + desc.getCodDesc() + ",'" + text + "')");
            smt.executeUpdate("INSERT INTO capitulo VALUES(" + cap.getCodigo() + ",'" + titulo + "','" + coment + "'," + desc.getCodDesc() + ")");
            smt.executeUpdate("INSERT INTO cap_aut VALUES(" + cap.getCodigo() + "," + aut.getCodAutor() + ")");
            con.commit();
            return (true);
        } catch (SQLException e) {
            try {
                JOptionPane.showMessageDialog(null, "Rolling back transaction", "CAPITULO: Database error", JOptionPane.ERROR_MESSAGE);
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
