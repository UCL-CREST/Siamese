    public static NotaFiscal insert(NotaFiscal objNF) {
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        int result;
        if (c == null) {
            return null;
        }
        if (objNF == null) {
            return null;
        }
        try {
            c.setAutoCommit(false);
            String sql = "";
            int idNotaFiscal;
            idNotaFiscal = NotaFiscalDAO.getLastCodigo();
            if (idNotaFiscal < 1) {
                return null;
            }
            sql = "INSERT INTO nota_fiscal " + "(id_nota_fiscal, id_fornecedor, total, data_emissao, data_cadastro, numero) " + "VALUES(?, ?, TRUNCATE(?,2), STR_TO_DATE(?,'%d/%m/%Y'), now(), ?) ";
            pst = c.prepareStatement(sql);
            pst.setInt(1, idNotaFiscal);
            pst.setLong(2, objNF.getFornecedor().getCodigo());
            pst.setString(3, new DecimalFormat("#0.00").format(objNF.getValor()));
            pst.setString(4, objNF.getDataEmissaoFormatada());
            pst.setString(5, objNF.getNumero());
            result = pst.executeUpdate();
            pst = null;
            if (result > 0) {
                Iterator<ItemNotaFiscal> itINF = (objNF.getItemNotaFiscal()).iterator();
                while ((itINF != null) && (itINF.hasNext())) {
                    ItemNotaFiscal objINF = (ItemNotaFiscal) itINF.next();
                    sql = "";
                    sql = "INSERT INTO item_nota_fiscal " + "(id_nota_fiscal, id_produto, quantidade, subtotal) " + "VALUES(?, ?, ?, TRUNCATE(?,2))";
                    pst = c.prepareStatement(sql);
                    pst.setInt(1, idNotaFiscal);
                    pst.setInt(2, objINF.getProduto().getCodigo());
                    pst.setInt(3, objINF.getQuantidade());
                    pst.setString(4, new DecimalFormat("#0.00").format(objINF.getSubtotal()));
                    result = pst.executeUpdate();
                }
            }
            c.commit();
            objNF.setCodigo(idNotaFiscal);
        } catch (final Exception e) {
            try {
                c.rollback();
            } catch (final Exception e1) {
                System.out.println("[NotaFiscalDAO.insert.rollback] Erro ao inserir -> " + e1.getMessage());
            }
            System.out.println("[NotaFiscalDAO.insert] Erro ao inserir -> " + e.getMessage());
            objNF = null;
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        return objNF;
    }
