    public static void importDocumentLines(Connection conn, String originDocumentID, String destinationDocumentID) throws SQLException {
        boolean defaultAutoCommit = conn.getAutoCommit();
        String sqlQuery = "select ProductID,Description,PricePerUnit,Quantity,DiscountPCT,VATPCT,TotalNoVATPrice,TotalPrice from tbl_DocumentItem where DocumentID=?";
        String sqlInsert = "insert into tbl_DocumentItem (ProductID,Description,PricePerUnit,Quantity,DiscountPCT,VATPCT,TotalNoVATPrice,TotalPrice,DocumentID) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            pstmt1 = conn.prepareStatement(sqlQuery);
            pstmt2 = conn.prepareStatement(sqlInsert);
            conn.setAutoCommit(false);
            pstmt1.setString(1, originDocumentID);
            ResultSet rs = pstmt1.executeQuery();
            while (rs.next()) {
                pstmt2.setInt(1, rs.getInt(1));
                pstmt2.setString(2, rs.getString(2));
                pstmt2.setDouble(3, rs.getDouble(3));
                pstmt2.setDouble(4, rs.getDouble(4));
                pstmt2.setDouble(5, rs.getDouble(5));
                pstmt2.setDouble(6, rs.getDouble(6));
                pstmt2.setDouble(7, rs.getDouble(7));
                pstmt2.setDouble(8, rs.getDouble(8));
                pstmt2.setString(9, destinationDocumentID);
                pstmt2.executeUpdate();
            }
            rs.close();
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(defaultAutoCommit);
            if (pstmt1 != null) pstmt1.close();
            if (pstmt2 != null) pstmt2.close();
        }
    }
