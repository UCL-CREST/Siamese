        public static int simpleUpdate(String query) throws SQLException {
            Connection conn = null;
            Statement st = null;
            try {
                conn = dataSource.getConnection();
                st = conn.createStatement();
                int res = st.executeUpdate(query);
                conn.commit();
                return res;
            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (Exception e1) {
                }
                throw e;
            } finally {
                try {
                    st.close();
                } catch (Exception e) {
                }
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        }
