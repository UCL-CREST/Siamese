            public void actionPerformed(ActionEvent e) {
                String sql = "delete from jjgenjang where no='" + Fieldno.getText().trim() + "'";
                if (Fieldno.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "alas masih kosong!", "Simpan data", JOptionPane.WARNING_MESSAGE);
                    Fieldno.requestFocus();
                } else {
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:odbc:jjgenjang");
                        Statement statement = conn.createStatement();
                        statement.executeUpdate(sql);
                        statement.close();
                        Fieldno.setText("");
                        Fieldalas.setText("");
                        Fieldtinggi.setText("");
                        Fieldluas.setText("");
                        Fieldalas.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error:" + exc);
                    }
                }
            }
