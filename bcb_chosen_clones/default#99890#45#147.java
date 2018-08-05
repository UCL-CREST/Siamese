    PasswordTableWindow(String login) {
        super(login + ", tecle a senha de uso �nico");
        this.login = login;
        Error.log(4001, "Autentica��o etapa 3 iniciada.");
        Container container = getContentPane();
        container.setLayout(new FlowLayout());
        btnNumber = new JButton[10];
        btnOK = new JButton("OK");
        btnClear = new JButton("Limpar");
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 10));
        ResultSet rs;
        Statement stmt;
        String sql;
        Vector<Integer> result = new Vector<Integer>();
        sql = "select key from Senhas_De_Unica_Vez where login='" + login + "'";
        try {
            theConn = DatabaseConnection.getConnection();
            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getInt("key"));
            }
            rs.close();
            stmt.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (theConn != null) theConn.close();
            } catch (Exception exception) {
            }
        }
        Random rn = new Random();
        int r = rn.nextInt();
        if (result.size() == 0) {
            rn = new Random();
            Vector<Integer> passwordVector = new Vector<Integer>();
            Vector<String> hashVector = new Vector<String>();
            for (int i = 0; i < 10; i++) {
                r = rn.nextInt() % 10000;
                if (r < 0) r = r * (-1);
                passwordVector.add(r);
            }
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(login + ".txt", false));
                for (int i = 0; i < 10; i++) {
                    out.append("" + i + " " + passwordVector.get(i) + "\n");
                }
                out.close();
                try {
                    for (int i = 0; i < 10; i++) {
                        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                        messageDigest.update(passwordVector.get(i).toString().getBytes());
                        BigInteger bigInt = new BigInteger(1, messageDigest.digest());
                        String digest = bigInt.toString(16);
                        sql = "insert into Senhas_De_Unica_Vez (login,key,password) values " + "('" + login + "'," + i + ",'" + digest + "')";
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
                    }
                } catch (NoSuchAlgorithmException exception) {
                    exception.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "nova tabela de senhas criada para o usu�rio " + login + ".");
            Error.log(1002, "Sistema encerrado");
            System.exit(0);
        }
        if (r < 0) r = r * (-1);
        int index = r % result.size();
        if (index > result.size()) index = 0;
        key = result.get(index);
        labelKey = new JLabel("Chave n�mero " + key + " ");
        passwordField = new JPasswordField(12);
        ButtonHandler handler = new ButtonHandler();
        for (int i = 0; i < 10; i++) {
            btnNumber[i] = new JButton("" + i);
            buttonPanel.add(btnNumber[i]);
            btnNumber[i].addActionListener(handler);
        }
        btnOK.addActionListener(handler);
        btnClear.addActionListener(handler);
        container.add(buttonPanel);
        container.add(passwordField);
        container.add(labelKey);
        container.add(btnOK);
        container.add(btnClear);
        setSize(325, 200);
        setVisible(true);
    }
