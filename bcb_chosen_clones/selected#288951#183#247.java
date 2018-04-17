    public void markAsCachedHelper(Item item, Date from, Date to, Map<String, Boolean> properties) {
        if (properties.size() == 0) {
            return;
        }
        Connection conn = null;
        Iterable<Integer> props = representer.getInternalReps(properties.keySet());
        Integer hostIndex = representer.lookUpInternalRep(item.getResolved().getHost());
        HashMap<Integer, long[]> periods = new HashMap<Integer, long[]>();
        for (Map.Entry<String, Boolean> e : properties.entrySet()) {
            periods.put(representer.lookUpInternalRep(e.getKey()), new long[] { from.getTime(), to.getTime(), e.getValue() ? 1 : 0 });
        }
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            conn.setSavepoint();
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement("SELECT MIN(starttime), MAX(endtime), MAX(hasvalues) FROM cachedperiods WHERE " + "id = ? AND host = ? AND prop = ? AND " + "starttime <= ? AND endtime >= ?");
                stmt.setString(1, item.getResolved().getId());
                stmt.setInt(2, hostIndex);
                stmt.setLong(4, to.getTime());
                stmt.setLong(5, from.getTime());
                for (Map.Entry<Integer, long[]> e1 : periods.entrySet()) {
                    stmt.setInt(3, e1.getKey());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        e1.getValue()[0] = Math.min(rs.getLong(1), e1.getValue()[0]);
                        e1.getValue()[1] = Math.max(rs.getLong(2), e1.getValue()[1]);
                        e1.getValue()[2] = Math.max(rs.getInt(3), e1.getValue()[2]);
                    }
                    StorageUtils.close(rs);
                }
                StorageUtils.close(stmt);
                stmt = conn.prepareStatement("DELETE FROM cachedperiods WHERE " + "id = ? AND host = ? AND " + "starttime <= ? AND endtime >= ? AND " + "prop IN (" + StringUtils.join(props.iterator(), ",") + ")");
                stmt.setString(1, item.getResolved().getId());
                stmt.setInt(2, hostIndex);
                stmt.setLong(3, to.getTime());
                stmt.setLong(4, from.getTime());
                stmt.executeUpdate();
                StorageUtils.close(stmt);
                stmt = conn.prepareStatement("INSERT INTO cachedperiods (id, host, prop, starttime, endtime, hasvalues) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setString(1, item.getResolved().getId());
                stmt.setInt(2, hostIndex);
                for (Map.Entry<Integer, long[]> e2 : periods.entrySet()) {
                    stmt.setInt(3, e2.getKey());
                    stmt.setLong(4, e2.getValue()[0]);
                    stmt.setLong(5, e2.getValue()[1]);
                    stmt.setInt(6, (int) e2.getValue()[2]);
                    stmt.executeUpdate();
                }
            } finally {
                StorageUtils.close(stmt);
            }
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MetaDataStoragerImpl.class.getName()).log(Level.SEVERE, "Cannot update cachedperiods table.", ex);
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(MetaDataStoragerImpl.class.getName()).log(Level.SEVERE, "Could not roll back database, please consult system administrator.", ex1);
            }
        } finally {
            StorageUtils.close(conn);
        }
    }
