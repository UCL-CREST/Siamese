    public static int createEmptyCart() {
        int SHOPPING_ID = 0;
        Connection con = null;
        try {
            con = getConnection();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
        try {
            PreparedStatement insert_cart = null;
            SHOPPING_ID = Integer.parseInt(Sequence.getSequenceNumber("shopping_cart"));
            insert_cart = con.prepareStatement("INSERT INTO shopping_cart (sc_id, sc_time) VALUES ( ? , NOW() )");
            insert_cart.setInt(1, SHOPPING_ID);
            insert_cart.executeUpdate();
            con.commit();
            insert_cart.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
        return SHOPPING_ID;
    }
