    public int commit() throws TransactionException, SQLException, ConnectionFactoryException {
        Connection conn = ConnectionFactoryProxy.getInstance().getConnection(_poolName);
        conn.setAutoCommit(false);
        int numRowsUpdated = 0;
        try {
            Iterator statements = _statements.iterator();
            while (statements.hasNext()) {
                StatementData sd = (StatementData) statements.next();
                PreparedStatement ps = conn.prepareStatement(sd.statement);
                Iterator params = sd.params.iterator();
                int index = 1;
                while (params.hasNext()) {
                    ps.setString(index++, (String) params.next());
                }
                numRowsUpdated += ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            System.err.println("com.zenark.zsql.TransactionImpl.commit() failed: Queued Statements follow");
            Iterator statements = _statements.iterator();
            while (statements.hasNext()) {
                StatementData sd = (StatementData) statements.next();
                System.err.println("+--Statement: " + sd.statement + " with " + sd.params.size() + " parameters");
                for (int loop = 0; loop < sd.params.size(); loop++) {
                    System.err.println("+--Param    : " + (String) sd.params.get(loop));
                }
            }
            throw ex;
        } finally {
            _statements.clear();
            conn.rollback();
            conn.clearWarnings();
            ConnectionFactoryProxy.getInstance().releaseConnection(conn);
        }
        return numRowsUpdated;
    }
