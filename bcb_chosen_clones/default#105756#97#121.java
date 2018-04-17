            public void actionPerformed(ActionEvent e) {
                String sql = "update dataPegawai set Nama='" + FieldNama.getText() + "',Jk='" + FieldTtl.getText() + "',Jabatan='" + FieldJk.getText() + "',Golongan='" + FieldUsia.getText() + "',Keterangan='" + FieldAlamat.getText() + "'where NIP='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "NIP Wajib diIsi....", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldJk.setText("");
                        FieldUsia.setText("");
                        FieldAlamat.setText("");
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error :" + exc);
                    }
                }
            }
