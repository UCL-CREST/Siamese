    public ReaderX() {
        super("ReaderX - POP3 Mail Client - (C)opyRight AndSoft Inc., 2003-06");
        Options.init();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                Options.close();
            }
        });
        final JDesktopPane desktop = new JDesktopPane();
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setContentPane(desktop);
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        final JMenu mWindow = new JMenu("Window");
        JMenu mSystem = new JMenu("System");
        jmb.add(mSystem);
        JMenuItem mPOP3 = new JMenuItem("Check mail...");
        mSystem.add(mPOP3);
        mPOP3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JComboBox accountsBox = new JComboBox();
                for (Enumeration en = Options.accounts.keys(); en.hasMoreElements(); ) {
                    accountsBox.addItem(en.nextElement());
                }
                int result = JOptionPane.showConfirmDialog(ReaderX.this, accountsBox, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String name = (String) accountsBox.getSelectedItem();
                    Account account = (Account) Options.accounts.get(name);
                    POP3Frame jif = new POP3Frame(mWindow, account);
                    jif.pack();
                    jif.setVisible(true);
                    desktop.add(jif);
                }
            }
        });
        JMenuItem mMessages = new JMenuItem("Folders");
        mSystem.add(mMessages);
        mMessages.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                FoldersFrame.showUnique(desktop, mWindow);
            }
        });
        JMenuItem mBook = new JMenuItem("Address Book");
        mSystem.add(mBook);
        mBook.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AddressBook.showUnique(desktop, mWindow);
            }
        });
        JMenuItem mNew = new JMenuItem("New message");
        mSystem.add(mNew);
        mNew.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JInternalFrame jif = new Composer(null, mWindow);
                jif.setSize(640, 480);
                jif.setVisible(true);
                desktop.add(jif);
                try {
                    jif.setSelected(true);
                } catch (Exception ex) {
                    LogFrame.log(ex);
                }
            }
        });
        JMenuItem mExit = new JMenuItem("Exit");
        mSystem.add(mExit);
        mExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenu mMime = new JMenu("MIME");
        jmb.add(mMime);
        JMenuItem mMimeOpen = new JMenuItem("Open...");
        mMime.add(mMimeOpen);
        mMimeOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Options.openFileChooser.setSelectedFile(null);
                int returnVal = Options.openFileChooser.showOpenDialog(ReaderX.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = Options.openFileChooser.getSelectedFile();
                    try {
                        InputStream is = new BufferedInputStream(new FileInputStream(file));
                        MimeViewer jif = new MimeViewer(mWindow, is, file.getName());
                        is.close();
                        jif.setSize(320, 240);
                        jif.setVisible(true);
                        desktop.add(jif);
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
        });
        JMenuItem mMimeOpenMessage = new JMenuItem("Open Message...");
        mMime.add(mMimeOpenMessage);
        mMimeOpenMessage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Options.openFileChooser.setSelectedFile(null);
                int returnVal = Options.openFileChooser.showOpenDialog(ReaderX.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = Options.openFileChooser.getSelectedFile();
                    try {
                        InputStream is = new BufferedInputStream(new FileInputStream(file));
                        MimeMessage msg = new MimeMessage(Options.session, is);
                        is.close();
                        MessageViewer msgView = new MessageViewer(msg, mWindow);
                        msgView.setSize(640, 480);
                        msgView.show();
                        desktop.add(msgView);
                        msgView.setSelected(true);
                    } catch (Exception ex) {
                        LogFrame.log(ex);
                    }
                }
            }
        });
        JMenu mOptions = new JMenu("Options");
        jmb.add(mOptions);
        JMenuItem mAccounts = new JMenuItem("Accounts");
        mOptions.add(mAccounts);
        mAccounts.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AccountEditor.showUnique(desktop, mWindow);
            }
        });
        JMenuItem mSMTP = new JMenuItem("SMTP...");
        mOptions.add(mSMTP);
        mSMTP.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JPanel jp = new JPanel(new GridLayout(5, 2));
                jp.add(new JLabel("Server:"));
                JTextField server = new JTextField(Options.smtpServer);
                jp.add(server);
                final JTextField username = new JTextField(Options.smtpUserID);
                final JPasswordField password = new JPasswordField(Options.smtpPasswd);
                jp.add(new JLabel("Relay"));
                final JCheckBox auth = new JCheckBox("AUTH", Options.smtpUserID != null);
                jp.add(auth);
                auth.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        username.setEnabled(auth.isSelected());
                        password.setEnabled(auth.isSelected());
                    }
                });
                jp.add(new JLabel("Username:"));
                jp.add(username);
                jp.add(new JLabel("Password:"));
                jp.add(password);
                if (JOptionPane.showConfirmDialog(ReaderX.this, jp, "SMTP server", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    Options.smtpServer = server.getText();
                    if (auth.isSelected()) {
                        Options.smtpUserID = username.getText();
                        Options.smtpPasswd = new String(password.getPassword());
                    } else {
                        Options.smtpUserID = null;
                        Options.smtpPasswd = null;
                    }
                }
            }
        });
        JMenuItem mPreferences = new JMenuItem("Preferences...");
        mOptions.add(mPreferences);
        mPreferences.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JPanel jp = new JPanel(new GridLayout(2, 2));
                jp.add(new JLabel("Connections:"));
                final JCheckBox debug = new JCheckBox("debug", Options.debug);
                jp.add(debug);
                jp.add(new JLabel("Message ID:"));
                final JTextField msgID = new JTextField(Options.messageID);
                jp.add(msgID);
                if (JOptionPane.showConfirmDialog(ReaderX.this, jp, "Preferences", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    Options.debug = debug.isSelected();
                    Options.messageID = msgID.getText();
                }
            }
        });
        jmb.add(mWindow);
        JMenu mHelp = new JMenu("Help");
        jmb.add(mHelp);
        JMenuItem mAbout = new JMenuItem("About...");
        mHelp.add(mAbout);
        mAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String notice = "POP3 Mail Client\n(C)opyRight AndSoft Inc., 2003-06\nVersion " + Version.ver;
                JOptionPane.showMessageDialog(ReaderX.this, notice, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        LogFrame lf = new LogFrame(mWindow);
        lf.setSize(320, 240);
        lf.setVisible(true);
        desktop.add(lf);
        StreamLog sl = new StreamLog(mWindow);
        sl.setSize(320, 240);
        sl.setVisible(true);
        desktop.add(sl);
        setSize(800, 600);
        setVisible(true);
    }
