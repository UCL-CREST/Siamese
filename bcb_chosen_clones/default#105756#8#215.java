    public pegawai() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        } catch (Exception exc) {
            System.err.println("Error: " + exc);
        }
        final JFrame frame = new JFrame("Database Kepegawaian");
        frame.setLocation(250, 300);
        JLabel lnim = new JLabel("NIP");
        JLabel lnama = new JLabel("Nama");
        JLabel lttl = new JLabel("Jenis Kelamin");
        JLabel ljk = new JLabel("Jabatan ");
        JLabel lusia = new JLabel("Golongan");
        JLabel lalamat = new JLabel("Keterangan ");
        final JTextField FieldNim = new JTextField(20);
        final JTextField FieldNama = new JTextField(20);
        final JTextField FieldTtl = new JTextField(20);
        final JTextField FieldJk = new JTextField(20);
        final JTextField FieldUsia = new JTextField(20);
        final JTextField FieldAlamat = new JTextField(20);
        JButton tombolCari = new JButton("Cari");
        tombolCari.setMnemonic('C');
        tombolCari.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String sql = "select * from dataPegawai where NIP='" + FieldNim.getText() + "'";
                try {
                    Connection connection = DriverManager.getConnection("jdbc:odbc:db_Sekolah");
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        FieldNama.setText(rs.getString(2));
                        FieldTtl.setText(rs.getString(3));
                        FieldJk.setText(rs.getString(4));
                        FieldUsia.setText(rs.getString(5));
                        FieldAlamat.setText(rs.getString(6));
                        FieldNim.requestFocus();
                    } else {
                        FieldNim.setText("");
                        FieldNama.setText("");
                        FieldTtl.setText("");
                        FieldJk.setText("");
                        FieldUsia.setText("");
                        FieldAlamat.setText("");
                        JOptionPane.showMessageDialog(frame, "Data Tidak Ditemukan....", "Cari Data", JOptionPane.WARNING_MESSAGE);
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
                String sql = "insert into dataPegawai values('" + FieldNim.getText() + "','" + FieldNama.getText() + "','" + FieldTtl.getText() + "','" + FieldJk.getText() + "','" + FieldUsia.getText() + "','" + FieldAlamat.getText() + "')";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "NIP Wajib diIsi....", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldJk.setText("");
                        FieldUsia.setText("");
                        FieldAlamat.setText("");
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
                String sql = "update dataPegawai set Nama='" + FieldNama.getText() + "',Jk='" + FieldTtl.getText() + "',Jabatan='" + FieldJk.getText() + "',Golongan='" + FieldUsia.getText() + "',Keterangan='" + FieldAlamat.getText() + "'where NIP='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "NIP Wajib diIsi....", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldUsia.setText("");
                        FieldAlamat.setText("");
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
                String sql = "delete from dataPegawai where NIP='" + FieldNim.getText().trim() + "'";
                if (FieldNim.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(frame, "NIP Wajib diIsi....", "Simpan Data", JOptionPane.WARNING_MESSAGE);
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
                        FieldUsia.setText("");
                        FieldAlamat.setText("");
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
                FieldJk.setText("");
                FieldUsia.setText("");
                FieldAlamat.setText("");
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
        pos.gridx = 5;
        konten.add(ljk, pos);
        pos.gridx++;
        konten.add(FieldJk, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lusia, pos);
        pos.gridx++;
        konten.add(FieldUsia, pos);
        pos.gridy++;
        pos.gridx = 5;
        konten.add(lalamat, pos);
        pos.gridx++;
        konten.add(FieldAlamat, pos);
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
