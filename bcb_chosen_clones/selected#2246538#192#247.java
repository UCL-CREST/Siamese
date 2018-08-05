    public boolean deleteRoleType(int id, int namespaceId, boolean removeReferencesInRoleTypes, DTSPermission permit) throws SQLException, PermissionException, DTSValidationException {
        checkPermission(permit, String.valueOf(namespaceId));
        boolean exist = isRoleTypeUsed(namespaceId, id);
        if (exist) {
            throw new DTSValidationException(ApelMsgHandler.getInstance().getMsg("DTS-0034"));
        }
        if (!removeReferencesInRoleTypes) {
            StringBuffer msgBuf = new StringBuffer();
            DTSTransferObject[] objects = fetchRightIdentityReferences(namespaceId, id);
            if (objects.length > 0) {
                msgBuf.append("Role Type is Right Identity in one or more Role Types.");
            }
            objects = fetchParentReferences(namespaceId, id);
            if (objects.length > 0) {
                if (msgBuf.length() > 0) {
                    msgBuf.append("\n");
                }
                msgBuf.append("Role Type is Parent of one or more Role Types.");
            }
            if (msgBuf.length() > 0) {
                throw new DTSValidationException(msgBuf.toString());
            }
        }
        String sqlRightId = getDAO().getStatement(ROLE_TYPE_TABLE_KEY, "DELETE_RIGHT_IDENTITY_REF");
        String sqlParent = getDAO().getStatement(ROLE_TYPE_TABLE_KEY, "DELETE_PARENT_REF");
        String sql = getDAO().getStatement(ROLE_TYPE_TABLE_KEY, "DELETE");
        PreparedStatement pstmt = null;
        boolean success = false;
        long typeGid = getGID(namespaceId, id);
        conn.setAutoCommit(false);
        int defaultLevel = conn.getTransactionIsolation();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        try {
            pstmt = conn.prepareStatement(sqlRightId);
            pstmt.setLong(1, typeGid);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(sqlParent);
            pstmt.setLong(1, typeGid);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, typeGid);
            int count = pstmt.executeUpdate();
            success = (count == 1);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
            closeStatement(pstmt);
        }
        return success;
    }
