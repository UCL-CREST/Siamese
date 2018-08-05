    public void saveMapping() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(this.jdbcURL);
            connection.setAutoCommit(false);
            String query = "INSERT INTO mapping(product_id, comp_id, count) VALUES(?,?,?)";
            ps = (PreparedStatement) connection.prepareStatement(query);
            ps.setInt(1, this.productId);
            ps.setInt(2, this.componentId);
            ps.setInt(3, 1);
            ps.executeUpdate();
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
