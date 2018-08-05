    private int create() throws SQLException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            query = "insert into " + DB.Tbl.users + "(" + col.name + "," + col.login + "," + col.pass + "," + col.passHash + "," + col.email + "," + col.role + "," + col.addDate + ") values('" + name + "','" + login + "','" + pass + "','" + pass.hashCode() + "','" + email + "'," + role + ",now())";
            st.executeUpdate(query, new String[] { col.id });
            rs = st.getGeneratedKeys();
            while (rs.next()) {
                int genId = rs.getInt(1);
                conn.commit();
                return genId;
            }
            throw new SQLException("Не удается получить generatedKey при создании пользователя.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
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
