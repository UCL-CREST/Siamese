    CAEMURA_GUI() {
        panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CAEMURA");
        setSize(800, 670);
        JLabel labelOne = new JLabel("Client Application for Encrypted Messaging Using Rijndael Algorithm\n(CAEMURA) v0.9.1 June 26, 2009\n\n");
        labelOne.setBounds(10, 1, 800, 100);
        labelUnlock.setIcon(unlock);
        labelUnlock.setBounds(730, 570, 39, 57);
        labelLock.setIcon(lock);
        labelLock.setBounds(730, 570, 39, 57);
        mainConsole.setBounds(10, 60, 780, 500);
        mainConsole.setEditable(false);
        mainConsole.setLineWrap(true);
        mainConsole.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(mainConsole);
        scrollPane.setBounds(10, 60, 780, 500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        connect.setBounds(10, 10, 150, 30);
        connect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                CAEMURA.getIP();
                CAEMURA.connect();
            }
        });
        disconnect.setBounds(640, 10, 150, 30);
        disconnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                CAEMURA.disconnect();
            }
        });
        sendMessage.setBounds(10, 600, 150, 30);
        sendMessage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                CAEMURA.sendMessage();
            }
        });
        inputMessage.setBounds(10, 570, 700, 30);
        inputMessage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                CAEMURA.sendMessage();
            }
        });
        openFileManager.setBounds(170, 10, 150, 30);
        openFileManager.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int returnVal = fileManager.showOpenDialog(CAEMURA_GUI.this);
                if (e.getSource() == openFileManager) {
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileManager.getSelectedFile();
                        CAEMURA.mBorrowedResource.mainConsole.append("Opening: " + file.getName() + ".\n");
                    } else {
                        CAEMURA.mBorrowedResource.mainConsole.append("Open command cancelled by user.\n");
                    }
                }
            }
        });
        panel.add(labelUnlock);
        panel.add(labelOne);
        panel.add(sendMessage);
        panel.add(inputMessage);
        panel.add(disconnect);
        panel.add(connect);
        panel.add(scrollPane);
        panel.add(openFileManager);
    }
