    public void doPreparedStatementQueryAndUpdate(Connection conn, String id) throws SQLException {
        try {
            int key = getNextKey();
            String bValue = "doPreparedStatementQueryAndUpdate:" + id + ":" + testId;
            PreparedStatement s1;
            if (key >= MAX_KEY_VALUE) {
                key = key % MAX_KEY_VALUE;
                s1 = conn.prepareStatement("delete from many_threads where a = ?");
                s1.setInt(1, key);
                s1.executeUpdate();
                s1.close();
            }
            s1 = conn.prepareStatement("insert into many_threads values (?, ?, 0)");
            s1.setInt(1, key);
            s1.setString(2, bValue);
            assertEquals(1, s1.executeUpdate());
            s1.close();
            s1 = conn.prepareStatement("select a from many_threads where a = ?");
            s1.setInt(1, key);
            assertEquals(key, executeQuery(s1));
            s1.close();
            s1 = conn.prepareStatement("update many_threads set value = a * a, b = b || ? where a = ?");
            s1.setString(1, "&" + bValue);
            s1.setInt(2, key + 1);
            s1.executeUpdate();
            s1.close();
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException e) {
            if (!conn.getAutoCommit()) {
                try {
                    conn.rollback();
                } catch (SQLException e2) {
                }
            }
        }
    }
