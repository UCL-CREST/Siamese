        public void run() {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root&password=&enableBlobStreaming=true");
                ResultSet rs = executeQuery(conn, "select n_text from mybs_test_tab order by n_id");
                while (rs.next()) {
                    Blob blob = rs.getBlob(1);
                    InputStream in = blob.getBinaryStream();
                    try {
                        for (int i = 0; i < blob.length(); i++) {
                            int ch = in.read();
                            if (ch != (i % 256)) throw new IOException("Error reading stream");
                        }
                    } finally {
                        in.close();
                    }
                }
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                ex.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
