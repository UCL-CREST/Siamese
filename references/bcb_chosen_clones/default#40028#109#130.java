            public void actionPerformed(ActionEvent e) {
                String sql = "delete from jadwalKBM where Bulan='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Bulan Wajib diIsi..", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                    FieldNim.requestFocus();
                } else {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql);
                        statement.close();
                        connection.close();
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error: " + exc);
                    }
                }
            }
