    public void importarEmpresasAbertas(File pArquivoTXT, Andamento pAndamento) throws FileNotFoundException, SQLException {
        int numeroDoRegistro = -1;
        Scanner in = null;
        Statement stmtLimpezaInicialDestino = conDestino.createStatement();
        String sql = "TRUNCATE TABLE TMP_TB_CIA_ABERTA";
        stmtLimpezaInicialDestino.executeUpdate(sql);
        sql = "INSERT INTO TMP_TB_CIA_ABERTA(CODIGO_CVM, DENOMINACAO_SOCIAL, DENOMINACAO_COMERCIAL, LOGRADOURO, COMPLEMENTO, BAIRRO, CEP, MUNICIPIO, UF, DDD, TELEFONE, FAX, DENOMINACAO_ANTERIOR, SETOR_ATIVIDADE, CNPJ, DRI, AUDITOR, QUANT_DE_ACOES_ORDINARIAS, QUANT_DE_ACOES_PREF, SITUACAO, DATA_DA_SITUACAO, TIPO_PAPEL1, TIPO_PAPEL2, TIPO_PAPEL3, TIPO_PAPEL4, TIPO_PAPEL5, TIPO_PAPEL6, CONTROLE_ACIONARIO, DATA_DE_REGISTRO, DATA_DO_CANCELAMENTO, MERCADO, BOLSA1, BOLSA2, BOLSA3, BOLSA4, BOLSA5, BOLSA6, BOLSA7, BOLSA8, BOLSA9, MOTIVO_DO_CANCELAMENTO, PATRIMONIO_LIQUIDO, DATA_DO_PATRIMONIO, E_MAIL, NOME_SETOR_ATIVIDADE, DATA_DA_ACAO, TIPO_NEGOCIO1, TIPO_NEGOCIO2, TIPO_NEGOCIO3, TIPO_NEGOCIO4, TIPO_NEGOCIO5, TIPO_NEGOCIO6, TIPO_MERCADO1, TIPO_MERCADO2, TIPO_MERCADO3, TIPO_MERCADO4, TIPO_MERCADO5, TIPO_MERCADO6) VALUES(:CODIGO_CVM, :DENOMINACAO_SOCIAL, :DENOMINACAO_COMERCIAL, :LOGRADOURO, :COMPLEMENTO, :BAIRRO, :CEP, :MUNICIPIO, :UF, :DDD, :TELEFONE, :FAX, :DENOMINACAO_ANTERIOR, :SETOR_ATIVIDADE, :CNPJ, :DRI, :AUDITOR, :QUANT_DE_ACOES_ORDINARIAS, :QUANT_DE_ACOES_PREF, :SITUACAO, :DATA_DA_SITUACAO, :TIPO_PAPEL1, :TIPO_PAPEL2, :TIPO_PAPEL3, :TIPO_PAPEL4, :TIPO_PAPEL5, :TIPO_PAPEL6, :CONTROLE_ACIONARIO, :DATA_DE_REGISTRO, :DATA_DO_CANCELAMENTO, :MERCADO, :BOLSA1, :BOLSA2, :BOLSA3, :BOLSA4, :BOLSA5, :BOLSA6, :BOLSA7, :BOLSA8, :BOLSA9, :MOTIVO_DO_CANCELAMENTO, :PATRIMONIO_LIQUIDO, :DATA_DO_PATRIMONIO, :E_MAIL, :NOME_SETOR_ATIVIDADE, :DATA_DA_ACAO, :TIPO_NEGOCIO1, :TIPO_NEGOCIO2, :TIPO_NEGOCIO3, :TIPO_NEGOCIO4, :TIPO_NEGOCIO5, :TIPO_NEGOCIO6, :TIPO_MERCADO1, :TIPO_MERCADO2, :TIPO_MERCADO3, :TIPO_MERCADO4, :TIPO_MERCADO5, :TIPO_MERCADO6)";
        OraclePreparedStatement stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
        stmtDestino.setExecuteBatch(COMANDOS_POR_LOTE);
        final int TAMANHO_DO_CABECALHO_DO_ARQUIVO = 707;
        final int TAMANHO_DO_RODAPE_DO_ARQUIVO = 0;
        final int TAMANHO_DOS_METADADOS_DO_ARQUIVO = TAMANHO_DO_CABECALHO_DO_ARQUIVO + TAMANHO_DO_RODAPE_DO_ARQUIVO;
        final int TAMANHO_MEDIO_POR_REGISTRO = 659;
        long tamanhoDosArquivos = pArquivoTXT.length();
        int quantidadeDeRegistrosEstimada = (int) (tamanhoDosArquivos - TAMANHO_DOS_METADADOS_DO_ARQUIVO) / TAMANHO_MEDIO_POR_REGISTRO;
        try {
            in = new Scanner(new FileInputStream(pArquivoTXT), Constantes.CONJUNTO_DE_CARACTERES_DO_ARQUIVO_TEXTO_DA_CVM.name());
            int quantidadeDeRegistrosImportada = 0;
            String registro;
            String[] campos;
            in.nextLine();
            numeroDoRegistro = 0;
            int vCODIGO_CVM;
            String vDENOMINACAO_SOCIAL, vDENOMINACAO_COMERCIAL, vLOGRADOURO, vCOMPLEMENTO, vBAIRRO;
            BigDecimal vCEP;
            String vMUNICIPIO, vUF;
            BigDecimal vDDD, vTELEFONE, vFAX;
            String vDENOMINACAO_ANTERIOR, vSETOR_ATIVIDADE;
            BigDecimal vCNPJ;
            String vDRI, vAUDITOR;
            BigDecimal vQUANT_DE_ACOES_ORDINARIAS, vQUANT_DE_ACOES_PREF;
            String vSITUACAO;
            java.sql.Date vDATA_DA_SITUACAO;
            String vTIPO_PAPEL1, vTIPO_PAPEL2, vTIPO_PAPEL3, vTIPO_PAPEL4, vTIPO_PAPEL5, vTIPO_PAPEL6, vCONTROLE_ACIONARIO;
            java.sql.Date vDATA_DE_REGISTRO, vDATA_DO_CANCELAMENTO;
            String vMERCADO, vBOLSA1, vBOLSA2, vBOLSA3, vBOLSA4, vBOLSA5, vBOLSA6, vBOLSA7, vBOLSA8, vBOLSA9, vMOTIVO_DO_CANCELAMENTO;
            BigDecimal vPATRIMONIO_LIQUIDO;
            java.sql.Date vDATA_DO_PATRIMONIO;
            String vE_MAIL, vNOME_SETOR_ATIVIDADE;
            java.sql.Date vDATA_DA_ACAO;
            String vTIPO_NEGOCIO1, vTIPO_NEGOCIO2, vTIPO_NEGOCIO3, vTIPO_NEGOCIO4, vTIPO_NEGOCIO5, vTIPO_NEGOCIO6, vTIPO_MERCADO1, vTIPO_MERCADO2, vTIPO_MERCADO3, vTIPO_MERCADO4, vTIPO_MERCADO5, vTIPO_MERCADO6;
            final int QTDE_CAMPOS = CampoDoArquivoDasEmpresasAbertas.values().length;
            final String SEPARADOR_DE_CAMPOS_DO_REGISTRO = ";";
            while (in.hasNextLine()) {
                ++numeroDoRegistro;
                registro = in.nextLine();
                stmtDestino.clearParameters();
                ArrayList<String> camposTmp = new ArrayList<String>(QTDE_CAMPOS);
                StringBuilder campoTmp = new StringBuilder();
                char[] registroTmp = registro.toCharArray();
                char c;
                boolean houveMesclagemDeCampos = false;
                boolean campoIniciaComEspacoEmBranco, campoPossuiConteudo, registroComExcessoDeDelimitadores;
                int quantidadeDeDelimitadoresEncontrados = (registro.length() - registro.replace(SEPARADOR_DE_CAMPOS_DO_REGISTRO, "").length());
                registroComExcessoDeDelimitadores = (quantidadeDeDelimitadoresEncontrados > (QTDE_CAMPOS - 1));
                for (int idxCaractere = 0; idxCaractere < registroTmp.length; idxCaractere++) {
                    c = registroTmp[idxCaractere];
                    if (c == SEPARADOR_DE_CAMPOS_DO_REGISTRO.charAt(0)) {
                        campoPossuiConteudo = (campoTmp.length() > 0 && campoTmp.toString().trim().length() > 0);
                        if (campoPossuiConteudo) {
                            String campoAnterior = null;
                            if (camposTmp.size() > 0) {
                                campoAnterior = camposTmp.get(camposTmp.size() - 1);
                            }
                            campoIniciaComEspacoEmBranco = campoTmp.toString().startsWith(" ");
                            if (campoAnterior != null && campoIniciaComEspacoEmBranco && registroComExcessoDeDelimitadores) {
                                camposTmp.set(camposTmp.size() - 1, (campoAnterior + campoTmp.toString()).trim());
                                houveMesclagemDeCampos = true;
                            } else {
                                camposTmp.add(campoTmp.toString().trim());
                            }
                        } else {
                            camposTmp.add(null);
                        }
                        campoTmp.setLength(0);
                    } else {
                        campoTmp.append(c);
                    }
                }
                if (registro.endsWith(SEPARADOR_DE_CAMPOS_DO_REGISTRO)) {
                    camposTmp.add(null);
                }
                if (houveMesclagemDeCampos && camposTmp.size() < QTDE_CAMPOS) {
                    camposTmp.add(CampoDoArquivoDasEmpresasAbertas.COMPLEMENTO.ordinal(), null);
                }
                campos = camposTmp.toArray(new String[camposTmp.size()]);
                int quantidadeDeCamposEncontradosIncluindoOsVazios = campos.length;
                if (quantidadeDeCamposEncontradosIncluindoOsVazios != QTDE_CAMPOS) {
                    throw new CampoMalDelimitadoEmRegistroDoArquivoImportado(registro);
                }
                vCODIGO_CVM = Integer.parseInt(campos[CampoDoArquivoDasEmpresasAbertas.CODIGO_CVM.ordinal()]);
                vDENOMINACAO_SOCIAL = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_SOCIAL.ordinal()];
                vDENOMINACAO_COMERCIAL = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_COMERCIAL.ordinal()];
                vLOGRADOURO = campos[CampoDoArquivoDasEmpresasAbertas.LOGRADOURO.ordinal()];
                vCOMPLEMENTO = campos[CampoDoArquivoDasEmpresasAbertas.COMPLEMENTO.ordinal()];
                vBAIRRO = campos[CampoDoArquivoDasEmpresasAbertas.BAIRRO.ordinal()];
                String cepTmp = campos[CampoDoArquivoDasEmpresasAbertas.CEP.ordinal()];
                if (cepTmp != null && cepTmp.trim().length() > 0) {
                    vCEP = new BigDecimal(cepTmp);
                } else {
                    vCEP = null;
                }
                vMUNICIPIO = campos[CampoDoArquivoDasEmpresasAbertas.MUNICIPIO.ordinal()];
                vUF = campos[CampoDoArquivoDasEmpresasAbertas.UF.ordinal()];
                String dddTmp = campos[CampoDoArquivoDasEmpresasAbertas.DDD.ordinal()], foneTmp = campos[CampoDoArquivoDasEmpresasAbertas.TELEFONE.ordinal()], dddFone = "";
                if (dddTmp != null && dddTmp.trim().length() > 0) {
                    dddFone = dddFone + dddTmp;
                }
                if (foneTmp != null && foneTmp.trim().length() > 0) {
                    dddFone = dddFone + foneTmp;
                }
                if (dddFone != null && dddFone.trim().length() > 0) {
                    dddFone = new BigDecimal(dddFone).toString();
                    if (dddFone.length() > 10 && dddFone.endsWith("0")) {
                        dddFone = dddFone.substring(0, 10);
                    }
                    vDDD = new BigDecimal(dddFone.substring(0, 2));
                    vTELEFONE = new BigDecimal(dddFone.substring(2));
                } else {
                    vDDD = null;
                    vTELEFONE = null;
                }
                String faxTmp = campos[CampoDoArquivoDasEmpresasAbertas.FAX.ordinal()];
                if (faxTmp != null && faxTmp.trim().length() > 0) {
                    vFAX = new BigDecimal(faxTmp);
                } else {
                    vFAX = null;
                }
                vDENOMINACAO_ANTERIOR = campos[CampoDoArquivoDasEmpresasAbertas.DENOMINACAO_ANTERIOR.ordinal()];
                vSETOR_ATIVIDADE = campos[CampoDoArquivoDasEmpresasAbertas.SETOR_ATIVIDADE.ordinal()];
                String cnpjTmp = campos[CampoDoArquivoDasEmpresasAbertas.CNPJ.ordinal()];
                if (cnpjTmp != null && cnpjTmp.trim().length() > 0) {
                    vCNPJ = new BigDecimal(cnpjTmp);
                } else {
                    vCNPJ = null;
                }
                vDRI = campos[CampoDoArquivoDasEmpresasAbertas.DRI.ordinal()];
                vAUDITOR = campos[CampoDoArquivoDasEmpresasAbertas.AUDITOR.ordinal()];
                String qtdeAcoesON = campos[CampoDoArquivoDasEmpresasAbertas.QUANT_DE_ACOES_ORDINARIAS.ordinal()];
                if (qtdeAcoesON != null && qtdeAcoesON.trim().length() > 0) {
                    vQUANT_DE_ACOES_ORDINARIAS = new BigDecimal(qtdeAcoesON);
                } else {
                    vQUANT_DE_ACOES_ORDINARIAS = null;
                }
                String qtdeAcoesPN = campos[CampoDoArquivoDasEmpresasAbertas.QUANT_DE_ACOES_PREF.ordinal()];
                if (qtdeAcoesPN != null && qtdeAcoesPN.trim().length() > 0) {
                    vQUANT_DE_ACOES_PREF = new BigDecimal(qtdeAcoesPN);
                } else {
                    vQUANT_DE_ACOES_PREF = null;
                }
                vSITUACAO = campos[CampoDoArquivoDasEmpresasAbertas.SITUACAO.ordinal()];
                String dataDaSituacaoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_DA_SITUACAO.ordinal()];
                String[] partesDaData = dataDaSituacaoTmp.trim().split("/");
                int dia = Integer.parseInt(partesDaData[0]), mes = Integer.parseInt(partesDaData[1]) - 1, ano = Integer.parseInt(partesDaData[2]);
                Calendar calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(ano, mes, dia);
                vDATA_DA_SITUACAO = new java.sql.Date(calendario.getTimeInMillis());
                vTIPO_PAPEL1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL1.ordinal()];
                vTIPO_PAPEL2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL2.ordinal()];
                vTIPO_PAPEL3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL3.ordinal()];
                vTIPO_PAPEL4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL4.ordinal()];
                vTIPO_PAPEL5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL5.ordinal()];
                vTIPO_PAPEL6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_PAPEL6.ordinal()];
                vCONTROLE_ACIONARIO = campos[CampoDoArquivoDasEmpresasAbertas.CONTROLE_ACIONARIO.ordinal()];
                String dataDeRegistroTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_DE_REGISTRO.ordinal()];
                partesDaData = dataDeRegistroTmp.trim().split("/");
                dia = Integer.parseInt(partesDaData[0]);
                mes = Integer.parseInt(partesDaData[1]) - 1;
                ano = Integer.parseInt(partesDaData[2]);
                calendario = Calendar.getInstance();
                calendario.clear();
                calendario.set(ano, mes, dia);
                vDATA_DE_REGISTRO = new java.sql.Date(calendario.getTimeInMillis());
                String dataDoCancelamentoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_DO_CANCELAMENTO.ordinal()];
                if (dataDoCancelamentoTmp != null && dataDoCancelamentoTmp.trim().length() > 0) {
                    partesDaData = dataDoCancelamentoTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_DO_CANCELAMENTO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_DO_CANCELAMENTO = null;
                }
                vMERCADO = campos[CampoDoArquivoDasEmpresasAbertas.MERCADO.ordinal()];
                vBOLSA1 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA1.ordinal()];
                vBOLSA2 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA2.ordinal()];
                vBOLSA3 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA3.ordinal()];
                vBOLSA4 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA4.ordinal()];
                vBOLSA5 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA5.ordinal()];
                vBOLSA6 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA6.ordinal()];
                vBOLSA7 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA7.ordinal()];
                vBOLSA8 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA8.ordinal()];
                vBOLSA9 = campos[CampoDoArquivoDasEmpresasAbertas.BOLSA9.ordinal()];
                vMOTIVO_DO_CANCELAMENTO = campos[CampoDoArquivoDasEmpresasAbertas.MOTIVO_DO_CANCELAMENTO.ordinal()];
                String patrimonioLiquidoTmp = campos[CampoDoArquivoDasEmpresasAbertas.PATRIMONIO_LIQUIDO.ordinal()];
                if (patrimonioLiquidoTmp != null && patrimonioLiquidoTmp.trim().length() > 0) {
                    vPATRIMONIO_LIQUIDO = new BigDecimal(patrimonioLiquidoTmp);
                } else {
                    vPATRIMONIO_LIQUIDO = null;
                }
                String dataDoPatrimonioTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_DO_PATRIMONIO.ordinal()];
                if (dataDoPatrimonioTmp != null && dataDoPatrimonioTmp.trim().length() > 0) {
                    partesDaData = dataDoPatrimonioTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_DO_PATRIMONIO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_DO_PATRIMONIO = null;
                }
                vE_MAIL = campos[CampoDoArquivoDasEmpresasAbertas.E_MAIL.ordinal()];
                vNOME_SETOR_ATIVIDADE = campos[CampoDoArquivoDasEmpresasAbertas.NOME_SETOR_ATIVIDADE.ordinal()];
                String dataDaAcaoTmp = campos[CampoDoArquivoDasEmpresasAbertas.DATA_DA_ACAO.ordinal()];
                if (dataDaAcaoTmp != null && dataDaAcaoTmp.trim().length() > 0) {
                    partesDaData = dataDaAcaoTmp.trim().split("/");
                    dia = Integer.parseInt(partesDaData[0]);
                    mes = Integer.parseInt(partesDaData[1]) - 1;
                    ano = Integer.parseInt(partesDaData[2]);
                    calendario = Calendar.getInstance();
                    calendario.clear();
                    calendario.set(ano, mes, dia);
                    vDATA_DA_ACAO = new java.sql.Date(calendario.getTimeInMillis());
                } else {
                    vDATA_DA_ACAO = null;
                }
                vTIPO_NEGOCIO1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO1.ordinal()];
                vTIPO_NEGOCIO2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO2.ordinal()];
                vTIPO_NEGOCIO3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO3.ordinal()];
                vTIPO_NEGOCIO4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO4.ordinal()];
                vTIPO_NEGOCIO5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO5.ordinal()];
                vTIPO_NEGOCIO6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_NEGOCIO6.ordinal()];
                vTIPO_MERCADO1 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO1.ordinal()];
                vTIPO_MERCADO2 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO2.ordinal()];
                vTIPO_MERCADO3 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO3.ordinal()];
                vTIPO_MERCADO4 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO4.ordinal()];
                vTIPO_MERCADO5 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO5.ordinal()];
                vTIPO_MERCADO6 = campos[CampoDoArquivoDasEmpresasAbertas.TIPO_MERCADO6.ordinal()];
                stmtDestino.setIntAtName("CODIGO_CVM", vCODIGO_CVM);
                stmtDestino.setStringAtName("DENOMINACAO_SOCIAL", vDENOMINACAO_SOCIAL);
                stmtDestino.setStringAtName("DENOMINACAO_COMERCIAL", vDENOMINACAO_COMERCIAL);
                stmtDestino.setStringAtName("LOGRADOURO", vLOGRADOURO);
                stmtDestino.setStringAtName("COMPLEMENTO", vCOMPLEMENTO);
                stmtDestino.setStringAtName("BAIRRO", vBAIRRO);
                stmtDestino.setBigDecimalAtName("CEP", vCEP);
                stmtDestino.setStringAtName("MUNICIPIO", vMUNICIPIO);
                stmtDestino.setStringAtName("UF", vUF);
                stmtDestino.setBigDecimalAtName("DDD", vDDD);
                stmtDestino.setBigDecimalAtName("TELEFONE", vTELEFONE);
                stmtDestino.setBigDecimalAtName("FAX", vFAX);
                stmtDestino.setStringAtName("DENOMINACAO_ANTERIOR", vDENOMINACAO_ANTERIOR);
                stmtDestino.setStringAtName("SETOR_ATIVIDADE", vSETOR_ATIVIDADE);
                stmtDestino.setBigDecimalAtName("CNPJ", vCNPJ);
                stmtDestino.setStringAtName("DRI", vDRI);
                stmtDestino.setStringAtName("AUDITOR", vAUDITOR);
                stmtDestino.setBigDecimalAtName("QUANT_DE_ACOES_ORDINARIAS", vQUANT_DE_ACOES_ORDINARIAS);
                stmtDestino.setBigDecimalAtName("QUANT_DE_ACOES_PREF", vQUANT_DE_ACOES_PREF);
                stmtDestino.setStringAtName("SITUACAO", vSITUACAO);
                stmtDestino.setDateAtName("DATA_DA_SITUACAO", vDATA_DA_SITUACAO);
                stmtDestino.setStringAtName("TIPO_PAPEL1", vTIPO_PAPEL1);
                stmtDestino.setStringAtName("TIPO_PAPEL2", vTIPO_PAPEL2);
                stmtDestino.setStringAtName("TIPO_PAPEL3", vTIPO_PAPEL3);
                stmtDestino.setStringAtName("TIPO_PAPEL4", vTIPO_PAPEL4);
                stmtDestino.setStringAtName("TIPO_PAPEL5", vTIPO_PAPEL5);
                stmtDestino.setStringAtName("TIPO_PAPEL6", vTIPO_PAPEL6);
                stmtDestino.setStringAtName("CONTROLE_ACIONARIO", vCONTROLE_ACIONARIO);
                stmtDestino.setDateAtName("DATA_DE_REGISTRO", vDATA_DE_REGISTRO);
                stmtDestino.setDateAtName("DATA_DO_CANCELAMENTO", vDATA_DO_CANCELAMENTO);
                stmtDestino.setStringAtName("MERCADO", vMERCADO);
                stmtDestino.setStringAtName("BOLSA1", vBOLSA1);
                stmtDestino.setStringAtName("BOLSA2", vBOLSA2);
                stmtDestino.setStringAtName("BOLSA3", vBOLSA3);
                stmtDestino.setStringAtName("BOLSA4", vBOLSA4);
                stmtDestino.setStringAtName("BOLSA5", vBOLSA5);
                stmtDestino.setStringAtName("BOLSA6", vBOLSA6);
                stmtDestino.setStringAtName("BOLSA7", vBOLSA7);
                stmtDestino.setStringAtName("BOLSA8", vBOLSA8);
                stmtDestino.setStringAtName("BOLSA9", vBOLSA9);
                stmtDestino.setStringAtName("MOTIVO_DO_CANCELAMENTO", vMOTIVO_DO_CANCELAMENTO);
                stmtDestino.setBigDecimalAtName("PATRIMONIO_LIQUIDO", vPATRIMONIO_LIQUIDO);
                stmtDestino.setDateAtName("DATA_DO_PATRIMONIO", vDATA_DO_PATRIMONIO);
                stmtDestino.setStringAtName("E_MAIL", vE_MAIL);
                stmtDestino.setStringAtName("NOME_SETOR_ATIVIDADE", vNOME_SETOR_ATIVIDADE);
                stmtDestino.setDateAtName("DATA_DA_ACAO", vDATA_DA_ACAO);
                stmtDestino.setStringAtName("TIPO_NEGOCIO1", vTIPO_NEGOCIO1);
                stmtDestino.setStringAtName("TIPO_NEGOCIO2", vTIPO_NEGOCIO2);
                stmtDestino.setStringAtName("TIPO_NEGOCIO3", vTIPO_NEGOCIO3);
                stmtDestino.setStringAtName("TIPO_NEGOCIO4", vTIPO_NEGOCIO4);
                stmtDestino.setStringAtName("TIPO_NEGOCIO5", vTIPO_NEGOCIO5);
                stmtDestino.setStringAtName("TIPO_NEGOCIO6", vTIPO_NEGOCIO6);
                stmtDestino.setStringAtName("TIPO_MERCADO1", vTIPO_MERCADO1);
                stmtDestino.setStringAtName("TIPO_MERCADO2", vTIPO_MERCADO2);
                stmtDestino.setStringAtName("TIPO_MERCADO3", vTIPO_MERCADO3);
                stmtDestino.setStringAtName("TIPO_MERCADO4", vTIPO_MERCADO4);
                stmtDestino.setStringAtName("TIPO_MERCADO5", vTIPO_MERCADO5);
                stmtDestino.setStringAtName("TIPO_MERCADO6", vTIPO_MERCADO6);
                int contagemDasInsercoes = stmtDestino.executeUpdate();
                quantidadeDeRegistrosImportada++;
                double percentualCompleto = (double) quantidadeDeRegistrosImportada / quantidadeDeRegistrosEstimada * 100;
                pAndamento.setPercentualCompleto((int) percentualCompleto);
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoTXT.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = numeroDoRegistro;
            problemaDetalhado.detalhesSobreOProblema = ex;
            throw problemaDetalhado;
        } finally {
            pAndamento.setPercentualCompleto(100);
            in.close();
            if (stmtLimpezaInicialDestino != null && (!stmtLimpezaInicialDestino.isClosed())) {
                stmtLimpezaInicialDestino.close();
            }
            if (stmtDestino != null && (!stmtDestino.isClosed())) {
                stmtDestino.close();
            }
        }
    }
