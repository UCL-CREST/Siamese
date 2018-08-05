    public void importarBancoDeDadosDARI(File pArquivoXLS, Andamento pAndamento) throws IOException, SQLException, InvalidFormatException {
        final String ABA_VALOR_DE_MERCADO = "Valor de Mercado";
        final int COLUNA_DATA = 1, COLUNA_ANO = 6, COLUNA_VALOR_DE_MERCADO_DIARIO_EM_BILHOES_DE_REAIS = 2, COLUNA_VALOR_DE_MERCADO_DIARIO_EM_BILHOES_DE_DOLARES = 3, COLUNA_VALOR_DE_MERCADO_ANUAL_EM_BILHOES_DE_REAIS = 7, COLUNA_VALOR_DE_MERCADO_ANUAL_EM_BILHOES_DE_DOLARES = 8;
        final BigDecimal BILHAO = new BigDecimal("1000000000");
        int iLinha = -1;
        Statement stmtLimpezaInicialDestino = null;
        OraclePreparedStatement stmtDestino = null;
        try {
            Workbook arquivo = WorkbookFactory.create(new FileInputStream(pArquivoXLS));
            Sheet planilhaValorDeMercado = arquivo.getSheet(ABA_VALOR_DE_MERCADO);
            int QUANTIDADE_DE_REGISTROS_DE_METADADOS = 7;
            final Calendar DATA_INICIAL = Calendar.getInstance();
            DATA_INICIAL.setTime(planilhaValorDeMercado.getRow(QUANTIDADE_DE_REGISTROS_DE_METADADOS).getCell(COLUNA_DATA).getDateCellValue());
            final int ANO_DA_DATA_INICIAL = DATA_INICIAL.get(Calendar.YEAR);
            final int ANO_INICIAL = Integer.parseInt(planilhaValorDeMercado.getRow(QUANTIDADE_DE_REGISTROS_DE_METADADOS).getCell(COLUNA_ANO).getStringCellValue());
            final int ANO_FINAL = Calendar.getInstance().get(Calendar.YEAR);
            Row registro;
            int quantidadeDeRegistrosAnuaisEstimada = (ANO_FINAL - ANO_INICIAL + 1), quantidadeDeRegistrosDiariosEstimada = (planilhaValorDeMercado.getPhysicalNumberOfRows() - QUANTIDADE_DE_REGISTROS_DE_METADADOS);
            final int quantidadeDeRegistrosEstimada = quantidadeDeRegistrosAnuaisEstimada + quantidadeDeRegistrosDiariosEstimada;
            int vAno;
            BigDecimal vValorDeMercadoEmReais, vValorDeMercadoEmDolares;
            Cell celulaDoAno, celulaDoValorDeMercadoEmReais, celulaDoValorDeMercadoEmDolares;
            stmtLimpezaInicialDestino = conDestino.createStatement();
            String sql = "TRUNCATE TABLE TMP_TB_VALOR_MERCADO_BOLSA";
            stmtLimpezaInicialDestino.executeUpdate(sql);
            sql = "INSERT INTO TMP_TB_VALOR_MERCADO_BOLSA(DATA, VALOR_DE_MERCADO_REAL, VALOR_DE_MERCADO_DOLAR) VALUES(:DATA, :VALOR_DE_MERCADO_REAL, :VALOR_DE_MERCADO_DOLAR)";
            stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_POR_LOTE);
            int quantidadeDeRegistrosImportados = 0;
            Calendar calendario = Calendar.getInstance();
            calendario.clear();
            calendario.set(Calendar.MONTH, Calendar.DECEMBER);
            calendario.set(Calendar.DAY_OF_MONTH, 31);
            for (iLinha = QUANTIDADE_DE_REGISTROS_DE_METADADOS; true; iLinha++) {
                registro = planilhaValorDeMercado.getRow(iLinha);
                celulaDoAno = registro.getCell(COLUNA_ANO);
                String anoTmp = celulaDoAno.getStringCellValue();
                if (anoTmp != null && anoTmp.length() > 0) {
                    vAno = Integer.parseInt(anoTmp);
                    if (vAno < ANO_DA_DATA_INICIAL) {
                        celulaDoValorDeMercadoEmReais = registro.getCell(COLUNA_VALOR_DE_MERCADO_ANUAL_EM_BILHOES_DE_REAIS);
                        celulaDoValorDeMercadoEmDolares = registro.getCell(COLUNA_VALOR_DE_MERCADO_ANUAL_EM_BILHOES_DE_DOLARES);
                    } else {
                        break;
                    }
                    calendario.set(Calendar.YEAR, vAno);
                    java.sql.Date vUltimoDiaDoAno = new java.sql.Date(calendario.getTimeInMillis());
                    vValorDeMercadoEmReais = new BigDecimal(celulaDoValorDeMercadoEmReais.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                    vValorDeMercadoEmDolares = new BigDecimal(celulaDoValorDeMercadoEmDolares.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                    stmtDestino.clearParameters();
                    stmtDestino.setDateAtName("DATA", vUltimoDiaDoAno);
                    stmtDestino.setBigDecimalAtName("VALOR_DE_MERCADO_REAL", vValorDeMercadoEmReais);
                    stmtDestino.setBigDecimalAtName("VALOR_DE_MERCADO_DOLAR", vValorDeMercadoEmDolares);
                    int contagemDasInsercoes = stmtDestino.executeUpdate();
                    quantidadeDeRegistrosImportados++;
                } else {
                    break;
                }
                double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                pAndamento.setPercentualCompleto((int) percentualCompleto);
            }
            java.util.Date dataAnterior = null;
            String dataTmp;
            final DateFormat formatadorDeData_ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy", Constantes.IDIOMA_PORTUGUES_BRASILEIRO);
            final DateFormat formatadorDeData_ddMMMyyyy = new SimpleDateFormat("dd/MMM/yyyy", Constantes.IDIOMA_PORTUGUES_BRASILEIRO);
            Cell celulaDaData;
            for (iLinha = QUANTIDADE_DE_REGISTROS_DE_METADADOS; true; iLinha++) {
                registro = planilhaValorDeMercado.getRow(iLinha);
                if (registro != null) {
                    celulaDaData = registro.getCell(COLUNA_DATA);
                    java.util.Date data;
                    if (celulaDaData.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        data = celulaDaData.getDateCellValue();
                    } else {
                        dataTmp = celulaDaData.getStringCellValue();
                        try {
                            data = formatadorDeData_ddMMyyyy.parse(dataTmp);
                        } catch (ParseException ex) {
                            data = formatadorDeData_ddMMMyyyy.parse(dataTmp);
                        }
                    }
                    if (dataAnterior == null || data.after(dataAnterior)) {
                        celulaDoValorDeMercadoEmReais = registro.getCell(COLUNA_VALOR_DE_MERCADO_DIARIO_EM_BILHOES_DE_REAIS);
                        celulaDoValorDeMercadoEmDolares = registro.getCell(COLUNA_VALOR_DE_MERCADO_DIARIO_EM_BILHOES_DE_DOLARES);
                        java.sql.Date vData = new java.sql.Date(data.getTime());
                        vValorDeMercadoEmReais = new BigDecimal(celulaDoValorDeMercadoEmReais.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                        vValorDeMercadoEmDolares = new BigDecimal(celulaDoValorDeMercadoEmDolares.getNumericCellValue()).multiply(BILHAO).setScale(0, RoundingMode.DOWN);
                        stmtDestino.clearParameters();
                        stmtDestino.setDateAtName("DATA", vData);
                        stmtDestino.setBigDecimalAtName("VALOR_DE_MERCADO_REAL", vValorDeMercadoEmReais);
                        stmtDestino.setBigDecimalAtName("VALOR_DE_MERCADO_DOLAR", vValorDeMercadoEmDolares);
                        int contagemDasInsercoes = stmtDestino.executeUpdate();
                        quantidadeDeRegistrosImportados++;
                        double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosEstimada * 100;
                        pAndamento.setPercentualCompleto((int) percentualCompleto);
                    }
                    dataAnterior = data;
                } else {
                    break;
                }
            }
            conDestino.commit();
        } catch (Exception ex) {
            conDestino.rollback();
            ProblemaNaImportacaoDeArquivo problemaDetalhado = new ProblemaNaImportacaoDeArquivo();
            problemaDetalhado.nomeDoArquivo = pArquivoXLS.getName();
            problemaDetalhado.linhaProblematicaDoArquivo = iLinha;
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
