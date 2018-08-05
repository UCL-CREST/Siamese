    private void transferir() {
        PreparedStatement ps = null;
        StringBuilder sql = new StringBuilder();
        boolean problema = false;
        String idFk = "";
        try {
            for (String tabela : tabelas) {
                idFk = mapaTabelas.get(tabela);
                sql.delete(0, sql.length());
                sql.append("UPDATE ");
                sql.append(tabela);
                sql.append(" SET");
                sql.append(" CODEMP" + idFk + "=?,");
                sql.append(" CODFILIAL" + idFk + "=?,");
                sql.append(" CODPLAN=?");
                sql.append(" WHERE");
                sql.append(" CODEMP" + idFk + "=? AND");
                sql.append(" CODFILIAL" + idFk + "=? AND");
                sql.append(" CODPLAN=?");
                try {
                    status.setText("Atulizando tabela " + tabela);
                    ps = con.prepareStatement(sql.toString());
                    ps.setInt(1, Aplicativo.iCodEmp);
                    ps.setInt(2, lcPlanDest.getCodFilial());
                    ps.setString(3, txtCodPlanDest.getVlrString());
                    ps.setInt(4, Aplicativo.iCodEmp);
                    ps.setInt(5, lcPlanOrig.getCodFilial());
                    ps.setString(6, txtCodPlanOrig.getVlrString());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    problema = true;
                    Funcoes.mensagemErro(this, "Erro ao atualizar planejamento de destino.\n" + e.getMessage(), true, con, e);
                    status.setText("");
                    break;
                }
            }
        } finally {
            try {
                if (problema) {
                    con.rollback();
                } else {
                    sql.delete(0, sql.length());
                    sql.append("DELETE FROM FNSALDOLANCA ");
                    sql.append("WHERE CODEMPPN=? AND CODFILIALPN=? AND CODPLAN=?");
                    ps = con.prepareStatement(sql.toString());
                    ps.setInt(1, Aplicativo.iCodEmp);
                    ps.setInt(2, lcPlanOrig.getCodFilial());
                    ps.setString(3, txtCodPlanOrig.getVlrString());
                    ps.executeUpdate();
                    con.commit();
                    btTransferir.setEnabled(false);
                    status.setText("Transfer�ncia completada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
