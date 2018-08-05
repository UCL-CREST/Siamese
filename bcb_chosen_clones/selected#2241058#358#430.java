    private void gravaOp(Vector<?> op) {
        PreparedStatement ps = null;
        String sql = null;
        ResultSet rs = null;
        int seqop = 0;
        Date dtFabrOP = null;
        try {
            sql = "SELECT MAX(SEQOP) FROM PPOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("PPOP"));
            ps.setInt(3, txtCodOP.getVlrInteger().intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                seqop = rs.getInt(1) + 1;
            }
            rs.close();
            ps.close();
            con.commit();
            sql = "SELECT DTFABROP FROM PPOP WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("PPOP"));
            ps.setInt(3, txtCodOP.getVlrInteger().intValue());
            ps.setInt(4, txtSeqOP.getVlrInteger().intValue());
            rs = ps.executeQuery();
            if (rs.next()) {
                dtFabrOP = rs.getDate(1);
            }
            rs.close();
            ps.close();
            con.commit();
            sql = "INSERT INTO PPOP (CODEMP,CODFILIAL,CODOP,SEQOP,CODEMPPD,CODFILIALPD,CODPROD,SEQEST,DTFABROP," + "QTDPREVPRODOP,QTDFINALPRODOP,DTVALIDPDOP,CODEMPLE,CODFILIALLE,CODLOTE,CODEMPTM,CODFILIALTM,CODTIPOMOV," + "CODEMPAX,CODFILIALAX,CODALMOX,CODEMPOPM,CODFILIALOPM,CODOPM,SEQOPM,QTDDISTIOP,QTDSUGPRODOP)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("PPOP"));
            ps.setInt(3, txtCodOP.getVlrInteger().intValue());
            ps.setInt(4, seqop);
            ps.setInt(5, Aplicativo.iCodEmp);
            ps.setInt(6, ListaCampos.getMasterFilial("PPESTRUTURA"));
            ps.setInt(7, ((Integer) op.elementAt(4)).intValue());
            ps.setInt(8, ((Integer) op.elementAt(6)).intValue());
            ps.setDate(9, dtFabrOP);
            ps.setFloat(10, ((BigDecimal) op.elementAt(7)).floatValue());
            ps.setFloat(11, 0);
            ps.setDate(12, (Funcoes.strDateToSqlDate((String) op.elementAt(11))));
            ps.setInt(13, Aplicativo.iCodEmp);
            ps.setInt(14, ListaCampos.getMasterFilial("EQLOTE"));
            ps.setString(15, ((String) op.elementAt(10)));
            ps.setInt(16, Aplicativo.iCodEmp);
            ps.setInt(17, ListaCampos.getMasterFilial("EQTIPOMOV"));
            ps.setInt(18, buscaTipoMov());
            ps.setInt(19, ((Integer) op.elementAt(13)).intValue());
            ps.setInt(20, ((Integer) op.elementAt(14)).intValue());
            ps.setInt(21, ((Integer) op.elementAt(12)).intValue());
            ps.setInt(22, Aplicativo.iCodEmp);
            ps.setInt(23, ListaCampos.getMasterFilial("PPOP"));
            ps.setInt(24, txtCodOP.getVlrInteger().intValue());
            ps.setInt(25, txtSeqOP.getVlrInteger().intValue());
            ps.setFloat(26, ((BigDecimal) op.elementAt(9)).floatValue());
            ps.setFloat(27, ((BigDecimal) op.elementAt(7)).floatValue());
            ps.executeUpdate();
            ps.close();
            con.commit();
            geraRMA(seqop);
        } catch (SQLException e) {
            Funcoes.mensagemErro(null, "Erro ao gerar OP's de distribui��o!\n" + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException eb) {
            }
        }
    }
