    public static String deleteTag(String tag_id) {
        String so = OctopusErrorMessages.UNKNOWN_ERROR;
        if (tag_id == null || tag_id.trim().equals("")) {
            return OctopusErrorMessages.TAG_ID_CANT_BE_EMPTY;
        }
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "DELETE FROM tr_translation WHERE tr_translation_trtagid=?";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, tag_id);
            state.executeUpdate();
            String query2 = "DELETE FROM tr_tag WHERE tr_tag_id=? ";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, tag_id);
            state2.executeUpdate();
            theConnection.commit();
            so = OctopusErrorMessages.ACTION_DONE;
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
            }
            so = OctopusErrorMessages.ERROR_DATABASE;
        } finally {
            if (theConnection != null) {
                try {
                    theConnection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
                theConnection.release();
            }
        }
        return so;
    }
