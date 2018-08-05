    private void gerarComissao() {
        int opt = Funcoes.mensagemConfirma(null, "Confirma gerar comiss�es para o vendedor " + txtNomeVend.getVlrString().trim() + "?");
        if (opt == JOptionPane.OK_OPTION) {
            StringBuilder insert = new StringBuilder();
            insert.append("INSERT INTO RPCOMISSAO ");
            insert.append("(CODEMP, CODFILIAL, CODPED, CODITPED, ");
            insert.append("CODEMPVD, CODFILIALVD, CODVEND, VLRCOMISS ) ");
            insert.append("VALUES ");
            insert.append("(?,?,?,?,?,?,?,?)");
            PreparedStatement ps;
            int parameterIndex;
            boolean gerou = false;
            try {
                for (int i = 0; i < tab.getNumLinhas(); i++) {
                    if (((BigDecimal) tab.getValor(i, 8)).floatValue() > 0) {
                        parameterIndex = 1;
                        ps = con.prepareStatement(insert.toString());
                        ps.setInt(parameterIndex++, AplicativoRep.iCodEmp);
                        ps.setInt(parameterIndex++, ListaCampos.getMasterFilial("RPCOMISSAO"));
                        ps.setInt(parameterIndex++, txtCodPed.getVlrInteger());
                        ps.setInt(parameterIndex++, (Integer) tab.getValor(i, ETabNota.ITEM.ordinal()));
                        ps.setInt(parameterIndex++, AplicativoRep.iCodEmp);
                        ps.setInt(parameterIndex++, ListaCampos.getMasterFilial("RPVENDEDOR"));
                        ps.setInt(parameterIndex++, txtCodVend.getVlrInteger());
                        ps.setBigDecimal(parameterIndex++, (BigDecimal) tab.getValor(i, ETabNota.VLRCOMIS.ordinal()));
                        ps.executeUpdate();
                        gerou = true;
                    }
                }
                if (gerou) {
                    Funcoes.mensagemInforma(null, "Comiss�o gerada para " + txtNomeVend.getVlrString().trim());
                    txtCodPed.setText("0");
                    lcPedido.carregaDados();
                    carregaTabela();
                    con.commit();
                } else {
                    Funcoes.mensagemInforma(null, "N�o foi possiv�l gerar comiss�o!\nVerifique os valores das comiss�es dos itens.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Funcoes.mensagemErro(this, "Erro ao gerar comiss�o!\n" + e.getMessage());
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
