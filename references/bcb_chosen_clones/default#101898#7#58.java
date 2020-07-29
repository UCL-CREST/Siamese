    public static void main(String[] args) throws Exception {
        StockTrackerDB db;
        String userID;
        String symb;
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));
        Connection con;
        String url = "jdbc:odbc:StockTracker";
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            try {
                con = DriverManager.getConnection(url);
                PreparedStatement pStmt1 = con.prepareStatement("SELECT * FROM Users WHERE userID = ?");
                PreparedStatement pStmt2 = con.prepareStatement("SELECT * FROM UserStocks WHERE userID = ? ORDER BY symbol");
                PreparedStatement pStmt3 = con.prepareStatement("SELECT name FROM Stocks WHERE symbol = ?");
                System.out.print("Enter User ID: ");
                userID = dataIn.readLine();
                while (userID.length() > 0) {
                    pStmt1.setString(1, userID);
                    ResultSet rs1 = pStmt1.executeQuery();
                    while (rs1.next()) {
                        System.out.print("\nStock holdings for User: ");
                        System.out.print(rs1.getString("firstName"));
                        System.out.println(" " + rs1.getString("lastName"));
                        System.out.println("Stock - Description");
                        System.out.println("----------------------------");
                        pStmt2.setString(1, userID);
                        ResultSet rs2 = pStmt2.executeQuery();
                        while (rs2.next()) {
                            symb = rs2.getString("symbol");
                            System.out.print(symb);
                            pStmt3.setString(1, symb);
                            ResultSet rs3 = pStmt3.executeQuery();
                            if (rs3.next()) System.out.println("\t" + rs3.getString("name")); else System.out.println("No description available.");
                        }
                        System.out.println("");
                    }
                    System.out.print("Enter User ID: ");
                    userID = dataIn.readLine();
                }
                System.out.println("\nReport done.");
                pStmt1.close();
                pStmt2.close();
                pStmt3.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "SQL exception creating database object", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Class not found exception creating database object", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
