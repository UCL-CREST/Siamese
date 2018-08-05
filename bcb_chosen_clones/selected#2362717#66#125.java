    protected long incrementInDatabase(Object type) {
        long current_value;
        long new_value;
        String entry;
        if (global_entry != null) entry = global_entry; else throw new UnsupportedOperationException("Named key generators are not yet supported.");
        String lkw = (String) properties.get("net.ontopia.topicmaps.impl.rdbms.HighLowKeyGenerator.SelectSuffix");
        String sql_select;
        if (lkw == null && (database.equals("sqlserver"))) {
            sql_select = "select " + valcol + " from " + table + " with (XLOCK) where " + keycol + " = ?";
        } else {
            if (lkw == null) {
                if (database.equals("sapdb")) lkw = "with lock"; else lkw = "for update";
            }
            sql_select = "select " + valcol + " from " + table + " where " + keycol + " = ? " + lkw;
        }
        if (log.isDebugEnabled()) log.debug("KeyGenerator: retrieving: " + sql_select);
        Connection conn = null;
        try {
            conn = connfactory.requestConnection();
            PreparedStatement stm1 = conn.prepareStatement(sql_select);
            try {
                stm1.setString(1, entry);
                ResultSet rs = stm1.executeQuery();
                if (!rs.next()) throw new OntopiaRuntimeException("HIGH/LOW key generator table '" + table + "' not initialized (no rows).");
                current_value = rs.getLong(1);
                rs.close();
            } finally {
                stm1.close();
            }
            new_value = current_value + grabsize;
            String sql_update = "update " + table + " set " + valcol + " = ? where " + keycol + " = ?";
            if (log.isDebugEnabled()) log.debug("KeyGenerator: incrementing: " + sql_update);
            PreparedStatement stm2 = conn.prepareStatement(sql_update);
            try {
                stm2.setLong(1, new_value);
                stm2.setString(2, entry);
                stm2.executeUpdate();
            } finally {
                stm2.close();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException e2) {
            }
            throw new OntopiaRuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    throw new OntopiaRuntimeException(e);
                }
            }
        }
        value = current_value + 1;
        max_value = new_value;
        return value;
    }
