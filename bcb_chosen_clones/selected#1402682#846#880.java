    public static boolean insereMidia(final Connection con, Midia mid, Autor aut, Descricao desc) {
        try {
            con.setAutoCommit(false);
            Statement smt = con.createStatement();
            if (aut.getCodAutor() == 0) {
                GeraID.gerarCodAutor(con, aut);
                smt.executeUpdate("INSERT INTO autor VALUES(" + aut.getCodAutor() + ",'" + aut.getNome() + "','" + aut.getEmail() + "')");
            }
            GeraID.gerarCodMidia(con, mid);
            GeraID.gerarCodDescricao(con, desc);
            String titulo = mid.getTitulo().replaceAll("['\"]", "");
            String coment = mid.getComentario().replaceAll("[']", "\"");
            String texto = desc.getTexto().replaceAll("[']", "\"");
            smt.executeUpdate("INSERT INTO descricao VALUES(" + desc.getCodDesc() + ",'" + texto + "')");
            smt.executeUpdate("INSERT INTO midia VALUES(" + mid.getCodigo() + ", '" + titulo + "', '" + coment + "','" + mid.getUrl() + "', '" + mid.getTipo() + "', " + desc.getCodDesc() + ")");
            smt.executeUpdate("INSERT INTO mid_aut VALUES(" + mid.getCodigo() + "," + aut.getCodAutor() + ")");
            con.commit();
            return (true);
        } catch (SQLException e) {
            try {
                JOptionPane.showMessageDialog(null, "Rolling back transaction", "MIDIA: Database error", JOptionPane.ERROR_MESSAGE);
                System.err.print(e.getMessage());
                con.rollback();
            } catch (SQLException e1) {
                System.err.print(e1.getSQLState());
            }
            return (false);
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e2) {
                System.err.print(e2.getMessage());
            }
        }
    }
