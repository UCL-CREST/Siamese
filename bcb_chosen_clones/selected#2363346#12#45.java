    public static boolean insert(final PedidoSituacao pedidoSituacao) {
        int result = 0;
        final Connection c = DBConnection.getConnection();
        PreparedStatement pst = null;
        if (c == null) {
            return false;
        }
        try {
            c.setAutoCommit(false);
            final String sql = "insert into pedido_situacao (id_pedido, id_situacao, em, observacao, id_funcionario) " + "values (?, ? , now(), ?, ?) ";
            pst = c.prepareStatement(sql);
            pst.setInt(1, pedidoSituacao.getPedido().getCodigo());
            pst.setInt(2, pedidoSituacao.getSituacao().getCodigo());
            pst.setString(3, pedidoSituacao.getObservacao());
            pst.setInt(4, pedidoSituacao.getFuncionario().getCodigo());
            result = pst.executeUpdate();
            c.commit();
        } catch (final SQLException e) {
            try {
                c.rollback();
            } catch (final SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println("[PedidoSituacaoDAO.insert] Erro ao inserir -> " + e.getMessage());
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
