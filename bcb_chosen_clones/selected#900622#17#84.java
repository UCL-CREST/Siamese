    public static Pedido insert(Pedido objPedido) {
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        int result;
        if (c == null) {
            return null;
        }
        try {
            c.setAutoCommit(false);
            String sql = "";
            int idPedido;
            idPedido = PedidoDAO.getLastCodigo();
            if (idPedido < 1) {
                return null;
            }
            sql = "insert into pedido " + "(id_pedido, id_funcionario,data_pedido,valor) " + "values(?,?,now(),truncate(?,2))";
            pst = c.prepareStatement(sql);
            pst.setInt(1, idPedido);
            pst.setInt(2, objPedido.getFuncionario().getCodigo());
            pst.setString(3, new DecimalFormat("#0.00").format(objPedido.getValor()));
            result = pst.executeUpdate();
            pst = null;
            if (result > 0) {
                Iterator<ItemPedido> itItemPedido = (objPedido.getItemPedido()).iterator();
                while ((itItemPedido != null) && (itItemPedido.hasNext())) {
                    ItemPedido objItemPedido = (ItemPedido) itItemPedido.next();
                    sql = "";
                    sql = "insert into item_pedido " + "(id_pedido,id_produto,quantidade,subtotal) " + "values (?,?,?,truncate(?,2))";
                    pst = c.prepareStatement(sql);
                    pst.setInt(1, idPedido);
                    pst.setInt(2, (objItemPedido.getProduto()).getCodigo());
                    pst.setInt(3, objItemPedido.getQuantidade());
                    pst.setString(4, new DecimalFormat("#0.00").format(objItemPedido.getSubtotal()));
                    result = pst.executeUpdate();
                }
            }
            pst = null;
            sql = "";
            sql = "insert into pedido_situacao " + "(id_pedido,id_situacao, em, observacao, id_funcionario) " + "values (?,?,now(), ?, ?)";
            pst = c.prepareStatement(sql);
            pst.setInt(1, idPedido);
            pst.setInt(2, 1);
            pst.setString(3, "Inclus�o de pedido");
            pst.setInt(4, objPedido.getFuncionario().getCodigo());
            result = pst.executeUpdate();
            pst = null;
            sql = "";
            sql = "insert into tramitacao " + "(data_tramitacao, id_pedido, id_dep_origem, id_dep_destino) " + "values (now(),?,?, ?)";
            pst = c.prepareStatement(sql);
            pst.setInt(1, idPedido);
            pst.setInt(2, 6);
            pst.setInt(3, 2);
            result = pst.executeUpdate();
            c.commit();
            objPedido.setCodigo(idPedido);
        } catch (final Exception e) {
            try {
                c.rollback();
            } catch (final Exception e1) {
                System.out.println("[PedidoDAO.insert] Erro ao inserir -> " + e1.getMessage());
            }
            System.out.println("[PedidoDAO.insert] Erro ao inserir -> " + e.getMessage());
        } finally {
            DBConnection.closePreparedStatement(pst);
            DBConnection.closeConnection(c);
        }
        return objPedido;
    }
