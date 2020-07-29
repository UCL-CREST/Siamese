    static final void saveModule(Module module, DBConnector connector) throws IOException {
        String type = "pre";
        if (module.isPreModule()) type = "pre"; else if (module.isPostModule()) type = "post"; else if (module.isExceptionModule()) type = "exception"; else throw new IllegalArgumentException("Module must be of a known type.");
        Properties props = module.getState();
        Connection con = null;
        PreparedStatement ps = null;
        Statement st = null;
        try {
            con = connector.getDB();
            con.setAutoCommit(false);
            st = con.createStatement();
            st.executeUpdate("DELETE FROM instance where id=" + module.getId());
            st.executeUpdate("DELETE FROM instance_property where instance_id=" + module.getId());
            ps = con.prepareStatement("INSERT INTO instance VALUES (?, ?, ?, ?)");
            ps.setInt(1, module.getId());
            ps.setBoolean(2, module.getActive());
            ps.setString(3, module.getClass().getName());
            ps.setString(4, type);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO instance_property values(?, ?, ?)");
            for (Enumeration<Object> keys = props.keys(); keys.hasMoreElements(); ) {
                String key = (String) keys.nextElement();
                String value = props.getProperty(key);
                ps.setInt(1, module.getId());
                ps.setString(2, key);
                ps.setString(3, value);
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new IOException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignore) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignore) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
