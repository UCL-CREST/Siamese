    public AddBooks() {
        super("Add Books", false, true, false, true);
        setFrameIcon(new ImageIcon(ClassLoader.getSystemResource("images/Add16.gif")));
        Container cp = getContentPane();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        northLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        northPanel.add(northLabel);
        cp.add("North", northPanel);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Add a new book:"));
        informationLabelPanel.setLayout(new GridLayout(11, 1, 1, 1));
        for (int i = 0; i < informationLabel.length; i++) {
            informationLabelPanel.add(informationLabel[i] = new JLabel(informationString[i]));
            informationLabel[i].setFont(new Font("Tahoma", Font.BOLD, 11));
        }
        centerPanel.add("West", informationLabelPanel);
        informationTextFieldPanel.setLayout(new GridLayout(11, 1, 1, 1));
        for (int i = 0; i < informationTextField.length; i++) {
            informationTextFieldPanel.add(informationTextField[i] = new JTextField(25));
            informationTextField[i].setFont(new Font("Tahoma", Font.PLAIN, 11));
        }
        lblShelfNo.setFont(new Font("Tahoma", Font.BOLD, 11));
        informationLabelPanel.add(lblShelfNo);
        txtShelfNo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        informationTextFieldPanel.add(txtShelfNo);
        centerPanel.add("East", informationTextFieldPanel);
        insertInformationButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertInformationButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        insertInformationButtonPanel.add(insertInformationButton);
        centerPanel.add("South", insertInformationButtonPanel);
        cp.add("Center", centerPanel);
        southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        OKButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        southPanel.add(OKButton);
        southPanel.setBorder(BorderFactory.createEtchedBorder());
        cp.add("South", southPanel);
        insertInformationButton.addActionListener(new ActionListener() {

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
        });
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        setVisible(true);
        pack();
    }
