    public void importarHistoricoDeCotacoesDosPapeis(File[] pArquivosTXT, boolean pApagarDadosImportadosAnteriormente, Andamento pAndamento) throws FileNotFoundException, SQLException {
        if (pApagarDadosImportadosAnteriormente) {
            Statement stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_TB_COTACAO_AVISTA_LOTE_PDR";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "TRUNCATE TABLE TMP_TB_COTACAO_OUTROS_MERCADOS";
            stmtLimpezaInicialDestino.executeUpdate(sql);
        }
        final int TAMANHO_DO_REGISTRO = 245;
        long TAMANHO_DOS_METADADOS_DO_ARQUIVO = 2 * TAMANHO_DO_REGISTRO;
        long tamanhoDosArquivos = 0;
        for (File arquivoTXT : pArquivosTXT) {
            long tamanhoDoArquivo = arquivoTXT.length();
            tamanhoDosArquivos += tamanhoDoArquivo;
        }
        int quantidadeEstimadaDeRegistros = (int) ((tamanhoDosArquivos - (pArquivosTXT.length * TAMANHO_DOS_METADADOS_DO_ARQUIVO)) / TAMANHO_DO_REGISTRO);
        String sqlMercadoAVistaLotePadrao = "INSERT INTO TMP_TB_COTACAO_AVISTA_LOTE_PDR(DATA_PREGAO, CODBDI, CODNEG, TPMERC, NOMRES, ESPECI, PRAZOT, MODREF, PREABE, PREMAX, PREMIN, PREMED, PREULT, PREOFC, PREOFV, TOTNEG, QUATOT, VOLTOT, PREEXE, INDOPC, DATVEN, FATCOT, PTOEXE, CODISI, DISMES) VALUES(:DATA_PREGAO, :CODBDI, :CODNEG, :TPMERC, :NOMRES, :ESPECI, :PRAZOT, :MODREF, :PREABE, :PREMAX, :PREMIN, :PREMED, :PREULT, :PREOFC, :PREOFV, :TOTNEG, :QUATOT, :VOLTOT, :PREEXE, :INDOPC, :DATVEN, :FATCOT, :PTOEXE, :CODISI, :DISMES)";
        OraclePreparedStatement stmtDestinoMercadoAVistaLotePadrao = (OraclePreparedStatement) conDestino.prepareStatement(sqlMercadoAVistaLotePadrao);
        stmtDestinoMercadoAVistaLotePadrao.setExecuteBatch(COMANDOS_POR_LOTE);
        String sqlOutrosMercados = "INSERT INTO TMP_TB_COTACAO_OUTROS_MERCADOS(DATA_PREGAO, CODBDI, CODNEG, TPMERC, NOMRES, ESPECI, PRAZOT, MODREF, PREABE, PREMAX, PREMIN, PREMED, PREULT, PREOFC, PREOFV, TOTNEG, QUATOT, VOLTOT, PREEXE, INDOPC, DATVEN, FATCOT, PTOEXE, CODISI, DISMES) VALUES(:DATA_PREGAO, :CODBDI, :CODNEG, :TPMERC, :NOMRES, :ESPECI, :PRAZOT, :MODREF, :PREABE, :PREMAX, :PREMIN, :PREMED, :PREULT, :PREOFC, :PREOFV, :TOTNEG, :QUATOT, :VOLTOT, :PREEXE, :INDOPC, :DATVEN, :FATCOT, :PTOEXE, :CODISI, :DISMES)";
        OraclePreparedStatement stmtDestinoOutrosMercados = (OraclePreparedStatement) conDestino.prepareStatement(sqlOutrosMercados);
        stmtDestinoOutrosMercados.setExecuteBatch(COMANDOS_POR_LOTE);
        int quantidadeDeRegistrosImportadosDosArquivos = 0;
        Scanner in = null;
        int numeroDoRegistro = -1;
        try {
            for (File arquivoTXT : pArquivosTXT) {
                int quantidadeDeRegistrosImportadosDoArquivoAtual = 0;
                int vDATA_PREGAO;
                try {
                    in = new Scanner(new FileInputStream(arquivoTXT), Constantes.CONJUNTO_DE_CARACTERES_DOS_ARQUIVOS_TEXTO_DA_BOVESPA.name());
                    String registro;
                    numeroDoRegistro = 0;
                    while (in.hasNextLine()) {
                        ++numeroDoRegistro;
                        registro = in.nextLine();
                        if (registro.length() != TAMANHO_DO_REGISTRO) throw new ProblemaNaImportacaoDeArquivo();
                        if (registro.startsWith("01")) {
                            stmtDestinoMercadoAVistaLotePadrao.clearParameters();
                            stmtDestinoOutrosMercados.clearParameters();
                            vDATA_PREGAO = Integer.parseInt(registro.substring(2, 10).trim());
                            int vCODBDI = Integer.parseInt(registro.substring(10, 12).trim());
                            String vCODNEG = registro.substring(12, 24).trim();
                            int vTPMERC = Integer.parseInt(registro.substring(24, 27).trim());
                            String vNOMRES = registro.substring(27, 39).trim();
                            String vESPECI = registro.substring(39, 49).trim();
                            String vPRAZOT = registro.substring(49, 52).trim();
                            String vMODREF = registro.substring(52, 56).trim();
                            BigDecimal vPREABE = obterBigDecimal(registro.substring(56, 69).trim(), 13, 2);
                            BigDecimal vPREMAX = obterBigDecimal(registro.substring(69, 82).trim(), 13, 2);
                            BigDecimal vPREMIN = obterBigDecimal(registro.substring(82, 95).trim(), 13, 2);
                            BigDecimal vPREMED = obterBigDecimal(registro.substring(95, 108).trim(), 13, 2);
                            BigDecimal vPREULT = obterBigDecimal(registro.substring(108, 121).trim(), 13, 2);
                            BigDecimal vPREOFC = obterBigDecimal(registro.substring(121, 134).trim(), 13, 2);
                            BigDecimal vPREOFV = obterBigDecimal(registro.substring(134, 147).trim(), 13, 2);
                            int vTOTNEG = Integer.parseInt(registro.substring(147, 152).trim());
                            BigDecimal vQUATOT = new BigDecimal(registro.substring(152, 170).trim());
                            BigDecimal vVOLTOT = obterBigDecimal(registro.substring(170, 188).trim(), 18, 2);
                            BigDecimal vPREEXE = obterBigDecimal(registro.substring(188, 201).trim(), 13, 2);
                            int vINDOPC = Integer.parseInt(registro.substring(201, 202).trim());
                            int vDATVEN = Integer.parseInt(registro.substring(202, 210).trim());
                            int vFATCOT = Integer.parseInt(registro.substring(210, 217).trim());
                            BigDecimal vPTOEXE = obterBigDecimal(registro.substring(217, 230).trim(), 13, 6);
                            String vCODISI = registro.substring(230, 242).trim();
                            int vDISMES = Integer.parseInt(registro.substring(242, 245).trim());
                            boolean mercadoAVistaLotePadrao = (vTPMERC == 10 && vCODBDI == 2);
                            OraclePreparedStatement stmtDestino;
                            if (mercadoAVistaLotePadrao) {
                                stmtDestino = stmtDestinoMercadoAVistaLotePadrao;
                            } else {
                                stmtDestino = stmtDestinoOutrosMercados;
                            }
                            stmtDestino.setIntAtName("DATA_PREGAO", vDATA_PREGAO);
                            stmtDestino.setIntAtName("CODBDI", vCODBDI);
                            stmtDestino.setStringAtName("CODNEG", vCODNEG);
                            stmtDestino.setIntAtName("TPMERC", vTPMERC);
                            stmtDestino.setStringAtName("NOMRES", vNOMRES);
                            stmtDestino.setStringAtName("ESPECI", vESPECI);
                            stmtDestino.setStringAtName("PRAZOT", vPRAZOT);
                            stmtDestino.setStringAtName("MODREF", vMODREF);
                            stmtDestino.setBigDecimalAtName("PREABE", vPREABE);
                            stmtDestino.setBigDecimalAtName("PREMAX", vPREMAX);
                            stmtDestino.setBigDecimalAtName("PREMIN", vPREMIN);
                            stmtDestino.setBigDecimalAtName("PREMED", vPREMED);
                            stmtDestino.setBigDecimalAtName("PREULT", vPREULT);
                            stmtDestino.setBigDecimalAtName("PREOFC", vPREOFC);
                            stmtDestino.setBigDecimalAtName("PREOFV", vPREOFV);
                            stmtDestino.setIntAtName("TOTNEG", vTOTNEG);
                            stmtDestino.setBigDecimalAtName("QUATOT", vQUATOT);
                            stmtDestino.setBigDecimalAtName("VOLTOT", vVOLTOT);
                            stmtDestino.setBigDecimalAtName("PREEXE", vPREEXE);
                            stmtDestino.setIntAtName("INDOPC", vINDOPC);
                            stmtDestino.setIntAtName("DATVEN", vDATVEN);
                            stmtDestino.setIntAtName("FATCOT", vFATCOT);
                            stmtDestino.setBigDecimalAtName("PTOEXE", vPTOEXE);
                            stmtDestino.setStringAtName("CODISI", vCODISI);
                            stmtDestino.setIntAtName("DISMES", vDISMES);
                            int contagemDasInsercoes = stmtDestino.executeUpdate();
                            quantidadeDeRegistrosImportadosDoArquivoAtual++;
                            quantidadeDeRegistrosImportadosDosArquivos++;
                        } else if (registro.startsWith("99")) {
                            BigDecimal totalDeRegistros = obterBigDecimal(registro.substring(31, 42).trim(), 11, 0);
                            assert (totalDeRegistros.intValue() - 2) == quantidadeDeRegistrosImportadosDoArquivoAtual : "Quantidade de registros divergente";
                            break;
                        }
                        double percentualCompleto = (double) quantidadeDeRegistrosImportadosDosArquivos / quantidadeEstimadaDeRegistros * 100;
                        pAndamento.setPercentualCompleto((int) percentualCompleto);
                    }
                    conDestino.commit();
                } catch (Exception ex) {
                    conDestino.rollback();
                    ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
                    problemaDetalhado.nomeDoArquivo = arquivoTXT.getName();
                    problemaDetalhado.linhaProblematicaDoArquivo = numeroDoRegistro;
                    problemaDetalhado.detalhesSobreOProblema = ex;
                    throw problemaDetalhado;
                } finally {
                    in.close();
                }
            }
        } finally {
            pAndamento.setPercentualCompleto(100);
            stmtDestinoMercadoAVistaLotePadrao.close();
            stmtDestinoOutrosMercados.close();
        }
    }
