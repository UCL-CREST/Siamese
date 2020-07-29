    public void delUser(User user) throws SQLException, IOException, ClassNotFoundException {
        String dbUserID;
        String stockSymbol;
        Statement stmt = con.createStatement();
        try {
            con.setAutoCommit(false);
            dbUserID = user.getUserID();
            if (getUser(dbUserID) != null) {
                ResultSet rs1 = stmt.executeQuery("SELECT userID, symbol " + "FROM UserStocks WHERE userID = '" + dbUserID + "'");
                while (rs1.next()) {
                    try {
                        stockSymbol = rs1.getString("symbol");
                        delUserStocks(dbUserID, stockSymbol);
                    } catch (SQLException ex) {
                        throw new SQLException("Deletion of user stock holding failed: " + ex.getMessage());
                    }
                }
                try {
                    stmt.executeUpdate("DELETE FROM Users WHERE " + "userID = '" + dbUserID + "'");
                } catch (SQLException ex) {
                    throw new SQLException("User deletion failed: " + ex.getMessage());
                }
            } else throw new IOException("User not found in database - cannot delete.");
            try {
                con.commit();
            } catch (SQLException ex) {
                throw new SQLException("Transaction commit failed: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException sqx) {
                throw new SQLException("Transaction failed then rollback failed: " + sqx.getMessage());
            }
            throw new SQLException("Transaction failed; was rolled back: " + ex.getMessage());
        }
        stmt.close();
    }
