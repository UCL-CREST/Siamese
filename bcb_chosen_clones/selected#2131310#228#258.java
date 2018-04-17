    public static Chunk updateLastSend(Chunk c) throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        Chunk ret = null;
        String SQL = "UPDATE CHUNK SET SENT=? WHERE FILEHASH=? AND STARTOFF=? AND LENGTH=?";
        log.debug("update chunk last sent for chunk " + c.getHash() + " startoff " + c.getStartOffset());
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            stmt = conn.prepareStatement(SQL);
            stmt.setDate(1, new java.sql.Date(c.getLastSend().getTime()));
            stmt.setString(2, c.getHash());
            stmt.setLong(3, c.getStartOffset());
            stmt.setLong(4, c.getSize());
            stmt.executeUpdate();
            conn.commit();
            stmt.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("Error while updating chunk " + c.getHash() + "offset:" + c.getStartOffset() + "SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                conn.rollback();
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
        return ret;
    }
