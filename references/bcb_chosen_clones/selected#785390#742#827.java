        int doOne(int bid, int tid, int aid, int delta) {
            int aBalance = 0;
            if (Conn == null) {
                incrementFailedTransactionCount();
                return 0;
            }
            try {
                if (prepared_stmt) {
                    pstmt1.setInt(1, delta);
                    pstmt1.setInt(2, aid);
                    pstmt1.executeUpdate();
                    pstmt1.clearWarnings();
                    pstmt2.setInt(1, aid);
                    ResultSet RS = pstmt2.executeQuery();
                    pstmt2.clearWarnings();
                    while (RS.next()) {
                        aBalance = RS.getInt(1);
                    }
                    pstmt3.setInt(1, delta);
                    pstmt3.setInt(2, tid);
                    pstmt3.executeUpdate();
                    pstmt3.clearWarnings();
                    pstmt4.setInt(1, delta);
                    pstmt4.setInt(2, bid);
                    pstmt4.executeUpdate();
                    pstmt4.clearWarnings();
                    pstmt5.setInt(1, tid);
                    pstmt5.setInt(2, bid);
                    pstmt5.setInt(3, aid);
                    pstmt5.setInt(4, delta);
                    pstmt5.executeUpdate();
                    pstmt5.clearWarnings();
                } else {
                    Statement Stmt = Conn.createStatement();
                    String Query = "UPDATE accounts ";
                    Query += "SET     Abalance = Abalance + " + delta + " ";
                    Query += "WHERE   Aid = " + aid;
                    int res = Stmt.executeUpdate(Query);
                    Stmt.clearWarnings();
                    Query = "SELECT Abalance ";
                    Query += "FROM   accounts ";
                    Query += "WHERE  Aid = " + aid;
                    ResultSet RS = Stmt.executeQuery(Query);
                    Stmt.clearWarnings();
                    while (RS.next()) {
                        aBalance = RS.getInt(1);
                    }
                    Query = "UPDATE tellers ";
                    Query += "SET    Tbalance = Tbalance + " + delta + " ";
                    Query += "WHERE  Tid = " + tid;
                    Stmt.executeUpdate(Query);
                    Stmt.clearWarnings();
                    Query = "UPDATE branches ";
                    Query += "SET    Bbalance = Bbalance + " + delta + " ";
                    Query += "WHERE  Bid = " + bid;
                    Stmt.executeUpdate(Query);
                    Stmt.clearWarnings();
                    Query = "INSERT INTO history(Tid, Bid, Aid, delta) ";
                    Query += "VALUES (";
                    Query += tid + ",";
                    Query += bid + ",";
                    Query += aid + ",";
                    Query += delta + ")";
                    Stmt.executeUpdate(Query);
                    Stmt.clearWarnings();
                    Stmt.close();
                }
                if (transactions) {
                    Conn.commit();
                }
                return aBalance;
            } catch (Exception E) {
                if (verbose) {
                    System.out.println("Transaction failed: " + E.getMessage());
                    E.printStackTrace();
                }
                incrementFailedTransactionCount();
                if (transactions) {
                    try {
                        Conn.rollback();
                    } catch (SQLException E1) {
                    }
                }
            }
            return 0;
        }
