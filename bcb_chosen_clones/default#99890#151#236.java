        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnClear) {
                passwordField.setText("");
            }
            for (int i = 0; i < 10; i++) {
                if (e.getSource() == btnNumber[i]) {
                    String password = new String((passwordField.getPassword()));
                    passwordField.setText(password + i);
                }
            }
            if (e.getSource() == btnOK) {
                String password = new String((passwordField.getPassword()));
                ResultSet rs;
                Statement stmt;
                String sql;
                String result = "";
                boolean checkPassword = false;
                sql = "select password from Senhas_De_Unica_Vez where login='" + login + "'" + " and key=" + key + " ";
                try {
                    theConn = DatabaseConnection.getConnection();
                    stmt = theConn.createStatement();
                    rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        result = rs.getString("password");
                    }
                    rs.close();
                    stmt.close();
                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                        messageDigest.update(password.getBytes());
                        BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                        String output = bigInt.toString(16);
                        if (output.compareTo(result) == 0) checkPassword = true; else checkPassword = false;
                    } catch (NoSuchAlgorithmException exception) {
                        exception.printStackTrace();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                } finally {
                    try {
                        if (theConn != null) theConn.close();
                    } catch (Exception exception) {
                    }
                }
                if (checkPassword == true) {
                    JOptionPane.showMessageDialog(null, "senha correta!");
                    sql = "delete from Senhas_De_Unica_Vez where login='" + login + "'" + " and key=" + key + " ";
                    try {
                        theConn = DatabaseConnection.getConnection();
                        stmt = theConn.createStatement();
                        stmt.executeUpdate(sql);
                        stmt.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        try {
                            if (theConn != null) theConn.close();
                        } catch (Exception exception) {
                        }
                    }
                    setVisible(false);
                    setTries(0);
                    Error.log(4003, "Senha de uso �nico verificada positivamente.");
                    Error.log(4002, "Autentica��o etapa 3 encerrada.");
                    ManagerWindow mw = new ManagerWindow(login);
                    mw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    JOptionPane.showMessageDialog(null, "senha incorreta!");
                    int tries = getTries();
                    if (tries == 0) {
                        Error.log(4004, "Primeiro erro da senha de uso �nico contabilizado.");
                    } else if (tries == 1) {
                        Error.log(4005, "Segundo erro da senha de uso �nico contabilizado.");
                    } else if (tries == 2) {
                        Error.log(4006, "Terceiro erro da senha de uso �nico contabilizado.");
                        Error.log(4007, "Acesso do usuario " + login + " bloqueado pela autentica��o etapa 3.");
                        Error.log(4002, "Autentica��o etapa 3 encerrada.");
                        Error.log(1002, "Sistema encerrado.");
                        setTries(++tries);
                        System.exit(1);
                    }
                    setTries(++tries);
                }
            }
        }
