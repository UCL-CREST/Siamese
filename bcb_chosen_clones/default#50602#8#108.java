    public InputData() {
        final JButton btnJumlah;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
        } catch (Exception exc) {
            System.err.println("Error = " + exc);
        }
        final JFrame frame = new JFrame("Input Data jjgenjang");
        JLabel lno = new JLabel("       no");
        JLabel lalas = new JLabel("      alas");
        JLabel ltinggi = new JLabel("      tinggi");
        JLabel lluas = new JLabel("      luas");
        final JTextField Fieldno = new JTextField(10);
        final JTextField Fieldalas = new JTextField(10);
        final JTextField Fieldtinggi = new JTextField(10);
        final JTextField Fieldluas = new JTextField(10);
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
        JButton tombolSimpan = new JButton("Save");
        tombolSimpan.setMnemonic('S');
        tombolSimpan.addActionListener(new ActionListener() {

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
        pos.gridy = 10;
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
        pos.gridx = 5;
        konten.add(btnJumlah, pos);
        pos.gridx++;
        konten.add(tombolSimpan, pos);
        pos.gridx++;
        konten.add(tombolClear, pos);
        frame.pack();
        frame.setVisible(true);
    }
