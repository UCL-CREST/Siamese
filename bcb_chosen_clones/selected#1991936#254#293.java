    public void lock(String oid, String key) throws PersisterException {
        String lock = getLock(oid);
        if (lock == null) {
            throw new PersisterException("Object does not exist: OID = " + oid);
        } else if (!NULL.equals(lock) && (!lock.equals(key))) {
            throw new PersisterException("The object is currently locked with another key: OID = " + oid + ", LOCK = " + lock + ", KEY = " + key);
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = _ds.getConnection();
            conn.setAutoCommit(true);
            ps = conn.prepareStatement("update " + _table_name + " set " + _key_col + " = ?, " + _ts_col + " = ? where " + _oid_col + " = ?");
            ps.setString(1, key);
            ps.setLong(2, System.currentTimeMillis());
            ps.setString(3, oid);
            ps.executeUpdate();
        } catch (Throwable th) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Throwable th2) {
                }
            }
            throw new PersisterException("Failed to lock object: OID = " + oid + ", KEY = " + key, th);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Throwable th) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Throwable th) {
                }
            }
        }
    }
