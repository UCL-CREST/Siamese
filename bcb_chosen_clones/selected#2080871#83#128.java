    public static boolean update(ItemNotaFiscal objINF) {
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        int result;
        CelulaFinanceira objCF = null;
        if (c == null) {
            return false;
        }
        if (objINF == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            String sql = "";
            sql = "update item_nota_fiscal " + "set id_item_pedido = ? " + "where id_item_nota_fiscal = ?";
            pst = c.prepareStatement(sql);
            pst.setInt(1, objINF.getItemPedido().getCodigo());
            pst.setInt(2, objINF.getCodigo());
            result = pst.executeUpdate();
            if (result > 0) {
                if (objINF.getItemPedido().getCelulaFinanceira() != null) {
                    objCF = objINF.getItemPedido().getCelulaFinanceira();
                    objCF.atualizaGastoReal(objINF.getSubtotal());
                    if (CelulaFinanceiraDAO.update(objCF)) {
                    }
                }
            }
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final Exception e1) {
                System.out.println("[ItemNotaFiscalDAO.update.rollback] Erro ao inserir -> " + e1.getMessage());
            }
            System.out.println("[ItemNotaFiscalDAO.update.insert] Erro ao inserir -> " + e.getMessage());
            result = 0;
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
