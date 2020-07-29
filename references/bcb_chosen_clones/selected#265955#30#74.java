    public void saveDownloadFiles(List downloadFiles) throws SQLException {
        Connection conn = AppLayerDatabase.getInstance().getPooledConnection();
        try {
            conn.setAutoCommit(false);
            Statement s = conn.createStatement();
            s.executeUpdate("DELETE FROM DOWNLOADFILES");
            s.close();
            s = null;
            PreparedStatement ps = conn.prepareStatement("INSERT INTO DOWNLOADFILES " + "(name,targetpath,size,fnkey,enabled,state,downloadaddedtime,downloadstartedtime,downloadfinishedtime," + "retries,lastdownloadstoptime,gqid,filelistfilesha) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            for (Iterator i = downloadFiles.iterator(); i.hasNext(); ) {
                FrostDownloadItem dlItem = (FrostDownloadItem) i.next();
                int ix = 1;
                ps.setString(ix++, dlItem.getFilename());
                ps.setString(ix++, dlItem.getTargetPath());
                ps.setLong(ix++, (dlItem.getFileSize() == null ? 0 : dlItem.getFileSize().longValue()));
                ps.setString(ix++, dlItem.getKey());
                ps.setBoolean(ix++, (dlItem.isEnabled() == null ? true : dlItem.isEnabled().booleanValue()));
                ps.setInt(ix++, dlItem.getState());
                ps.setLong(ix++, dlItem.getDownloadAddedTime());
                ps.setLong(ix++, dlItem.getDownloadStartedTime());
                ps.setLong(ix++, dlItem.getDownloadFinishedTime());
                ps.setInt(ix++, dlItem.getRetries());
                ps.setLong(ix++, dlItem.getLastDownloadStopTime());
                ps.setString(ix++, dlItem.getGqIdentifier());
                ps.setString(ix++, dlItem.getFileListFileObject() == null ? null : dlItem.getFileListFileObject().getSha());
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
