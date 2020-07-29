    public static boolean insert(final CelulaFinanceira objCelulaFinanceira) {
        int result = 0;
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "insert into celula_financeira " + "(descricao, id_orgao, id_gestao, " + "id_natureza_despesa, id_programa_trabalho, " + "id_unidade_orcamentaria, id_fonte_recursos, " + "valor_provisionado, gasto_previsto, gasto_real, " + "saldo_previsto, saldo_real)" + " values (?, ?, ?, ?, ?, ?, ?, TRUNCATE(?,2), TRUNCATE(?,2), TRUNCATE(?,2), TRUNCATE(?,2), TRUNCATE(?,2))";
            pst = c.prepareStatement(sql);
            pst.setString(1, objCelulaFinanceira.getDescricao());
            pst.setLong(2, (objCelulaFinanceira.getOrgao()).getCodigo());
            pst.setString(3, (objCelulaFinanceira.getGestao()).getCodigo());
            pst.setString(4, (objCelulaFinanceira.getNaturezaDespesa()).getCodigo());
            pst.setString(5, (objCelulaFinanceira.getProgramaTrabalho()).getCodigo());
            pst.setString(6, (objCelulaFinanceira.getUnidadeOrcamentaria()).getCodigo());
            pst.setString(7, (objCelulaFinanceira.getFonteRecursos()).getCodigo());
            pst.setDouble(8, objCelulaFinanceira.getValorProvisionado());
            pst.setDouble(9, objCelulaFinanceira.getGastoPrevisto());
            pst.setDouble(10, objCelulaFinanceira.getGastoReal());
            pst.setDouble(11, objCelulaFinanceira.getSaldoPrevisto());
            pst.setDouble(12, objCelulaFinanceira.getSaldoReal());
            result = pst.executeUpdate();
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final SQLException e1) {
                System.out.println("[CelulaFinanceiraDAO.insert] Erro ao inserir -> " + e1.getMessage());
            }
            System.out.println("[CelulaFinanceiraDAO.insert] Erro ao inserir -> " + e.getMessage());
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
