            public void actionPerformed(ActionEvent e) {
                String sql = "select * from jjgenjang where no='" + Fieldno.getText() + "'";
                try {
                    Connection conn = DriverManager.getConnection("jdbc:odbc:jjgenjang");
                    Statement statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        Fieldalas.setText(rs.getString(2));
                        Fieldtinggi.setText(rs.getString(3));
                        Fieldluas.setText(rs.getString(4));
                        Fieldluas.requestFocus();
                    } else {
                        Fieldluas.setText("");
                        JOptionPane.showMessageDialog(frame, "alas tidak ditemukan dalam database!", "Cari Data", JOptionPane.WARNING_MESSAGE);
                        Fieldno.requestFocus();
                    }
                    statement.close();
                    conn.close();
                } catch (Exception exc) {
                    System.err.println("Error : " + exc);
                }
            }
