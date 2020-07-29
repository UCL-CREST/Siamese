    public MainFrame() throws IOException {
        tabs = new MainPanel();
        new FileDrop(tabs, BorderFactory.createEmptyBorder(), new FileDrop.Listener() {

            @Override
            public void filesDropped(java.io.File[] files) {
                if (files.length != 1) return;
                try {
                    if (!checkClose()) return;
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
                open(files[0], false);
            }
        });
        panelsStack = new Stack<ElementPanel>();
        mb = new JMenuBar();
        fileMenu = new JMenu("File");
        insertMenu = new JMenu("Insert");
        aboutMenu = new JMenu("Help");
        insertPackItem = new JMenuItem("Pack");
        submitBugItem = new JMenuItem("Submit Bug Report");
        aboutItem = new JMenuItem("About");
        quitItem = new JMenuItem("Quit");
        newItem = new JMenuItem("New");
        saveItem = new JMenuItem("Save");
        saveAsItem = new JMenuItem("Save As...");
        openItem = new JMenuItem("Open...");
        checkVersionItem = new JMenuItem("Check For New Version");
        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    save();
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        });
        saveAsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveAs();
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        });
        newItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    populateTabs();
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        });
        openItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    open();
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        });
        quitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        submitBugItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI("https://sourceforge.net/tracker/?func=add&group_id=235519&atid=1096880"));
                } catch (Exception ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        });
        aboutItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                About a = new About();
                a.setIconImages(getIconImages());
                a.setTitle("About PackJacket");
                a.setLocationRelativeTo(null);
                a.setVisible(true);
            }
        });
        checkVersionItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int v = checkVersion();
                if (v == NEW_VERSION) JOptionPane.showMessageDialog(MainFrame.this, "You have a version that is not publicly released yet", "Version Check", JOptionPane.INFORMATION_MESSAGE); else if (v == SAME_VERSION) JOptionPane.showMessageDialog(MainFrame.this, "You have the latest version", "Version Check", JOptionPane.INFORMATION_MESSAGE); else if (v == UNKNOWN_VERSION) JOptionPane.showMessageDialog(MainFrame.this, "PackJacket cannot determine the latest version because there is a most likely a problem with your internet connection", "Version Check", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        CursorController.createListener(this, checkVersionItem);
        insertPackItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File f = GUIUtils.open(new FileNameExtensionFilter[] { new javax.swing.filechooser.FileNameExtensionFilter("PJC PackJacket Configuration (.pjc)", new String[] { "pjc" }), new javax.swing.filechooser.FileNameExtensionFilter("XML pack configuration file (.xml)", new String[] { "xml" }) });
                if (f != null) try {
                    if (f.getCanonicalPath().endsWith(".pjc")) try {
                        ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(f)));
                        XML x = (XML) ois.readObject();
                        ois.close();
                        InsertPack ip = new InsertPack((ArrayList<Pack>) x.packs);
                        ip.setIconImages(getIconImages());
                        ip.setLocationRelativeTo(RunnerClass.mf);
                        ip.setVisible(true);
                    } catch (IOException ex) {
                        RunnerClass.logger.log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        RunnerClass.logger.log(Level.SEVERE, null, ex);
                    } else if (f.getCanonicalPath().endsWith(".xml")) {
                        Pack p = new Pack();
                        p.xmlFile = f.getCanonicalPath();
                        RunnerClass.mf.packsPanel.packs.add(p);
                    }
                } catch (IOException ee) {
                }
            }
        });
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        openItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/open.png"))));
        newItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/new.png"))));
        saveItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/save.png"))));
        saveAsItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/saveas.png"))));
        quitItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/quit.png"))));
        submitBugItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/bug.png"))));
        aboutItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/about.png"))));
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(quitItem);
        insertMenu.add(insertPackItem);
        aboutMenu.add(submitBugItem);
        aboutMenu.add(checkVersionItem);
        aboutMenu.add(aboutItem);
        mb.add(fileMenu);
        mb.add(insertMenu);
        mb.add(aboutMenu);
        setJMenuBar(mb);
        List<Image> icons = new LinkedList<Image>();
        for (int x = 512; x >= 8; x /= 2) icons.add(ImageIO.read(getClass().getResource("/resources/logo" + x + ".png")));
        setIconImages(icons);
        remove(tabs);
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            @Override
            public void windowClosing(WindowEvent e) {
                closing();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        checkVersion();
                    }
                }).start();
            }
        });
        addWindowStateListener(new WindowAdapter() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                int state = e.getNewState();
                if (state == Frame.MAXIMIZED_BOTH || state == Frame.MAXIMIZED_HORIZ || state == Frame.MAXIMIZED_VERT) isMaximized = true; else if (state == 0) isMaximized = false;
                RunnerClass.logger.finest("resized MAX " + isMaximized + state);
                System.out.println("resized MAX " + isMaximized + state);
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                RunnerClass.logger.finest(pjResized + "resized " + c);
                System.out.println(pjResized + "resized " + c);
                if (!pjResized) {
                    userWidth = c.getWidth();
                    userHeight = c.getHeight();
                }
                pjResized = false;
            }
        });
        isMaximized = pjResized = false;
        userWidth = userHeight = -1;
        populateTabs();
        tabs.tabs.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (((JTabbedPane) e.getSource()).getSelectedIndex() == tabs.tabs.getTabCount() - 1) {
                    String item = RunnerClass.mf.processesPanel.logFilePath.getSelectedItem().toString();
                    RunnerClass.mf.processesPanel.logFilePath.setModel(GUIUtils.getAllFilesForComboBoxModel(true));
                    RunnerClass.mf.processesPanel.logFilePath.setSelectedItem(item);
                    setMinimumSize(getPreferredSize());
                }
            }
        });
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                pack();
            }
        });
    }
