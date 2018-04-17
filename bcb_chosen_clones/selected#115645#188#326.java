    private boolean processar(int iCodProd) {
        String sSQL = null;
        String sSQLCompra = null;
        String sSQLInventario = null;
        String sSQLVenda = null;
        String sSQLRMA = null;
        String sSQLOP = null;
        String sSQLOP_SP = null;
        String sWhere = null;
        String sProd = null;
        String sWhereCompra = null;
        String sWhereInventario = null;
        String sWhereVenda = null;
        String sWhereRMA = null;
        String sWhereOP = null;
        String sWhereOP_SP = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean bOK = false;
        try {
            try {
                sWhere = "";
                sProd = "";
                if (cbTudo.getVlrString().equals("S")) sProd = "[" + iCodProd + "] ";
                if (!(txtDataini.getVlrString().equals(""))) {
                    sWhere = " AND DTMOVPROD >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                }
                sSQL = "DELETE FROM EQMOVPROD WHERE " + "CODEMP=? AND CODPROD=?" + sWhere;
                state(sProd + "Limpando movimenta��es desatualizadas...");
                ps = con.prepareStatement(sSQL);
                ps.setInt(1, Aplicativo.iCodEmp);
                ps.setInt(2, iCodProd);
                ps.executeUpdate();
                ps.close();
                if ((txtDataini.getVlrString().equals(""))) {
                    sSQL = "UPDATE EQPRODUTO SET SLDPROD=0 WHERE " + "CODEMP=? AND CODPROD=?";
                    ps = con.prepareStatement(sSQL);
                    ps.setInt(1, Aplicativo.iCodEmp);
                    ps.setInt(2, iCodProd);
                    ps.executeUpdate();
                    ps.close();
                    state(sProd + "Limpando saldos...");
                    sSQL = "UPDATE EQSALDOPROD SET SLDPROD=0 WHERE CODEMP=? AND CODPROD=?";
                    ps = con.prepareStatement(sSQL);
                    ps.setInt(1, Aplicativo.iCodEmp);
                    ps.setInt(2, iCodProd);
                    ps.executeUpdate();
                    ps.close();
                    state(sProd + "Limpando saldos...");
                }
                bOK = true;
            } catch (SQLException err) {
                Funcoes.mensagemErro(null, "Erro ao limpar estoques!\n" + err.getMessage(), true, con, err);
            }
            if (bOK) {
                bOK = false;
                if (!txtDataini.getVlrString().equals("")) {
                    sWhereCompra = " AND C.DTENTCOMPRA >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                    sWhereInventario = " AND I.DATAINVP >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                    sWhereVenda = " AND V.DTEMITVENDA >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                    sWhereRMA = " AND RMA.DTAEXPRMA >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                    sWhereOP = " AND O.DTFABROP >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                    sWhereOP_SP = " AND O.DTSUBPROD >= '" + Funcoes.dateToStrDB(txtDataini.getVlrDate()) + "'";
                } else {
                    sWhereCompra = "";
                    sWhereInventario = "";
                    sWhereVenda = "";
                    sWhereRMA = "";
                    sWhereOP = "";
                    sWhereOP_SP = "";
                }
                sSQLInventario = "SELECT 'A' TIPOPROC, I.CODEMPPD, I.CODFILIALPD, I.CODPROD," + "I.CODEMPLE, I.CODFILIALLE, I.CODLOTE," + "I.CODEMPTM, I.CODFILIALTM, I.CODTIPOMOV," + "I.CODEMP, I.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, " + "I.CODINVPROD CODMASTER, I.CODINVPROD CODITEM, " + "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT ,CAST(NULL AS CHAR(4)) CODNAT," + "I.DATAINVP DTPROC, I.CODINVPROD DOCPROC,'N' FLAG," + "I.QTDINVP QTDPROC, I.PRECOINVP CUSTOPROC, " + "I.CODEMPAX, I.CODFILIALAX, I.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod  " + "FROM EQINVPROD I " + "WHERE I.CODEMP=? AND I.CODPROD = ?" + sWhereInventario;
                sSQLCompra = "SELECT 'C' TIPOPROC, IC.CODEMPPD, IC.CODFILIALPD, IC.CODPROD," + "IC.CODEMPLE, IC.CODFILIALLE, IC.CODLOTE," + "C.CODEMPTM, C.CODFILIALTM, C.CODTIPOMOV," + "C.CODEMP, C.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, " + "C.CODCOMPRA CODMASTER, IC.CODITCOMPRA CODITEM," + "IC.CODEMPNT, IC.CODFILIALNT, IC.CODNAT, " + "C.DTENTCOMPRA DTPROC, C.DOCCOMPRA DOCPROC, C.FLAG," + "IC.QTDITCOMPRA QTDPROC, IC.CUSTOITCOMPRA CUSTOPROC, " + "IC.CODEMPAX, IC.CODFILIALAX, IC.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod " + "FROM CPCOMPRA C,CPITCOMPRA IC " + "WHERE IC.CODCOMPRA=C.CODCOMPRA AND " + "IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL AND IC.QTDITCOMPRA > 0 AND " + "C.CODEMP=? AND IC.CODPROD = ?" + sWhereCompra;
                sSQLOP = "SELECT 'O' TIPOPROC, O.CODEMPPD, O.CODFILIALPD, O.CODPROD," + "O.CODEMPLE, O.CODFILIALLE, O.CODLOTE," + "O.CODEMPTM, O.CODFILIALTM, O.CODTIPOMOV," + "O.CODEMP, O.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA ," + "O.CODOP CODMASTER, CAST(O.SEQOP AS INTEGER) CODITEM," + "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " + "CAST(NULL AS CHAR(4)) CODNAT, " + "coalesce(oe.dtent,O.DTFABROP) DTPROC, " + "O.CODOP DOCPROC, 'N' FLAG, " + "coalesce(oe.qtdent,O.QTDFINALPRODOP) QTDPROC, " + "( SELECT SUM(PD.CUSTOMPMPROD) FROM PPITOP IT, EQPRODUTO PD " + "WHERE IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL AND " + "IT.CODOP=O.CODOP AND IT.SEQOP=O.SEQOP AND " + "PD.CODEMP=IT.CODEMPPD AND PD.CODFILIAL=IT.CODFILIALPD AND " + "PD.CODPROD=IT.CODPROD) CUSTOPROC, " + "O.CODEMPAX, O.CODFILIALAX, O.CODALMOX, oe.seqent, CAST(NULL AS SMALLINT) as seqsubprod " + "FROM PPOP O " + " left outer join ppopentrada oe on oe.codemp=o.codemp and oe.codfilial=o.codfilial and oe.codop=o.codop and oe.seqop=o.seqop " + "WHERE O.QTDFINALPRODOP > 0 AND " + "O.CODEMP=? AND O.CODPROD = ? " + sWhereOP;
                sSQLOP_SP = "SELECT 'S' TIPOPROC, O.CODEMPPD, O.CODFILIALPD, O.CODPROD," + "O.CODEMPLE, O.CODFILIALLE, O.CODLOTE," + "O.CODEMPTM, O.CODFILIALTM, O.CODTIPOMOV," + "O.CODEMP, O.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA ," + "O.CODOP CODMASTER, CAST(O.SEQOP AS INTEGER) CODITEM," + "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " + "CAST(NULL AS CHAR(4)) CODNAT, " + "coalesce(o.dtsubprod,Op.DTFABROP) DTPROC, " + "O.CODOP DOCPROC, 'N' FLAG, " + "O.QTDITSP QTDPROC, " + "( SELECT PD.CUSTOMPMPROD FROM EQPRODUTO PD " + "WHERE PD.CODEMP=O.CODEMPPD AND PD.CODFILIAL=O.CODFILIALPD AND " + "PD.CODPROD=O.CODPROD) CUSTOPROC, " + "OP.CODEMPAX, OP.CODFILIALAX, OP.CODALMOX, CAST(NULL AS SMALLINT) as seqent, O.SEQSUBPROD " + "FROM PPOPSUBPROD O, PPOP OP " + "WHERE O.QTDITSP > 0 AND " + "O.CODEMP=OP.CODEMP and O.CODFILIAL=OP.CODFILIAL and O.CODOP=OP.CODOP and O.SEQOP=OP.SEQOP AND " + "O.CODEMP=? AND O.CODPROD = ?" + sWhereOP_SP;
                sSQLRMA = "SELECT 'R' TIPOPROC, IT.CODEMPPD, IT.CODFILIALPD, IT.CODPROD, " + "IT.CODEMPLE, IT.CODFILIALLE, IT.CODLOTE, " + "RMA.CODEMPTM, RMA.CODFILIALTM, RMA.CODTIPOMOV, " + "RMA.CODEMP, RMA.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, " + "IT.CODRMA CODMASTER, CAST(IT.CODITRMA AS INTEGER) CODITEM, " + "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " + "CAST(NULL AS CHAR(4)) CODNAT, " + "COALESCE(IT.DTAEXPITRMA,RMA.DTAREQRMA) DTPROC, " + "RMA.CODRMA DOCPROC, 'N' FLAG, " + "IT.QTDEXPITRMA QTDPROC, IT.PRECOITRMA CUSTOPROC," + "IT.CODEMPAX, IT.CODFILIALAX, IT.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod   " + "FROM EQRMA RMA ,EQITRMA IT " + "WHERE IT.CODRMA=RMA.CODRMA AND " + "IT.CODEMP=RMA.CODEMP AND IT.CODFILIAL=RMA.CODFILIAL AND " + "IT.QTDITRMA > 0 AND " + "RMA.CODEMP=? AND IT.CODPROD = ?" + sWhereRMA;
                sSQLVenda = "SELECT 'V' TIPOPROC, IV.CODEMPPD, IV.CODFILIALPD, IV.CODPROD," + "IV.CODEMPLE, IV.CODFILIALLE, IV.CODLOTE," + "V.CODEMPTM, V.CODFILIALTM, V.CODTIPOMOV," + "V.CODEMP, V.CODFILIAL, V.TIPOVENDA, " + "V.CODVENDA CODMASTER, IV.CODITVENDA CODITEM, " + "IV.CODEMPNT, IV.CODFILIALNT, IV.CODNAT, " + "V.DTEMITVENDA DTPROC, V.DOCVENDA DOCPROC, V.FLAG, " + "IV.QTDITVENDA QTDPROC, IV.VLRLIQITVENDA CUSTOPROC, " + "IV.CODEMPAX, IV.CODFILIALAX, IV.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod   " + "FROM VDVENDA V ,VDITVENDA IV " + "WHERE IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA = V.TIPOVENDA AND " + "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " + "IV.QTDITVENDA > 0 AND " + "V.CODEMP=? AND IV.CODPROD = ?" + sWhereVenda;
                try {
                    state(sProd + "Iniciando reconstru��o...");
                    sSQL = sSQLInventario + " UNION ALL " + sSQLCompra + " UNION ALL " + sSQLOP + " UNION ALL " + sSQLOP_SP + " UNION ALL " + sSQLRMA + " UNION ALL " + sSQLVenda + " ORDER BY 19,1,20";
                    System.out.println(sSQL);
                    ps = con.prepareStatement(sSQL);
                    ps.setInt(paramCons.CODEMPIV.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODIV.ordinal(), iCodProd);
                    ps.setInt(paramCons.CODEMPCP.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODCP.ordinal(), iCodProd);
                    ps.setInt(paramCons.CODEMPOP.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODOP.ordinal(), iCodProd);
                    ps.setInt(paramCons.CODEMPOPSP.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODOPSP.ordinal(), iCodProd);
                    ps.setInt(paramCons.CODEMPRM.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODRM.ordinal(), iCodProd);
                    ps.setInt(paramCons.CODEMPVD.ordinal(), Aplicativo.iCodEmp);
                    ps.setInt(paramCons.CODPRODVD.ordinal(), iCodProd);
                    rs = ps.executeQuery();
                    bOK = true;
                    while (rs.next() && bOK) {
                        bOK = insereMov(rs, sProd);
                    }
                    rs.close();
                    ps.close();
                    state(sProd + "Aguardando grava��o final...");
                } catch (SQLException err) {
                    bOK = false;
                    err.printStackTrace();
                    Funcoes.mensagemErro(null, "Erro ao reconstruir base!\n" + err.getMessage(), true, con, err);
                }
            }
            try {
                if (bOK) {
                    con.commit();
                    state(sProd + "Registros processados com sucesso!");
                } else {
                    state(sProd + "Registros antigos restaurados!");
                    con.rollback();
                }
            } catch (SQLException err) {
                err.printStackTrace();
                Funcoes.mensagemErro(null, "Erro ao relizar procedimento!\n" + err.getMessage(), true, con, err);
            }
        } finally {
            sSQL = null;
            sSQLCompra = null;
            sSQLInventario = null;
            sSQLVenda = null;
            sSQLRMA = null;
            sWhere = null;
            sProd = null;
            sWhereCompra = null;
            sWhereInventario = null;
            sWhereVenda = null;
            sWhereRMA = null;
            rs = null;
            ps = null;
            bRunProcesso = false;
            btProcessar.setEnabled(true);
        }
        return bOK;
    }
