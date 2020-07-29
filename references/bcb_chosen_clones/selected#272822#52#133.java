    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("a", "advanced", false, "enable advanced features");
        options.addOption("help", false, "display usage information");
        boolean advancedModeTmp = false;
        CommandLineParser cmdParser = new PosixParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLine cmd = cmdParser.parse(options, args);
            if (cmd.hasOption("a")) advancedModeTmp = true;
            if (cmd.hasOption("help")) {
                helpFormatter.printHelp("Reversi_Contender", options);
                return;
            }
        } catch (ParseException ex) {
            System.out.println("issue reading command-line arguments\r\n");
            helpFormatter.printHelp("Reversi_Contender", options);
            return;
        }
        final boolean advancedMode = advancedModeTmp;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                GameSession.getInstance().setAdvancedMode(advancedMode);
                final JFrame frame = new JFrame("Reversi Contender");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                getGameContentPanel(frame);
                JMenuBar menuBar = new JMenuBar();
                JMenu gameMenu = new JMenu("Game");
                JMenuItem newGameMenuItem = new JMenuItem("New Game");
                newGameMenuItem.addActionListener(new NewGameActionListener(frame));
                gameMenu.add(newGameMenuItem);
                JMenuItem exitGameMenuItem = new JMenuItem("Exit");
                exitGameMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        System.exit(0);
                    }
                });
                gameMenu.add(exitGameMenuItem);
                menuBar.add(gameMenu);
                JMenu editMenu = new JMenu("Edit");
                JMenuItem prefsItemMenu = new JMenuItem("Preferences");
                prefsItemMenu.addActionListener(new PreferencesActionListener(frame));
                editMenu.add(prefsItemMenu);
                menuBar.add(editMenu);
                JMenu helpMenu = new JMenu("Help");
                JMenuItem homePageMenuItem = new JMenuItem("Visit Homepage");
                homePageMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.browse(new URI("http://code.google.com/p/reversi-contender"));
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(frame, "We can't seem to open your browser, sorry!", "Browser Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                if (!Desktop.isDesktopSupported()) {
                    homePageMenuItem.setEnabled(false);
                }
                helpMenu.add(homePageMenuItem);
                JMenuItem versionCheckItem = new JMenuItem("Check for new version");
                versionCheckItem.addActionListener(new CheckVersionActionListener(frame));
                helpMenu.add(versionCheckItem);
                JMenuItem aboutMenuItem = new JMenuItem("About");
                aboutMenuItem.addActionListener(new AboutActionListener(frame));
                helpMenu.add(aboutMenuItem);
                menuBar.add(helpMenu);
                frame.setJMenuBar(menuBar);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
