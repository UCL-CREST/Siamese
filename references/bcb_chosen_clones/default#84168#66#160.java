        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 5; i++) {
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
                boolean checkPassword1 = false;
                boolean checkPassword2 = false;
                sql = "select password from Usuarios where login='" + login + "'";
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
                        Tree tree1 = CreateTree(password, 0);
                        Tree tree2 = CreateTree(password, 1);
                        tree1.enumerateTree(tree1.root);
                        tree2.enumerateTree(tree2.root);
                        for (int i = 0; i < tree1.passwdVector.size(); i++) {
                            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                            messageDigest.update(tree1.passwdVector.get(i).getBytes());
                            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                            String output = bigInt.toString(16);
                            if (output.compareTo(result) == 0) {
                                checkPassword1 = true;
                                break;
                            } else checkPassword1 = false;
                        }
                        for (int i = 0; i < tree2.passwdVector.size(); i++) {
                            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                            messageDigest.update(tree2.passwdVector.get(i).getBytes());
                            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                            String output = bigInt.toString(16);
                            if (output.compareTo(result) == 0) {
                                checkPassword2 = true;
                                break;
                            } else checkPassword2 = false;
                        }
                        if (checkPassword1 == true || checkPassword2 == true) checkPassword = true; else checkPassword = false;
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
                    setTries(0);
                    setVisible(false);
                    Error.log(3003, "Senha pessoal verificada positivamente.");
                    Error.log(3002, "Autentica��o etapa 2 encerrada.");
                    PasswordTableWindow ptw = new PasswordTableWindow(login);
                    ptw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    JOptionPane.showMessageDialog(null, "senha incorreta!");
                    Error.log(3004, "Senha pessoal verificada negativamente.");
                    int tries = getTries();
                    if (tries == 0) {
                        Error.log(3005, "Primeiro erro da senha pessoal contabilizado.");
                    } else if (tries == 1) {
                        Error.log(3006, "Segundo erro da senha pessoal contabilizado.");
                    } else if (tries == 2) {
                        Error.log(3007, "Terceiro erro da senha pessoal contabilizado.");
                        Error.log(3008, "Acesso do usuario " + login + " bloqueado pela autentica��o etapa 2.");
                        Error.log(3002, "Autentica��o etapa 2 encerrada.");
                        Error.log(1002, "Sistema encerrado.");
                        setTries(++tries);
                        System.exit(1);
                    }
                    setTries(++tries);
                }
            }
            if (e.getSource() == btnClear) {
                passwordField.setText("");
            }
        }
