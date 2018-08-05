    public jadwalKBM() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (Exception exc) {
            System.err.println("Error: " + exc);
        }
        final JFrame frame = new JFrame("Database Jadwal KBM");
        frame.setLocation(250, 300);
        JLabel lnim = new JLabel("Bulan");
        JLabel lnama = new JLabel("Tahun");
        JLabel lttl = new JLabel("Banyaknya Pertemuan");
        final JTextField FieldNim = new JTextField(20);
        final JTextField FieldNama = new JTextField(20);
        final JTextField FieldTtl = new JTextField(20);
        JButton tombolCari = new JButton("Cari");
        tombolCari.setMnemonic('C');
        tombolCari.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sql = "select * from jadwalKBM where Bulan='" + FieldNim.getText() + "'";
                try {
                    Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        FieldNama.setText(rs.getString(2));
                        FieldTtl.setText(rs.getString(3));
                        FieldNim.requestFocus();
                    } else {
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        JOptionPane.showMessageDialog(frame, "Maaf,Tidak Ada KBM yang Di Temukan", "Cari Data", JOptionPane.WARNING_MESSAGE);
                        FieldNim.requestFocus();
                    }
                    statement.close();
                    connection.close();
                } catch (Exception exc) {
                    System.err.println("Error: " + exc);
                }
            }
        });
        JButton tombolSimpan = new JButton("Simpan");
        tombolSimpan.setMnemonic('S');
        tombolSimpan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sql = "insert into jadwalKBM values('" + FieldNim.getText() + "','" + FieldNama.getText() + "','" + FieldTtl.getText() + "')";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Bulan Wajib diIsi", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                    FieldNim.requestFocus();
                } else {
                    try {
                        Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(sql);
                        statement.close();
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        JOptionPane.showMessageDialog(frame, "Simpan berhasil", "Simpan Data", JOptionPane.WARNING_MESSAGE);
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println("Error : " + exc);
                    }
                }
            }
        });
        JButton tombolUbah = new JButton("Ubah");
        tombolUbah.setMnemonic('U');
        tombolUbah.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sql = "update jadwalKBM set Tahun='" + FieldNama.getText() + "',Pertemuan='" + FieldTtl.getText() + "'where Bulan='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Bulan Wajib diIsi", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error :" + exc);
                    }
                }
            }
        });
        JButton tombolHapus = new JButton("Hapus");
        tombolHapus.setMnemonic('H');
        tombolHapus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sql = "delete from jadwalKBM where Bulan='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Nama Bulan Wajib diIsi..", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldNim.requestFocus();
                    } catch (Exception exc) {
                        System.err.println(sql);
                        System.err.println("Error: " + exc);
                    }
                }
            }
        });
        JButton tombolClear = new JButton("Baru");
        tombolClear.setMnemonic('B');
        tombolClear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FieldNim.setText("");
                FieldNama.setText("");
                FieldTtl.setText("");
                FieldNim.requestFocus();
            }
        });
        Container konten;
        konten = frame.getContentPane();
        konten.setLayout(new GridBagLayout());
        GridBagConstraints pos = new GridBagConstraints();
        pos.anchor = GridBagConstraints.WEST;
        pos.gridx = 5;
        pos.gridy = 5;
        konten.add(lnim, pos);
        pos.gridx++;
        konten.add(FieldNim, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lnama, pos);
        pos.gridx++;
        konten.add(FieldNama, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lttl, pos);
        pos.gridx++;
        konten.add(FieldTtl, pos);
        pos.gridy++;
        pos.gridx = 7;
        konten.add(tombolSimpan, pos);
        pos.gridx++;
        konten.add(tombolClear, pos);
        pos.gridx++;
        konten.add(tombolCari, pos);
        pos.gridx++;
        konten.add(tombolUbah, pos);
        pos.gridx++;
        konten.add(tombolHapus, pos);
        frame.pack();
        frame.setVisible(true);
    }
