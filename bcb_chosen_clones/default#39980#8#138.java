    public HapusData() {
        final JButton btnJumlah;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        } catch (Exception exc) {
            System.err.println("Error = " + exc);
        }
        final JFrame frame = new JFrame("Delet Data jajarangenjang");
        JLabel lno = new JLabel("no");
        JLabel lalas = new JLabel("alas");
        JLabel ltinggi = new JLabel("tinggi");
        JLabel lluas = new JLabel("luas");
        final JTextField Fieldno = new JTextField(10);
        final JTextField Fieldalas = new JTextField(10);
        final JTextField Fieldtinggi = new JTextField(10);
        final JTextField Fieldluas = new JTextField(10);
        JButton tombolCari = new JButton("Cari");
        tombolCari.setMnemonic('C');
        tombolCari.addActionListener(new ActionListener() {

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
        });
        btnJumlah = new JButton("Hitung luas");
        btnJumlah.setMnemonic('H');
        btnJumlah.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnJumlah) {
                    int bilangan1 = (Integer.parseInt(Fieldalas.getText().trim()));
                    int bilangan2 = (Integer.parseInt(Fieldtinggi.getText().trim()));
                    int luas = bilangan1 * bilangan2;
                    Fieldluas.setText(String.valueOf(luas));
                }
            }
        });
        JButton tombolHapus = new JButton("Delete");
        tombolHapus.setMnemonic('D');
        tombolHapus.addActionListener(new ActionListener() {

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
        });
        JButton tombolClear = new JButton("Clear");
        tombolClear.setMnemonic('c');
        tombolClear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Fieldno.setText("");
                Fieldalas.setText("");
                Fieldtinggi.setText("");
                Fieldluas.setText("");
                Fieldalas.requestFocus();
            }
        });
        Container konten;
        konten = frame.getContentPane();
        konten.setLayout(new GridBagLayout());
        GridBagConstraints pos = new GridBagConstraints();
        pos.anchor = GridBagConstraints.WEST;
        pos.gridx = 5;
        pos.gridy = 5;
        konten.add(lno, pos);
        pos.gridx++;
        konten.add(Fieldno, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lalas, pos);
        pos.gridx++;
        konten.add(Fieldalas, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(ltinggi, pos);
        pos.gridx++;
        konten.add(Fieldtinggi, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lluas, pos);
        pos.gridx++;
        konten.add(Fieldluas, pos);
        pos.gridy++;
        konten.add(btnJumlah, pos);
        pos.gridx++;
        konten.add(tombolCari, pos);
        pos.gridx++;
        konten.add(tombolClear, pos);
        pos.gridy++;
        konten.add(tombolHapus, pos);
        frame.pack();
        frame.setVisible(true);
    }
