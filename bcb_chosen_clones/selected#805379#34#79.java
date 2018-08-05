    public void saveSharedFiles(List<FrostSharedFileItem> sfFiles) throws SQLException {
        Connection conn = AppLayerDatabase.getInstance().getPooledConnection();
        try {
            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            s.executeUpdate("DELETE FROM SHAREDFILES");
            s.close();
            s = null;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO SHAREDFILES (" + "path,size,fnkey,sha,owner,comment,rating,keywords," + "lastuploaded,uploadcount,reflastsent,requestlastreceived,requestsreceivedcount,lastmodified) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            for (Iterator<FrostSharedFileItem> i = sfFiles.iterator(); i.hasNext(); ) {
                FrostSharedFileItem sfItem = i.next();
                int ix = 1;
                ps.setString(ix++, sfItem.getFile().getPath());
                ps.setLong(ix++, sfItem.getFileSize());
                ps.setString(ix++, sfItem.getChkKey());
                ps.setString(ix++, sfItem.getSha());
                ps.setString(ix++, sfItem.getOwner());
                ps.setString(ix++, sfItem.getComment());
                ps.setInt(ix++, sfItem.getRating());
                ps.setString(ix++, sfItem.getKeywords());
                ps.setLong(ix++, sfItem.getLastUploaded());
                ps.setInt(ix++, sfItem.getUploadCount());
                ps.setLong(ix++, sfItem.getRefLastSent());
                ps.setLong(ix++, sfItem.getRequestLastReceived());
                ps.setInt(ix++, sfItem.getRequestsReceived());
                ps.setLong(ix++, sfItem.getLastModified());
                ps.executeUpdate();
            }
            ps.close();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Exception during save", t);
            try {
                conn.rollback();
            } catch (Throwable t1) {
                logger.log(Level.SEVERE, "Exception during rollback", t1);
            }
            try {
                conn.setAutoCommit(true);
            } catch (Throwable t1) {
            }
        } finally {
            AppLayerDatabase.getInstance().givePooledConnection(conn);
        }
    }
