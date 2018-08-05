    public void testBlobB() {
        ResultSet rs;
        byte[] ba;
        byte[] baR1 = new byte[] { (byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4, (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, (byte) 0xF8, (byte) 0xF9, (byte) 0xFA, (byte) 0xFB };
        byte[] baR2 = new byte[] { (byte) 0xE1, (byte) 0xE2, (byte) 0xE3, (byte) 0xE4, (byte) 0xE5, (byte) 0xE6, (byte) 0xE7, (byte) 0xE8, (byte) 0xE9, (byte) 0xEA, (byte) 0xEB };
        try {
            connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            st.executeUpdate("DROP TABLE blo IF EXISTS");
            st.executeUpdate("CREATE TABLE blo (id INTEGER, b blob( 100))");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO blo(id, b) values(2, ?)");
            ps.setBlob(1, new SerialBlob(baR1));
            ps.executeUpdate();
            rs = st.executeQuery("SELECT b FROM blo WHERE id = 2");
            if (!rs.next()) {
                assertTrue("No row with id 2", false);
            }
            java.sql.Blob blob1 = rs.getBlob("b");
            System.out.println("Size of retrieved blob: " + blob1.length());
            byte[] baOut = blob1.getBytes(1, (int) blob1.length());
            if (baOut.length != baR1.length) {
                assertTrue("Expected array len " + baR1.length + ", got len " + baOut.length, false);
            }
            for (int i = 0; i < baOut.length; i++) {
                if (baOut[i] != baR1[i]) {
                    assertTrue("Expected array len " + baR1.length + ", got len " + baOut.length, false);
                }
            }
            rs.close();
            rs = st.executeQuery("SELECT b FROM blo WHERE id = 2");
            if (!rs.next()) {
                assertTrue("No row with id 2", false);
            }
            blob1 = rs.getBlob("b");
            ba = blob1.getBytes(1, baR2.length);
            if (ba.length != baR2.length) {
                assertTrue("row2 byte length differs", false);
            }
            for (int i = 0; i < ba.length; i++) {
                if (ba[i] != baR1[i]) {
                    assertTrue("row2 byte " + i + " differs", false);
                }
            }
            rs.close();
            connection.rollback();
            ps.setBinaryStream(1, new HsqlByteArrayInputStream(baR1), baR1.length);
            ps.executeUpdate();
            rs = st.executeQuery("SELECT b FROM blo WHERE id = 2");
            if (!rs.next()) {
                assertTrue("No row with id 2", false);
            }
            blob1 = rs.getBlob("b");
            System.out.println("Size of retrieved blob: " + blob1.length());
            baOut = blob1.getBytes(1, (int) blob1.length());
            if (baOut.length != baR1.length) {
                assertTrue("Expected array len " + baR1.length + ", got len " + baOut.length, false);
            }
            for (int i = 0; i < baOut.length; i++) {
                if (baOut[i] != baR1[i]) {
                    assertTrue("Expected array len " + baR1.length + ", got len " + baOut.length, false);
                }
            }
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("test failure");
        }
    }
