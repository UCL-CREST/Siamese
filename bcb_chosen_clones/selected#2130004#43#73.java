    static final void executeUpdate(Collection<String> queries, DBConnector connector) throws IOException {
        Connection con = null;
        Statement st = null;
        try {
            con = connector.getDB();
            con.setAutoCommit(false);
            st = con.createStatement();
            for (String s : queries) st.executeUpdate(s);
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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
