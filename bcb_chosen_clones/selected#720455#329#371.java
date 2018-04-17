    public void updateProfile() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(this.url);
            connection.setAutoCommit(false);
            String query2 = "UPDATE customers SET password=? WHERE name=?";
            String query3 = "UPDATE customers_profile " + "SET first_name=?,middle_name=?,last_name=?,address1=?" + ",address2=?,city=?,post_box=?,email=?,country=? WHERE name=?";
            ps1 = connection.prepareStatement(query3);
            ps2 = connection.prepareStatement(query2);
            ps1.setString(1, this.firstName);
            ps1.setString(2, this.middleName);
            ps1.setString(3, this.lastName);
            ps1.setString(4, this.address1);
            ps1.setString(5, this.address2);
            ps1.setString(6, this.city);
            ps1.setString(7, this.postBox);
            ps1.setString(8, this.email);
            ps1.setString(9, this.country);
            ps1.setString(10, this.name);
            ps2.setString(1, this.password);
            ps2.setString(2, this.name);
            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (Exception ex) {
            connection.rollback();
        } finally {
            try {
                this.connection.close();
            } catch (Exception ex) {
            }
            try {
                ps1.close();
            } catch (Exception ex) {
            }
            try {
                ps2.close();
            } catch (Exception ex) {
            }
        }
    }
