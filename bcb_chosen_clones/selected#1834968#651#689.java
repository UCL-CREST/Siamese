    public void importarHistoricoDoPIB(Andamento pAndamento) throws FileNotFoundException, SQLException, Exception {
        pAndamento.delimitarIntervaloDeVariacao(0, 49);
        PIB[] valoresPendentesDoPIB = obterValoresPendentesDoPIB(pAndamento);
        pAndamento.delimitarIntervaloDeVariacao(50, 100);
        if (valoresPendentesDoPIB != null && valoresPendentesDoPIB.length > 0) {
            String sql = "INSERT INTO tmp_TB_PIB(ULTIMO_DIA_DO_MES, PIB_ACUM_12MESES_REAL, PIB_ACUM_12MESES_DOLAR) VALUES(:ULTIMO_DIA_DO_MES, :PIB_ACUM_12MESES_REAL, :PIB_ACUM_12MESES_DOLAR)";
            OraclePreparedStatement stmtDestino = (OraclePreparedStatement) conDestino.prepareStatement(sql);
            stmtDestino.setExecuteBatch(COMANDOS_POR_LOTE);
            int quantidadeDeRegistrosASeremImportados = valoresPendentesDoPIB.length;
            try {
                int quantidadeDeRegistrosImportados = 0;
                int numeroDoRegistro = 0;
                final BigDecimal MILHAO = new BigDecimal("1000000");
                for (PIB valorPendenteDoPIB : valoresPendentesDoPIB) {
                    ++numeroDoRegistro;
                    stmtDestino.clearParameters();
                    java.sql.Date vULTIMO_DIA_DO_MES = new java.sql.Date(obterUltimoDiaDoMes(valorPendenteDoPIB.mesEAno).getTime());
                    BigDecimal vPIB_ACUM_12MESES_REAL = valorPendenteDoPIB.valorDoPIBEmReais.multiply(MILHAO).setScale(0, RoundingMode.DOWN);
                    BigDecimal vPIB_ACUM_12MESES_DOLAR = valorPendenteDoPIB.valorDoPIBEmDolares.multiply(MILHAO).setScale(0, RoundingMode.DOWN);
                    stmtDestino.setDateAtName("ULTIMO_DIA_DO_MES", vULTIMO_DIA_DO_MES);
                    stmtDestino.setBigDecimalAtName("PIB_ACUM_12MESES_REAL", vPIB_ACUM_12MESES_REAL);
                    stmtDestino.setBigDecimalAtName("PIB_ACUM_12MESES_DOLAR", vPIB_ACUM_12MESES_DOLAR);
                    int contagemDasInsercoes = stmtDestino.executeUpdate();
                    quantidadeDeRegistrosImportados++;
                    double percentualCompleto = (double) quantidadeDeRegistrosImportados / quantidadeDeRegistrosASeremImportados * 100;
                    pAndamento.setPercentualCompleto((int) percentualCompleto);
                }
                conDestino.commit();
            } catch (Exception ex) {
                conDestino.rollback();
                throw ex;
            } finally {
                if (stmtDestino != null && (!stmtDestino.isClosed())) {
                    stmtDestino.close();
                }
            }
        }
        pAndamento.setPercentualCompleto(100);
    }
