    public void insertProfile() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(this.url);
            connection.setAutoCommit(false);
            String query1 = "INSERT INTO customers(name,password) VALUES(?,?)";
            ps1 = connection.prepareStatement(query1);
            ps1.setString(1, this.name);
            ps1.setString(2, this.password);
            String query2 = "INSERT INTO customer_roles(name,role_name) VALUES(?,?)";
            ps2 = connection.prepareStatement(query2);
            ps2.setString(1, this.name);
            ps2.setString(2, "user");
            String query3 = "INSERT INTO customers_profile(name,first_name,middle_name,last_name,address1,address2,city,post_box,email,country)" + "VALUES(?,?,?,?,?,?,?,?,?,?)";
            ps3 = connection.prepareStatement(query3);
            ps3.setString(1, this.name);
            ps3.setString(2, this.firstName);
            ps3.setString(3, this.middleName);
            ps3.setString(4, this.lastName);
            ps3.setString(5, this.address1);
            ps3.setString(6, this.address2);
            ps3.setString(7, this.city);
            ps3.setString(8, this.postBox);
            ps3.setString(9, this.email);
            ps3.setString(10, this.country);
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            connection.commit();
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
            try {
                ps3.close();
            } catch (Exception ex) {
            }
        }
    }
