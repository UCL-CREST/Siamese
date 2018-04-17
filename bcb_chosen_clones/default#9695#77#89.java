    private boolean login() {
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            String url = "jdbc:mysql://" + host.getText() + "/" + stddb;
            C = DriverManager.getConnection(url, username.getText(), new String(password.getPassword()));
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "SQL Exception :\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Couldn't load " + "JDBC Driver :\n" + e);
        }
        return false;
    }
