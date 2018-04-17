    public void insertComponent() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection(this.jdbcURL);
            connection.setAutoCommit(false);
            String query = "INSERT INTO components(name,rate,quantity, description) VALUES(?,?,?,?)";
            ps = (PreparedStatement) connection.prepareStatement(query);
            ps.setString(1, this.name);
            ps.setDouble(2, this.rate);
            ps.setInt(3, this.quantity);
            ps.setString(4, this.description);
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
