    public boolean initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 600));
        setJMenuBar(createMenuBar());
        setContentPane(createContentPane());
        backupReader = new BackupReader();
        infoReader = new InfoReader();
        findMyIPhoneReader = FindMyIPhoneReader.getInstance();
        String systemOs = System.getProperty("os.name");
        String dataPath = null;
        String laf = UIManager.getCrossPlatformLookAndFeelClassName();
        if (systemOs.startsWith("Windows")) {
            dataPath = System.getenv("APPDATA") + FILE_SEPARATOR + "Apple Computer" + FILE_SEPARATOR + FOLDER_PATH;
            laf = UIManager.getSystemLookAndFeelClassName();
        } else if (systemOs.startsWith("Mac OS")) {
            laf = UIManager.getSystemLookAndFeelClassName();
            dataPath = System.getProperty("user.home") + "" + "/Library/Application Support/" + FOLDER_PATH;
        } else {
            logger.log(Level.INFO, "{0} is not supported! Please manually select" + " a backup folder.", systemOs);
        }
        if (laf != null) {
            try {
                UIManager.setLookAndFeel(laf);
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        final String dataPathFinal = dataPath;
        if (dataPathFinal != null) {
            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    addStatus("Loading phones in '" + dataPathFinal + "'");
                    File dir = new File(dataPathFinal);
                    String[] children = dir.list();
                    if (children == null) {
                    } else {
                        for (int i = 0; i < children.length; i++) {
                            String filename = children[i];
                            if (!filename.contains("-")) {
                                String backupFolder = dataPathFinal + FILE_SEPARATOR + filename + FILE_SEPARATOR;
                                addBackupFolder(backupFolder, false);
                            }
                        }
                    }
                    return null;
                }
            };
            worker.execute();
        }
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        if (VersionCheck.getLatestVersion() != null) {
            int reply = JOptionPane.showConfirmDialog(null, "iPhoneStalker is out of date!\n" + "Would you like to visit the iPhoneStalker homepage?", "New Version!", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                if (!Desktop.isDesktopSupported()) {
                    JOptionPane.showMessageDialog(null, "Unable to open browser.\n" + "Please visit http://iphonestalker.googlecode.com");
                } else {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI("http://iphonestalker.googlecode.com"));
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Unable to open browser.\n" + "Please visit http://iphonestalker.googlecode.com");
                        } catch (URISyntaxException ex) {
                            logger.log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Unable to open browser.\n" + "Please visit http://iphonestalker.googlecode.com");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to open browser.\n" + "Please visit http://iphonestalker.googlecode.com");
                    }
                }
            }
        }
        initialized = true;
        return initialized;
    }
