    public void movePrior(String[] showOrder, String[] orgID, String targetShowOrder, String targetOrgID) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        int moveCount = showOrder.length;
        DBOperation dbo = factory.createDBOperation(POOL_NAME);
        String strQuery = "select show_order from " + Common.ORGANIZE_TABLE + " where show_order=" + showOrder[moveCount - 1] + " and organize_id= '" + orgID[moveCount - 1] + "'";
        try {
            con = dbo.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(strQuery);
            result = ps.executeQuery();
            int maxOrderNo = 0;
            if (result.next()) {
                maxOrderNo = result.getInt(1);
            }
            String[] sqls = new String[moveCount + 1];
            sqls[0] = "update " + Common.ORGANIZE_TABLE + " set show_order=" + maxOrderNo + " where show_order=" + targetShowOrder + " and organize_id= '" + targetOrgID + "'";
            for (int i = 0; i < showOrder.length; i++) {
                sqls[i + 1] = "update " + Common.ORGANIZE_TABLE + " set show_order=show_order-1" + " where show_order=" + showOrder[i] + " and organize_id= '" + orgID[i] + "'";
            }
            for (int j = 0; j < sqls.length; j++) {
                ps = con.prepareStatement(sqls[j]);
                int resultCount = ps.executeUpdate();
                if (resultCount != 1) {
                    throw new CesSystemException("Organize.movePrior(): ERROR Inserting data " + "in T_SYS_ORGANIZE update !! resultCount = " + resultCount);
                }
            }
            con.commit();
        } catch (SQLException se) {
            if (con != null) {
                con.rollback();
            }
            throw new CesSystemException("Organize.movePrior(): SQLException while mov organize order " + " :\n\t" + se);
        } finally {
            con.setAutoCommit(true);
            close(dbo, ps, result);
        }
    }
