            public void actionPerformed(ActionEvent e) {
                String sql = "select * from jadwalUjian where MP='" + FieldNim.getText() + "'";
                try {
                    Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        FieldNama.setText(rs.getString(2));
                        FieldTtl.setText(rs.getString(3));
                        FieldJk.setText(rs.getString(4));
                        FieldNim.requestFocus();
                    } else {
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        FieldJk.setText("");
                        JOptionPane.showMessageDialog(frame, "Maaf,Tidak Ada Data Jadwal Ujian yang bisa di Tampilkan...", "Cari Data", JOptionPane.WARNING_MESSAGE);
                        FieldNim.requestFocus();
                    }
                    statement.close();
                    connection.close();
                } catch (Exception exc) {
                    System.err.println("Error: " + exc);
                }
            }
