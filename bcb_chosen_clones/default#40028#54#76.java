            public void actionPerformed(ActionEvent e) {
                String sql = "insert into jadwalKBM values('" + FieldNim.getText() + "','" + FieldNama.getText() + "','" + FieldTtl.getText() + "')";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Bulan Wajib diIsi", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                    FieldNim.requestFocus();
                } else {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql);
                        statement.close();
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        JOptionPane.showMessageDialog(frame, "Simpan berhasil", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println("Error : " + exc);
                    }
                }
            }
