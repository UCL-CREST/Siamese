            public void actionPerformed(ActionEvent e) {
                String sql = "insert into jadwalUjian values('" + FieldNim.getText() + "','" + FieldNama.getText() + "','" + FieldTtl.getText() + "','" + FieldJk.getText() + "')";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Mata_Pelajaran Wajib di Isi", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldJk.setText("");
                        JOptionPane.showMessageDialog(frame, "Simpan berhasil", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println("Error : " + exc);
                    }
                }
            }
