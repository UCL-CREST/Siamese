    public static void nuovoAcquisto(int quantita, Date d, double price, int id) throws SQLException {
        MyDBConnection c = new MyDBConnection();
        c.init();
        Connection conn = c.getMyConnection();
        PreparedStatement ps = conn.prepareStatement(insertAcquisto);
        ps.setInt(1, quantita);
        ps.setDate(2, d);
        ps.setDouble(3, price);
        ps.setInt(4, id);
        ps.executeUpdate();
        double newPrice = price;
        int newQ = quantita;
        ResultSet rs = MyDBConnection.executeQuery(queryPrezzo.replace("?", "" + id), conn);
        if (rs.next()) {
            int oldQ = rs.getInt(1);
            double oldPrice = rs.getDouble(2);
            newQ = quantita + oldQ;
            newPrice = (oldPrice * oldQ + price * quantita) / newQ;
            updatePortafoglio(conn, newPrice, newQ, id);
        } else insertPortafoglio(conn, id, newPrice, newQ);
        try {
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new SQLException("Effettuato rollback dopo " + e.getMessage());
        } finally {
            c.close();
        }
    }
