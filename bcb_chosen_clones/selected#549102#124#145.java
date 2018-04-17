    private void removeCollection(long oid, Connection conn) throws XMLDBException {
        try {
            String sql = "DELETE FROM X_DOCUMENT WHERE X_DOCUMENT.XDB_COLLECTION_OID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, oid);
            pstmt.executeUpdate();
            pstmt.close();
            sql = "DELETE FROM XDB_COLLECTION WHERE XDB_COLLECTION.XDB_COLLECTION_OID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, oid);
            pstmt.executeUpdate();
            pstmt.close();
            removeChildCollection(oid, conn);
        } catch (java.sql.SQLException se) {
            try {
                conn.rollback();
            } catch (java.sql.SQLException se2) {
                se2.printStackTrace();
            }
            se.printStackTrace();
        }
    }
