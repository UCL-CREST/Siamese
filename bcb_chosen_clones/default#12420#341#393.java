    public static void adminUpdate(int i_id, double cost, String image, String thumbnail) {
        Connection con = null;
        try {
            tmpAdmin++;
            String name = "$tmp_admin" + tmpAdmin;
            con = getConnection();
            PreparedStatement related1 = con.prepareStatement("CREATE TEMPORARY TABLE " + name + " TYPE=HEAP SELECT o_id FROM orders ORDER BY o_date DESC LIMIT 10000");
            related1.executeUpdate();
            related1.close();
            PreparedStatement related2 = con.prepareStatement("SELECT ol2.ol_i_id, SUM(ol2.ol_qty) AS sum_ol FROM order_line ol, order_line ol2, " + name + " t " + "WHERE ol.ol_o_id = t.o_id AND ol.ol_i_id = ? AND ol2.ol_o_id = t.o_id AND ol2.ol_i_id <> ? " + "GROUP BY ol2.ol_i_id ORDER BY sum_ol DESC LIMIT 0,5");
            related2.setInt(1, i_id);
            related2.setInt(2, i_id);
            ResultSet rs = related2.executeQuery();
            int[] related_items = new int[5];
            int counter = 0;
            int last = 0;
            while (rs.next()) {
                last = rs.getInt(1);
                related_items[counter] = last;
                counter++;
            }
            for (int i = counter; i < 5; i++) {
                last++;
                related_items[i] = last;
            }
            rs.close();
            related2.close();
            PreparedStatement related3 = con.prepareStatement("DROP TABLE " + name);
            related3.executeUpdate();
            related3.close();
            PreparedStatement statement = con.prepareStatement("UPDATE item SET i_cost = ?, i_image = ?, i_thumbnail = ?, i_pub_date = CURRENT_DATE(), " + " i_related1 = ?, i_related2 = ?, i_related3 = ?, i_related4 = ?, i_related5 = ? WHERE i_id = ?");
            statement.setDouble(1, cost);
            statement.setString(2, image);
            statement.setString(3, thumbnail);
            statement.setInt(4, related_items[0]);
            statement.setInt(5, related_items[1]);
            statement.setInt(6, related_items[2]);
            statement.setInt(7, related_items[3]);
            statement.setInt(8, related_items[4]);
            statement.setInt(9, i_id);
            statement.executeUpdate();
            con.commit();
            statement.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
    }
