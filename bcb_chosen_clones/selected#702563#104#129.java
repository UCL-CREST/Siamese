    private boolean delete() {
        boolean ret = false;
        try {
            Connection conn = ((JdbcRequestCycle) getRequestCycle()).getConnection();
            if (conn == null) {
                throw new RestartResponseException(new OkErrorPage(OkErrorEnum.DATABASE));
            }
            String query = "delete from revisions where entry=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entry);
            int revisionsRowsAffected = pstmt.executeUpdate();
            query = "delete from entry where id=?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, entry);
            int entryRowAffected = pstmt.executeUpdate();
            if (entryRowAffected > 0) {
                ret = true;
            } else {
                conn.rollback();
            }
            info(entryRowAffected + " entry with " + revisionsRowsAffected + " revisions was deleted.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
