    public static boolean insereLicao(final Connection con, Licao lic, Autor aut, Descricao desc) {
        try {
            con.setAutoCommit(false);
            Statement smt = con.createStatement();
            if (aut.getCodAutor() == 0) {
                GeraID.gerarCodAutor(con, aut);
                smt.executeUpdate("INSERT INTO autor VALUES(" + aut.getCodAutor() + ",'" + aut.getNome() + "','" + aut.getEmail() + "')");
            }
            GeraID.gerarCodDescricao(con, desc);
            GeraID.gerarCodLicao(con, lic);
            String titulo = lic.getTitulo().replaceAll("['\"]", "");
            String coment = lic.getComentario().replaceAll("[']", "\"");
            String texto = desc.getTexto().replaceAll("[']", "\"");
            smt.executeUpdate("INSERT INTO descricao VALUES(" + desc.getCodDesc() + ",'" + texto + "')");
            smt.executeUpdate("INSERT INTO licao VALUES(" + lic.getCodigo() + ",'" + titulo + "','" + coment + "'," + desc.getCodDesc() + ")");
            smt.executeUpdate("INSERT INTO lic_aut VALUES(" + lic.getCodigo() + "," + aut.getCodAutor() + ")");
            con.commit();
            return (true);
        } catch (SQLException e) {
            try {
                JOptionPane.showMessageDialog(null, "Rolling back transaction", "LICAO: Database error", JOptionPane.ERROR_MESSAGE);
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
