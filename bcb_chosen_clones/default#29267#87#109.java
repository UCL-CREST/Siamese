            public void actionPerformed(ActionEvent e) {
                String sql = "update jadwalUjian set Semester='" + FieldNama.getText() + "',Tahun='" + FieldTtl.getText() + "',Waktu='" + FieldJk.getText() + "'where MP='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Mata_Pelajaran Wajib di Isi...", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error :" + exc);
                    }
                }
            }
