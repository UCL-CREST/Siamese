    public SysSequences getSeqs(String tableName) throws SQLException {
        SysSequences seq = new SysSequences();
        if (tableName == null || tableName.trim().equals("")) return null;
        Connection conn = null;
        try {
            conn = ConnectUtil.getConnect();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("update ss_sys_sequences set next_value=next_value+step_value where table_name='" + tableName + "'");
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("select * from ss_sys_sequences where table_name='" + tableName + "'");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long nextValue = rs.getLong(2);
                long stepValue = rs.getLong(3);
                seq.setTableName(tableName);
                seq.setNextValue(nextValue - stepValue + 1);
                seq.setStepValue(stepValue);
            }
            rs.close();
            ps.close();
            if (seq.getTableName() == null) {
                ps = conn.prepareStatement("insert into ss_sys_sequences values('" + tableName + "'," + (Constants.DEFAULT_CURR_VALUE + Constants.DEFAULT_STEP_VALUE) + "," + Constants.DEFAULT_STEP_VALUE + ")");
                ps.executeUpdate();
                ps.close();
                seq.setTableName(tableName);
                seq.setNextValue(Constants.DEFAULT_CURR_VALUE + 1);
                seq.setStepValue(Constants.DEFAULT_STEP_VALUE);
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
            }
            ConnectUtil.closeConn(conn);
        }
        return seq;
    }
