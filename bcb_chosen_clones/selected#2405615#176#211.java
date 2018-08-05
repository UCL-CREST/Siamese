    private synchronized void persist() {
        Connection conn = null;
        try {
            PoolManager pm = PoolManager.getInstance();
            conn = pm.getConnection(JukeXTrackStore.DB_NAME);
            conn.setAutoCommit(false);
            Statement state = conn.createStatement();
            state.executeUpdate("DELETE FROM PlaylistEntry WHERE playlistid=" + this.id);
            if (this.size() > 0) {
                StringBuffer sql = new StringBuffer();
                sql.append("INSERT INTO PlaylistEntry ( playlistid , trackid , position ) VALUES ");
                int location = 0;
                Iterator i = ll.iterator();
                while (i.hasNext()) {
                    long currTrackID = ((DatabaseObject) i.next()).getId();
                    sql.append('(').append(this.id).append(',').append(currTrackID).append(',').append(location++).append(')');
                    if (i.hasNext()) sql.append(',');
                }
                state.executeUpdate(sql.toString());
            }
            conn.commit();
            conn.setAutoCommit(true);
            state.close();
        } catch (SQLException se) {
            try {
                conn.rollback();
            } catch (SQLException ignore) {
            }
            log.error("Encountered an error persisting a playlist", se);
        } finally {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }
