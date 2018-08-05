        public void keyPressed(KeyEvent k) {
            java.sql.Date da = null;
            if (k.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    int fineamt = Integer.parseInt(txtFinePerDay.getText());
                    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                    Connection con = DriverManager.getConnection("jdbc:odbc:JLibrary");
                    Statement st = con.createStatement();
                    int bookid = Integer.parseInt(informationTextField[0].getText());
                    int memid = Integer.parseInt(informationTextField[1].getText());
                    try {
                        String sql = "SELECT DayOfReturn from Borrow where MemberID=" + memid + " and BookID=" + bookid;
                        ResultSet rs = st.executeQuery(sql);
                        if (rs.next()) {
                            da = rs.getDate(1);
                            java.util.Date today = new java.util.Date();
                            System.out.println(today.after(da));
                            if (today.after(da)) {
                                long finedays = today.getTime() - da.getTime();
                                int days = (int) (finedays / (1000 * 60 * 60 * 24));
                                System.out.println(days);
                                txtTotalFineAmt.setText(String.valueOf(fineamt * days));
                            } else {
                                txtTotalFineAmt.setText("0");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Member ID entered not found on databse");
                        }
                    } catch (Exception ex1) {
                        JOptionPane.showMessageDialog(null, "Error, Cannot retrieve date value from table" + ex1.toString());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error, cannot connect to database" + ex.toString());
                }
            }
        }
