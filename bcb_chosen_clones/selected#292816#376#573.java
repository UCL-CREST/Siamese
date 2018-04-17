    public static String aplicCampos(ResultSet rs, String[] sNat, String[] sInfoMoeda) {
        Date dCampo = null;
        String sRet = null;
        String sTxa = null;
        String sCampo = null;
        String sParam1 = null;
        String sParam2 = null;
        try {
            sTxa = rs.getString("TxaModBol");
            sCampo = "";
            dCampo = null;
            String sObsOrc;
            String sObsVen;
            List<String> lObsOrc;
            int iNumLinObs = 0;
            int iNumColObs = 0;
            if (sTxa != null) {
                if ((sCampo = rs.getString("CODORC")) != null) sTxa = sTxa.replaceAll("\\[_CODORC_]", sCampo);
                if ((sCampo = rs.getString("NOMECONV")) != null) sTxa = sTxa.replaceAll("\\[_____________________NOMECONV___________________]", sCampo);
                sParam1 = "";
                sParam2 = "";
                sTxa = aplicaTxtObs(sTxa, "[OBSORC_", rs.getString("OBSORC"));
                while (sTxa.indexOf("[OBSVEN_") > -1) {
                    sTxa = aplicaTxtObs(sTxa, "[OBSVEN_", rs.getString("OBSVENDA"));
                }
                if ((dCampo = rs.getDate("DtVencItRec")) != null) sTxa = sTxa.replaceAll("\\[VENCIMEN]", StringFunctions.sqlDateToStrDate(dCampo));
                if ((dCampo = rs.getDate("DtEmitVenda")) != null) {
                    sTxa = sTxa.replaceAll("\\[DATADOC_]", StringFunctions.sqlDateToStrDate(dCampo));
                    sTxa = sTxa.replaceAll("\\[DIA_E]", StringFunctions.strZero(String.valueOf(Funcoes.getDiaMes(Funcoes.sqlDateToDate(dCampo))), 2));
                    sTxa = sTxa.replaceAll("\\[MES_E]", Funcoes.getMesExtenso(Funcoes.sqlDateToDate(dCampo)));
                    sTxa = sTxa.replaceAll("\\[ANO_E]", StringFunctions.strZero(String.valueOf(Funcoes.getAno(Funcoes.sqlDateToDate(dCampo))), 4));
                }
                if ((sCampo = rs.getString("CodRec")) != null) sTxa = sTxa.replaceAll("\\[CODREC]", Funcoes.alinhaDir(sCampo, 8));
                if ((sCampo = rs.getString("DocVenda")) != null) sTxa = sTxa.replaceAll("\\[__DOCUMENTO__]", Funcoes.copy(StringFunctions.strZero(sCampo.trim(), 8), 15));
                if ((sCampo = rs.getString("ReciboItRec")) != null) sTxa = sTxa.replaceAll("\\[RECIBO]", Funcoes.alinhaDir(sCampo, 8));
                if ((sCampo = rs.getString("NParcItRec")) != null) {
                    sTxa = sTxa.replaceAll("\\[P]", Funcoes.alinhaDir(sCampo, 2));
                    if (rs.getInt(1) > 1) sTxa = sTxa.replaceAll("\\[A]", "" + ((char) (rs.getInt("NParcItRec") + 64)));
                }
                if ((sCampo = rs.getInt("PARCS") + "") != null) sTxa = sTxa.replaceAll("\\[T]", "/" + Funcoes.copy(sCampo, 0, 2));
                if ((sCampo = rs.getString("VlrParcItRec")) != null && rs.getDouble("VlrParcItRec") != 0) {
                    sTxa = sTxa.replaceAll("\\[VALOR_DOCUMEN]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                    sTxa = sTxa.replaceAll("\\[VALOR_EXTENSO]", Extenso.extenso(rs.getDouble("VlrParcItRec"), sInfoMoeda[0], sInfoMoeda[1], sInfoMoeda[2], sInfoMoeda[3])).toUpperCase();
                }
                if ((sCampo = rs.getString("VlrApagItRec")) != null && rs.getDouble("VlrApagItRec") != 0) {
                    sTxa = sTxa.replaceAll("\\[VLIQ_DOCUMENT]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                }
                try {
                    if ((sCampo = rs.getString("VlrPagoItRec")) != null && rs.getDouble("VlrPagoItRec") != 0) {
                        sTxa = sTxa.replaceAll("\\[VPAGO_DOCUMENT]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                    }
                } catch (Exception e) {
                    System.out.println("Protegendo, caso n�o exista a coluna na query");
                }
                try {
                    if ((sCampo = rs.getString("NumCheq")) != null && rs.getDouble("VlrParcItRec") != 0) {
                        sTxa = sTxa.replaceAll("\\[NUM_CHEQ]", Funcoes.copy(StringFunctions.strZero(sCampo.trim(), 8), 15));
                    }
                } catch (Exception e) {
                    System.out.println("Protegendo, caso n�o exista a coluna na query");
                }
                if ((sCampo = rs.getString("VlrDescItRec")) != null && rs.getDouble("VlrDescItRec") != 0) {
                    sTxa = sTxa.replaceAll("\\[DESC_DOCUMENT]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                    sTxa = sTxa.replaceAll("\\[VALOR_EXTENSO_DESC]", Extenso.extenso(rs.getDouble("VlrDescItRec"), sInfoMoeda[0], sInfoMoeda[1], sInfoMoeda[2], sInfoMoeda[3])).toUpperCase();
                }
                try {
                    if (rs.getString("baseinss") != null) {
                        if (rs.getBigDecimal("retinss").floatValue() > 0) {
                            sCampo = Funcoes.bdToStrd(rs.getBigDecimal("baseinss"), 2).toString();
                        } else {
                            sCampo = "0";
                        }
                        sTxa = sTxa.replaceAll("\\[BASE_INSS]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                    }
                    if (rs.getString("baseirrf") != null) {
                        if (rs.getBigDecimal("retirrf").floatValue() > 0) {
                            sCampo = Funcoes.bdToStrd(rs.getBigDecimal("baseirrf"), 2).toString();
                        } else {
                            sCampo = "0";
                        }
                        sTxa = sTxa.replaceAll("\\[BASE_IRRF]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                    }
                    if (rs.getString("retirrf") != null) {
                        sCampo = Funcoes.bdToStrd(rs.getBigDecimal("retirrf"), 2).toString();
                        sTxa = sTxa.replaceAll("\\[VALOR_IRRF]", sCampo);
                    }
                    if (rs.getString("retinss") != null) {
                        sCampo = Funcoes.bdToStrd(rs.getBigDecimal("retinss"), 2).toString();
                        sTxa = sTxa.replaceAll("\\[VALOR_INSS]", sCampo);
                    }
                    if (rs.getString("aliqinss") != null) {
                        BigDecimal aliqinss = rs.getBigDecimal("aliqinss");
                        aliqinss = aliqinss.setScale(2, BigDecimal.ROUND_UP);
                        sCampo = Funcoes.bdToStrd(aliqinss, 2).toString();
                        sTxa = sTxa.replaceAll("\\[ALIQ_INSS]", sCampo);
                    }
                    if (rs.getString("aliqirrf") != null) {
                        sCampo = Funcoes.bdToStrd(rs.getBigDecimal("aliqirrf"), 2).toString();
                        sTxa = sTxa.replaceAll("\\[ALIQ_IRRF]", sCampo);
                    }
                    if (rs.getString("vlrbruto") != null) {
                        sCampo = Funcoes.bdToStrd(rs.getBigDecimal("vlrbruto"), 2).toString();
                        sTxa = sTxa.replaceAll("\\[VALOR_BRUTO]", sCampo);
                    }
                } catch (Exception e) {
                    System.out.print("N�o possui os campos relativos a reten��o de inss e irrf...");
                }
                if ((sCampo = rs.getString("CodCli")) != null) sTxa = sTxa.replaceAll("\\[CODCLI]", Funcoes.copy(sCampo, 0, 8));
                if ((sCampo = rs.getString("RazCli")) != null) sTxa = sTxa.replaceAll("\\[_____________RAZAO____DO____CLIENTE_____________]", Funcoes.copy(sCampo, 0, 50));
                if ((sCampo = rs.getString("NomeCli")) != null) sTxa = sTxa.replaceAll("\\[_____________NOME_____DO____CLIENTE_____________]", Funcoes.copy(sCampo, 0, 50));
                if ((sCampo = rs.getString("CpfCli")) != null) sTxa = sTxa.replaceAll("\\[CPF/CNPJ_ CLIENT]", Funcoes.setMascara(sCampo, "###.###.###-##")); else if ((sCampo = rs.getString("CnpjCli")) != null) sTxa = sTxa.replaceAll("\\[CPF/CNPJ_ CLIENT]", Funcoes.setMascara(sCampo, "##.###.###/####-##"));
                if ((sCampo = rs.getString("RgCli")) != null) sTxa = sTxa.replaceAll("\\[____IE/RG____CLIENTE]", Funcoes.copy(sCampo, 0, 22)); else if ((sCampo = rs.getString("InscCli")) != null) sTxa = sTxa.replaceAll("\\[____IE/RG____CLIENTE]", Funcoes.copy(sCampo, 0, 22));
                if ((sCampo = rs.getString("EndCob")) != null || (sCampo = rs.getString("EndCli")) != null) sTxa = sTxa.replaceAll("\\[____________ENDERECO____DO____CLIENTE___________]", Funcoes.copy(sCampo, 0, 31));
                if ((sCampo = rs.getString("NumCob")) != null || (sCampo = rs.getString("NumCli")) != null) sTxa = sTxa.replaceAll("\\[NUMERO]", Funcoes.copy(sCampo, 0, 10));
                if ((sCampo = rs.getString("EndCob")) != null || (sCampo = rs.getString("EndCli")) != null) {
                    if (rs.getString("EndCob") != null) {
                        sCampo = sCampo.trim() + ", " + rs.getString("numcob");
                    } else {
                        sCampo = sCampo.trim() + ", " + rs.getString("numcli");
                    }
                    sTxa = sTxa.replaceAll("\\[_______ENDERECO_COM_NUMERO_DO_CLIENTE___________]", Funcoes.copy(sCampo, 0, 50));
                }
                if ((sCampo = rs.getString("ComplCob")) != null || (sCampo = rs.getString("ComplCli")) != null) {
                    sTxa = sTxa.replaceAll("\\[____COMPLEMENTO___]", Funcoes.copy(sCampo, 0, 12));
                } else {
                    sTxa = sTxa.replaceAll("\\[____COMPLEMENTO___]", Funcoes.copy("", 0, 12));
                }
                if ((sCampo = rs.getString("BairCob")) != null || (sCampo = rs.getString("BairCli")) != null) sTxa = sTxa.replaceAll("\\[___________BAIRRO___________]", Funcoes.copy(sCampo, 0, 12));
                if ((sCampo = rs.getString("CepCob")) != null || (sCampo = rs.getString("CepCli")) != null) sTxa = sTxa.replaceAll("\\[__CEP__]", Funcoes.setMascara(sCampo, "#####-###"));
                if ((sCampo = rs.getString("CidCob")) != null || (sCampo = rs.getString("CidCli")) != null) sTxa = sTxa.replaceAll("\\[___________CIDADE___________]", sCampo.trim());
                if ((sCampo = rs.getString("UfCob")) != null || (sCampo = rs.getString("UfCli")) != null) sTxa = sTxa.replaceAll("\\[UF]", Funcoes.copy(sCampo, 0, 2));
                if ((sCampo = rs.getString("FoneCli")) != null) sTxa = sTxa.replaceAll("\\[__TELEFONE___]", Funcoes.setMascara(sCampo.trim(), "####-####"));
                if ((sCampo = rs.getString("DDDCli")) != null || (sCampo = "(" + rs.getString("DDDCli")) + ")" != null) sTxa = sTxa.replaceAll("\\[DDD]", Funcoes.copy(sCampo, 0, 5));
                if (sNat != null) {
                    if ((sCampo = sNat[0]) != null) sTxa = sTxa.replaceAll("\\[CODNAT]", Funcoes.copy(sCampo, 0, 8));
                    if ((sCampo = sNat[1]) != null) sTxa = sTxa.replaceAll("\\[______________NATUREZA_DA_OPERACAO______________]", Funcoes.copy(sCampo, 0, 50));
                }
                if ((sCampo = rs.getString("CODVENDA")) != null) sTxa = sTxa.replaceAll("\\[CODVENDA]", Funcoes.copy(sCampo, 0, 10));
                if ((sCampo = rs.getString("VlrApagRec")) != null && rs.getDouble("VlrApagRec") != 0) sTxa = sTxa.replaceAll("\\[TOTAL_PARCELAS]", Funcoes.strDecimalToStrCurrency(15, 2, sCampo));
                if ((sCampo = rs.getString("NomeVend")) != null || (sCampo = rs.getString("NomeVend")) != null) sTxa = sTxa.replaceAll("\\[_______COMISSIONADO1_______]", Funcoes.copy(sCampo, 0, 30));
                if ((sCampo = rs.getString("NomeVend2")) != null || (sCampo = rs.getString("NomeVend2")) != null) sTxa = sTxa.replaceAll("\\[_______COMISSIONADO2_______]", Funcoes.copy(sCampo, 0, 30));
                if ((sCampo = rs.getString("NomeVend3")) != null || (sCampo = rs.getString("NomeVend3")) != null) sTxa = sTxa.replaceAll("\\[_______COMISSIONADO3_______]", Funcoes.copy(sCampo, 0, 30));
                if ((sCampo = rs.getString("NomeVend4")) != null || (sCampo = rs.getString("NomeVend4")) != null) sTxa = sTxa.replaceAll("\\[_______COMISSIONADO4_______]", Funcoes.copy(sCampo, 0, 30));
                int iPos = 0;
                while ((iPos = sTxa.indexOf("%_VAL", iPos + 1)) > 0) {
                    double dVal = 0;
                    String sCaixa = sTxa.substring(iPos - 9, iPos);
                    sCaixa += "\\" + sTxa.substring(iPos, iPos + 6);
                    dVal = rs.getDouble("VlrParcitRec");
                    dVal *= Double.parseDouble(sTxa.substring(iPos - 8, iPos)) / 100;
                    sTxa = sTxa.replaceAll("\\" + sCaixa, Funcoes.strDecimalToStrCurrency(15, 2, new BigDecimal(dVal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                iPos = 0;
                while ((iPos = sTxa.indexOf("+_VAL", iPos + 1)) > 0) {
                    double dVal = 0;
                    String sCaixa = sTxa.substring(iPos - 9, iPos);
                    sCaixa += "\\" + sTxa.substring(iPos, iPos + 6);
                    dVal = rs.getDouble("VlrParcitRec");
                    dVal += Double.parseDouble(sTxa.substring(iPos - 8, iPos));
                    sTxa = sTxa.replaceAll("\\" + sCaixa, Funcoes.strDecimalToStrCurrency(15, 2, new BigDecimal(dVal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                iPos = 0;
                while ((iPos = sTxa.indexOf("-_VAL", iPos + 1)) > 0) {
                    double dVal = 0;
                    String sCaixa = sTxa.substring(iPos - 9, iPos);
                    sCaixa += "\\" + sTxa.substring(iPos, iPos + 6);
                    dVal = rs.getDouble("VlrParcitRec");
                    dVal -= Double.parseDouble(sTxa.substring(iPos - 8, iPos));
                    sTxa = sTxa.replaceAll("\\" + sCaixa, Funcoes.strDecimalToStrCurrency(15, 2, new BigDecimal(dVal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                iPos = 0;
                while ((iPos = sTxa.indexOf("+_VEN", iPos + 1)) > 0) {
                    GregorianCalendar cVal = new GregorianCalendar();
                    String sCaixa = sTxa.substring(iPos - 4, iPos);
                    sCaixa += "\\" + sTxa.substring(iPos, iPos + 6);
                    cVal.setTime(rs.getDate("DtVencItRec"));
                    cVal.set(Calendar.DATE, cVal.get(Calendar.DATE) + Integer.parseInt(sTxa.substring(iPos - 3, iPos)));
                    sTxa = sTxa.replaceAll("\\" + sCaixa, Funcoes.dateToStrDate(cVal.getTime()));
                }
                sRet = sTxa;
            }
            sRet = sRet.replaceAll("\\<LP\\>.*].*\\<_LP\\>", "");
            sRet = sRet.replaceAll("\\<[_]*LP\\>", "");
            sRet = sRet.replaceAll("\\<EJECT\\>", "" + ((char) 12) + ((char) 13));
            Pattern p = Pattern.compile("\\[.*\\]");
            Matcher m = p.matcher(sRet);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, StringFunctions.replicate(" ", m.end() - m.start()));
            }
            m.appendTail(sb);
            sRet = sb.toString();
        } catch (SQLException err) {
            Funcoes.mensagemErro(null, "Erro na consulta ao modelo de boleto!\n" + err.getMessage(), true, Aplicativo.getInstace().getConexao(), err);
            err.printStackTrace();
        }
        return sRet;
    }
