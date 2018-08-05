    public void updateComponent(int id, int quantity) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(this.jdbcURL);
            connection.setAutoCommit(false);
            String query = "UPDATE components SET quantity=quantity+? WHERE comp_id=?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.executeUpdate();
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
            }
            try {
                ps.close();
            } catch (Exception ex) {
            }
        }
    }
