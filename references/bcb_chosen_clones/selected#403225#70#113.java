    public synchronized void checkout() throws SQLException, InterruptedException {
        Connection con = this.session.open();
        con.setAutoCommit(false);
        String sql_stmt = DB2SQLStatements.shopping_cart_getAll(this.customer_id);
        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet res = stmt.executeQuery(sql_stmt);
        res.last();
        int rowcount = res.getRow();
        res.beforeFirst();
        ShoppingCartItem[] resArray = new ShoppingCartItem[rowcount];
        int i = 0;
        while (res.next()) {
            resArray[i] = new ShoppingCartItem();
            resArray[i].setCustomer_id(res.getInt("customer_id"));
            resArray[i].setDate_start(res.getDate("date_start"));
            resArray[i].setDate_stop(res.getDate("date_stop"));
            resArray[i].setRoom_type_id(res.getInt("room_type_id"));
            resArray[i].setNumtaken(res.getInt("numtaken"));
            resArray[i].setTotal_price(res.getInt("total_price"));
            i++;
        }
        this.wait(4000);
        try {
            for (int j = 0; j < rowcount; j++) {
                sql_stmt = DB2SQLStatements.room_date_update(resArray[j]);
                stmt = con.createStatement();
                stmt.executeUpdate(sql_stmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
        }
        for (int j = 0; j < rowcount; j++) {
            System.out.println(j);
            sql_stmt = DB2SQLStatements.booked_insert(resArray[j], 2);
            stmt = con.createStatement();
            stmt.executeUpdate(sql_stmt);
        }
        sql_stmt = DB2SQLStatements.shopping_cart_deleteAll(this.customer_id);
        stmt = con.createStatement();
        stmt.executeUpdate(sql_stmt);
        con.commit();
        this.session.close(con);
    }
