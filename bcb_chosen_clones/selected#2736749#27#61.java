    public List<SysSequences> getSeqs() throws Exception {
        List<SysSequences> list = new ArrayList<SysSequences>();
        Connection conn = null;
        try {
            conn = ConnectUtil.getConnect();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("update ss_sys_sequences set next_value=next_value+step_value");
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("select * from ss_sys_sequences");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SysSequences seq = new SysSequences();
                seq.setTableName(rs.getString(1));
                long nextValue = rs.getLong(2);
                long stepValue = rs.getLong(3);
                seq.setNextValue(nextValue - stepValue);
                seq.setStepValue(stepValue);
                list.add(seq);
            }
            rs.close();
            ps.close();
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
        return list;
    }
