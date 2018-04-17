    void ProcessRequests(DataInputStream in, PrintStream out) {
        String sQuery;
        while (true) {
            try {
                sQuery = in.readLine();
                if (sQuery == null) return;
                if (sQuery.startsWith("select")) {
                    ProcessQuery(sQuery, out);
                } else if (sQuery.startsWith("insert")) {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sQuery);
                } else if (sQuery.startsWith("update")) {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sQuery);
                } else if (sQuery.startsWith("commit")) {
                    con.commit();
                } else if (sQuery.startsWith("autocommit true")) {
                    con.setAutoCommit(true);
                } else if (sQuery.startsWith("autocommit false")) {
                    con.setAutoCommit(false);
                } else if (sQuery.startsWith("delete")) {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sQuery);
                } else if (sQuery.startsWith("rollback")) {
                    con.rollback();
                } else {
                    out.println("What ?");
                }
                out.println(">");
            } catch (SQLException ex) {
                out.println("\n*** SQLException caught ***\n");
                while (ex != null) {
                    out.println("SQLState: " + ex.getSQLState());
                    out.println("Message:  " + ex.getMessage());
                    out.println("Vendor:   " + ex.getErrorCode());
                    ex = ex.getNextException();
                    out.println("");
                }
            } catch (java.lang.Exception ex) {
                ex.printStackTrace();
            }
        }
    }
