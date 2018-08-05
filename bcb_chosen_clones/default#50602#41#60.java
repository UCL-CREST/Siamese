            public void actionPerformed(ActionEvent e) {
                String sql = "insert into jjgenjang values ('" + Fieldno.getText() + "','" + Fieldalas.getText() + "','" + Fieldtinggi.getText() + "','" + Fieldluas.getText() + "')";
                if (Fieldalas.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "alas masih kosong!", "Simpan data", JOptionPane.WARNING_MESSAGE);
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
                        System.err.println("Error:" + exc);
                    }
                }
            }
