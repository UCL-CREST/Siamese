    private static void setupSystrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            };
            ActionListener optionsListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    optGUI.displayOptions(curOptions);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            MenuItem optionsItem = new MenuItem("Options");
            optionsItem.addActionListener(optionsListener);
            popup.add(optionsItem);
            popup.add(defaultItem);
            trayIcon = new TrayIcon(noMsgImage, "Google Voice Notifier", popup);
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    checkAndDisplayMessages();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            URI uri = new URI("https://www.google.com/voice/");
                            Desktop.getDesktop().browse(uri);
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            };
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
            try {
                tray.add(trayIcon);
                if (curOptions != null) {
                    login(curOptions);
                    setupTimers(curOptions);
                    checkForUpdates();
                }
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
        }
    }
