    private boolean postCorrecao() {
        boolean valido = false;
        Integer newCodCorrecao = null;
        String sqlmaxac = "SELECT MAX(SEQAC) FROM PPOPACAOCORRET WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?";
        String sqlmaxcq = "SELECT MAX(SEQOPCQ) + 1 FROM PPOPCQ WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?";
        try {
            for (Entry<Integer, JCheckBoxPad> ek : analises.entrySet()) {
                JCheckBoxPad cb = ek.getValue();
                if ("S".equals(cb.getVlrString())) {
                    valido = true;
                    keysItens[2] = ek.getKey();
                    break;
                }
            }
            if (!valido) {
                Funcoes.mensagemInforma(this, "Selecione as analises para aplicar a corre��o!");
                return false;
            }
            if (txaCausa.getVlrString().trim().length() == 0) {
                Funcoes.mensagemInforma(this, "Informe as causas!");
                return false;
            }
            if (txaAcao.getVlrString().trim().length() == 0) {
                Funcoes.mensagemInforma(this, "Detalhe a a��o corretiva!");
                return false;
            }
            PreparedStatement ps = con.prepareStatement(sqlmaxac);
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("PPOPACAOCORRET"));
            ps.setInt(3, txtCodOP.getVlrInteger());
            ps.setInt(4, txtSeqOP.getVlrInteger());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                newCodCorrecao = rs.getInt(1) + 1;
                keysItens[3] = newCodCorrecao;
            }
            rs.close();
            ps.close();
            if (newCodCorrecao != null) {
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO PPOPACAOCORRET ");
                sql.append("( CODEMP, CODFILIAL, CODOP, SEQOP, SEQAC, TPCAUSA, OBSCAUSA, TPACAO, OBSACAO ) ");
                sql.append("VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, Aplicativo.iCodEmp);
                ps.setInt(2, ListaCampos.getMasterFilial("PPOPACAOCORRET"));
                ps.setInt(3, txtCodOP.getVlrInteger());
                ps.setInt(4, txtSeqOP.getVlrInteger());
                ps.setInt(5, newCodCorrecao);
                ps.setString(6, m.getCode());
                ps.setString(7, txaCausa.getVlrString());
                ps.setString(8, rgSolucao.getVlrString());
                ps.setString(9, txaAcao.getVlrString());
                ps.execute();
                ps.close();
                String strAnalises = "";
                for (Entry<Integer, JCheckBoxPad> ek : analises.entrySet()) {
                    JCheckBoxPad cb = ek.getValue();
                    if ("S".equals(cb.getVlrString())) {
                        if (strAnalises.trim().length() > 0) {
                            strAnalises += ",";
                        }
                        strAnalises += String.valueOf(ek.getKey());
                    }
                }
                sql = new StringBuilder();
                sql.append("UPDATE PPOPCQ SET SEQAC=? ");
                sql.append("WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND SEQOPCQ IN ( " + strAnalises + " )");
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, newCodCorrecao);
                ps.setInt(2, Aplicativo.iCodEmp);
                ps.setInt(3, ListaCampos.getMasterFilial("PPOPACAOCORRET"));
                ps.setInt(4, txtCodOP.getVlrInteger());
                ps.setInt(5, txtSeqOP.getVlrInteger());
                ps.executeUpdate();
                ps.close();
                sql.delete(0, sql.length());
                sql.append("INSERT INTO PPOPCQ (CODEMP,CODFILIAL,CODOP,SEQOP,SEQOPCQ,");
                sql.append("CODEMPEA,CODFILIALEA,CODESTANALISE) ");
                sql.append("SELECT CODEMP,CODFILIAL,CODOP,SEQOP,(");
                sql.append(sqlmaxcq);
                sql.append("),CODEMPEA,CODFILIALEA,CODESTANALISE ");
                sql.append("FROM PPOPCQ ");
                sql.append("WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND ");
                sql.append("SEQOP=? AND SEQOPCQ IN ( " + strAnalises + " )");
                System.out.println(sql.toString());
                ps = con.prepareStatement(sql.toString());
                ps = con.prepareStatement(sql.toString());
                ps.setInt(1, Aplicativo.iCodEmp);
                ps.setInt(2, ListaCampos.getMasterFilial("PPOPCQ"));
                ps.setInt(3, txtCodOP.getVlrInteger());
                ps.setInt(4, txtSeqOP.getVlrInteger());
                ps.setInt(5, Aplicativo.iCodEmp);
                ps.setInt(6, ListaCampos.getMasterFilial("PPOPCQ"));
                ps.setInt(7, txtCodOP.getVlrInteger());
                ps.setInt(8, txtSeqOP.getVlrInteger());
                ps.executeUpdate();
                ps.close();
                montaAnalises();
                Funcoes.mensagemInforma(this, "A��o corretiva aplicada com sucesso!");
            }
            con.commit();
        } catch (Exception err) {
            try {
                con.rollback();
            } catch (SQLException e) {
                System.out.println("Erro ao realizar rollback!\n" + err.getMessage());
            }
            err.printStackTrace();
            Funcoes.mensagemErro(this, "Erro ao atualizar analises!\n" + err.getMessage(), true, con, err);
            valido = false;
        }
        return valido;
    }
