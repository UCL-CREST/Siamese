    public void importarHistoricoDeProventos(File pArquivoXLS, boolean pFiltrarPelaDataDeCorteDoCabecalho, Andamento pAndamento) throws IOException, SQLException, InvalidFormatException {
        int iLinha = -1;
        String nomeDaColuna = "";
        Statement stmtLimpezaInicialDestino = null;
        OraclePreparedStatement stmtDestino = null;
        try {
            Workbook arquivo = WorkbookFactory.create(new FileInputStream(pArquivoXLS));
            Sheet plan1 = arquivo.getSheetAt(0);
            int QUANTIDADE_DE_REGISTROS_DE_METADADOS = 2;
            int quantidadeDeRegistrosEstimada = plan1.getPhysicalNumberOfRows() - QUANTIDADE_DE_REGISTROS_DE_METADADOS;
            String vNomeDePregao, vTipoDaAcao, vDataDaAprovacao, vTipoDoProvento, vDataDoUltimoPrecoCom;
            BigDecimal vValorDoProvento, vUltimoPrecoCom, vProventoPorPreco;
            int vProventoPor1Ou1000Acoes, vPrecoPor1Ou1000Acoes;
            java.sql.Date vUltimoDiaCom;
            DateFormat formatadorData = new SimpleDateFormat("yyyyMMdd");
            DateFormat formatadorPadraoData = DateFormat.getDateInstance();
            Row registro;
            Cell celula;
            java.util.Date dataLimite = plan1.getRow(0).getCell(CampoDaPlanilhaDosProventosEmDinheiro.NOME_DE_PREGAO.ordinal()).getDateCellValue();
            Cell celulaUltimoDiaCom;
            java.util.Date tmpUltimoDiaCom;
            stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_TB_PROVENTO_EM_DINHEIRO";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "INSERT INTO TMP_TB_PROVENTO_EM_DINHEIRO(NOME_DE_PREGAO, TIPO_DA_ACAO, DATA_DA_APROVACAO, VALOR_DO_PROVENTO, PROVENTO_POR_1_OU_1000_ACOES, TIPO_DO_PROVENTO, ULTIMO_DIA_COM, DATA_DO_ULTIMO_PRECO_COM, ULTIMO_PRECO_COM, PRECO_POR_1_OU_1000_ACOES, PERC_PROVENTO_POR_PRECO) VALUES(:NOME_DE_PREGAO, :TIPO_DA_ACAO, :DATA_DA_APROVACAO, :VALOR_DO_PROVENTO, :PROVENTO_POR_1_OU_1000_ACOES, :TIPO_DO_PROVENTO, :ULTIMO_DIA_COM, :DATA_DO_ULTIMO_PRECO_COM, :ULTIMO_PRECO_COM, :PRECO_POR_1_OU_1000_ACOES, :PERC_PROVENTO_POR_PRECO)";
            stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_POR_LOTE);
            int quantidadeDeRegistrosImportados = 0;
            final int NUMERO_DA_LINHA_INICIAL = 1;
            for (iLinha = NUMERO_DA_LINHA_INICIAL; true; iLinha++) {
                registro = plan1.getRow(iLinha);
                if (registro != null) {
                    nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_DIA_COM.toString();
                    celulaUltimoDiaCom = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_DIA_COM.ordinal());
                    if (celulaUltimoDiaCom != null) {
                        if (celulaUltimoDiaCom.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            tmpUltimoDiaCom = celulaUltimoDiaCom.getDateCellValue();
                            if (tmpUltimoDiaCom.compareTo(dataLimite) <= 0 || !pFiltrarPelaDataDeCorteDoCabecalho) {
                                vUltimoDiaCom = new java.sql.Date(celulaUltimoDiaCom.getDateCellValue().getTime());
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.NOME_DE_PREGAO.toString();
                                vNomeDePregao = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.NOME_DE_PREGAO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.TIPO_DA_ACAO.toString();
                                vTipoDaAcao = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.TIPO_DA_ACAO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.DATA_DA_APROVACAO.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.DATA_DA_APROVACAO.ordinal());
                                try {
                                    java.util.Date tmpDataDaAprovacao;
                                    if (celula.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                        tmpDataDaAprovacao = celula.getDateCellValue();
                                    } else {
                                        tmpDataDaAprovacao = formatadorPadraoData.parse(celula.getStringCellValue());
                                    }
                                    vDataDaAprovacao = formatadorData.format(tmpDataDaAprovacao);
                                } catch (ParseException ex) {
                                    vDataDaAprovacao = celula.getStringCellValue();
                                }
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.VALOR_DO_PROVENTO.toString();
                                vValorDoProvento = new BigDecimal(String.valueOf(registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.VALOR_DO_PROVENTO.ordinal()).getNumericCellValue()));
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_POR_1_OU_1000_ACOES.toString();
                                vProventoPor1Ou1000Acoes = (int) registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_POR_1_OU_1000_ACOES.ordinal()).getNumericCellValue();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.TIPO_DO_PROVENTO.toString();
                                vTipoDoProvento = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.TIPO_DO_PROVENTO.ordinal()).getStringCellValue().trim();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.DATA_DO_ULTIMO_PRECO_COM.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.DATA_DO_ULTIMO_PRECO_COM.ordinal());
                                if (celula != null) {
                                    try {
                                        java.util.Date tmpDataDoUltimoPrecoCom;
                                        if (celula.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                            tmpDataDoUltimoPrecoCom = celula.getDateCellValue();
                                        } else {
                                            tmpDataDoUltimoPrecoCom = formatadorPadraoData.parse(celula.getStringCellValue());
                                        }
                                        vDataDoUltimoPrecoCom = formatadorData.format(tmpDataDoUltimoPrecoCom);
                                    } catch (ParseException ex) {
                                        vDataDoUltimoPrecoCom = celula.getStringCellValue().trim();
                                    }
                                } else {
                                    vDataDoUltimoPrecoCom = "";
                                }
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_PRECO_COM.toString();
                                vUltimoPrecoCom = new BigDecimal(String.valueOf(registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.ULTIMO_PRECO_COM.ordinal()).getNumericCellValue()));
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PRECO_POR_1_OU_1000_ACOES.toString();
                                vPrecoPor1Ou1000Acoes = (int) registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PRECO_POR_1_OU_1000_ACOES.ordinal()).getNumericCellValue();
                                nomeDaColuna = CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_POR_PRECO.toString();
                                celula = registro.getCell(CampoDaPlanilhaDosProventosEmDinheiro.PROVENTO_POR_PRECO.ordinal());
                                if (celula != null && celula.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    vProventoPorPreco = new BigDecimal(String.valueOf(celula.getNumericCellValue()));
                                } else {
                                    vProventoPorPreco = null;
                                }
                                stmtDestino.clearParameters();
                                stmtDestino.setStringAtName("NOME_DE_PREGAO", vNomeDePregao);
                                stmtDestino.setStringAtName("TIPO_DA_ACAO", vTipoDaAcao);
                                stmtDestino.setStringAtName("DATA_DA_APROVACAO", vDataDaAprovacao);
                                stmtDestino.setBigDecimalAtName("VALOR_DO_PROVENTO", vValorDoProvento);
                                stmtDestino.setIntAtName("PROVENTO_POR_1_OU_1000_ACOES", vProventoPor1Ou1000Acoes);
                                stmtDestino.setStringAtName("TIPO_DO_PROVENTO", vTipoDoProvento);
                                stmtDestino.setDateAtName("ULTIMO_DIA_COM", vUltimoDiaCom);
                                stmtDestino.setStringAtName("DATA_DO_ULTIMO_PRECO_COM", vDataDoUltimoPrecoCom);
                                stmtDestino.setBigDecimalAtName("ULTIMO_PRECO_COM", vUltimoPrecoCom);
                                stmtDestino.setIntAtName("PRECO_POR_1_OU_1000_ACOES", vPrecoPor1Ou1000Acoes);
                                stmtDestino.setBigDecimalAtName("PERC_PROVENTO_POR_PRECO", vProventoPorPreco);
                                int contagemDasInsercoes = stmtDestino.executeUpdate();
                                quantidadeDeRegistrosImportados++;
                            }
                        }
                    } else {
                        break;
                    }
                    double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                    pAndamento.setPercentualCompleto((int) percentualCompleto);
                } else {
                    break;
                }
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoXLS.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = iLinha + 1;
            problemaDetalhado.colunaProblematicaDoArquivo = nomeDaColuna;
            problemaDetalhado.detalhesSobreOProblema = ex;
            throw problemaDetalhado;
        } finally {
            pAndamento.setPercentualCompleto(100);
            if (stmtLimpezaInicialDestino != null && (!stmtLimpezaInicialDestino.isClosed())) {
                stmtLimpezaInicialDestino.close();
            }
            if (stmtDestino != null && (!stmtDestino.isClosed())) {
                stmtDestino.close();
            }
        }
    }
