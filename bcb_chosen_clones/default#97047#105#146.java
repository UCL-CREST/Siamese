            public void actionPerformed(ActionEvent ae) {
                if (isCorrect()) {
                    Thread runner = new Thread() {

                        public void run() {
                            book = new Books();
                            book.connection("SELECT BookID FROM Books WHERE ISBN = '" + data[7] + "'");
                            String ISBN = book.getISBN();
                            if (!data[7].equalsIgnoreCase(ISBN)) {
                                try {
                                    String sql = "INSERT INTO Books (Subject,Title,Author,Publisher,Copyright," + "Edition,Pages,ISBN,NumberOfBooks,NumberOfAvailbleBooks,Library,Availble,ShelfNo) VALUES " + " (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                                    Connection con = DriverManager.getConnection("jdbc:odbc:JLibrary");
                                    PreparedStatement ps = con.prepareStatement(sql);
                                    ps.setString(1, data[0]);
                                    ps.setString(2, data[1]);
                                    ps.setString(3, data[2]);
                                    ps.setString(4, data[3]);
                                    ps.setInt(5, Integer.parseInt(data[4]));
                                    ps.setInt(6, Integer.parseInt(data[5]));
                                    ps.setInt(7, Integer.parseInt(data[6]));
                                    ps.setString(8, data[7]);
                                    ps.setInt(9, Integer.parseInt(data[8]));
                                    ps.setInt(10, Integer.parseInt(data[8]));
                                    ps.setString(11, data[9]);
                                    ps.setBoolean(12, availble);
                                    ps.setInt(13, Integer.parseInt(txtShelfNo.getText()));
                                    ps.executeUpdate();
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, ex.toString());
                                }
                                clearTextField();
                            } else {
                                JOptionPane.showMessageDialog(null, "The book is in the library", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };
                    runner.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Please, complete the information", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
