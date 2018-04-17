    public void create(String oid, Serializable obj) throws PersisterException {
        String key = getLock(oid);
        if (key != null) {
            throw new PersisterException("Object already exists: OID = " + oid);
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            byte[] data = serialize(obj);
            conn = _ds.getConnection();
            conn.setAutoCommit(true);
            ps = conn.prepareStatement("insert into " + _table_name + "(" + _oid_col + "," + _data_col + "," + _ts_col + ") values (?,?,?)");
            ps.setString(1, oid);
            ps.setBinaryStream(2, new ByteArrayInputStream(data), data.length);
            ps.setLong(3, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (Throwable th) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Throwable th2) {
                }
            }
            throw new PersisterException("Failed to create object: OID = " + oid, th);
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
