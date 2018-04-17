    public void run() {
        try {
            this.initialIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(IMAGE_INITIAL_ICON));
            this.monitoringExceptionIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(IMAGE_MONITORING_EXCEPTION));
            this.buildSuccessIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(IMAGE_BUILD_SUCCESS));
            this.buildFailureIcon = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(IMAGE_BUILD_FAILURE));
            this.aboutIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(IMAGE_ABOUT_ICON)));
            if (SystemTray.isSupported()) {
                this.openBuildServerHomePageActionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(monitor.getMainPageURI());
                            } catch (IOException err) {
                            }
                        }
                    }
                };
                SystemTray tray = SystemTray.getSystemTray();
                PopupMenu trayMenu = new PopupMenu();
                MenuItem buildServerHomePageMenuItem = new MenuItem(this.monitor.getMonitoredBuildSystemName() + " " + getMessage(MESSAGEKEY_TRAYICON_MENUITEM_BUILD_SERVER_HOME_PAGE_SUFFIX));
                buildServerHomePageMenuItem.addActionListener(this.openBuildServerHomePageActionListener);
                trayMenu.add(buildServerHomePageMenuItem);
                MenuItem updateStatusNowMenuItem = new MenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_UPDATE_STATUS_NOW));
                ActionListener updateStatusNowMenuItemActionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        monitorThread.interrupt();
                    }
                };
                updateStatusNowMenuItem.addActionListener(updateStatusNowMenuItemActionListener);
                trayMenu.add(updateStatusNowMenuItem);
                Menu sortMenu = new Menu(getMessage(MESSAGEKEY_TRAYICON_MENU_SORT));
                this.sortByAgeMenuItem = new CheckboxMenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_SORT_BY_AGE), true);
                this.currentSortOrder = SORT_BY_AGE;
                sortMenu.add(this.sortByAgeMenuItem);
                this.sortByNameMenuItem = new CheckboxMenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_SORT_BY_NAME), false);
                sortMenu.add(this.sortByNameMenuItem);
                ItemListener sortByNameMenuItemActionListener = new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            sortByNameMenuItem.setState(true);
                            sortByAgeMenuItem.setState(false);
                            currentSortOrder = SORT_BY_NAME;
                            reportConfigurationUpdatedToBeTakenIntoAccountImmediately();
                        } else {
                            sortByNameMenuItem.setState(true);
                            reportConfigurationUpdatedToBeTakenIntoAccountImmediately();
                        }
                    }
                };
                this.sortByNameMenuItem.addItemListener(sortByNameMenuItemActionListener);
                ItemListener sortByAgeMenuItemActionListener = new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            sortByNameMenuItem.setState(false);
                            sortByAgeMenuItem.setState(true);
                            currentSortOrder = SORT_BY_AGE;
                            reportConfigurationUpdatedToBeTakenIntoAccountImmediately();
                        } else {
                            sortByAgeMenuItem.setState(true);
                            reportConfigurationUpdatedToBeTakenIntoAccountImmediately();
                        }
                    }
                };
                this.sortByAgeMenuItem.addItemListener(sortByAgeMenuItemActionListener);
                trayMenu.add(sortMenu);
                MenuItem optionsMenuItem = new MenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_OPTIONS));
                this.openOptionsDialogActionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            monitor.displayOptionsDialog();
                        } catch (Exception exc) {
                            panic(exc);
                        }
                    }
                };
                optionsMenuItem.addActionListener(this.openOptionsDialogActionListener);
                trayMenu.add(optionsMenuItem);
                trayMenu.addSeparator();
                this.indexOfTheFirstBuildResultMenuItem = trayMenu.getItemCount();
                MenuItem aboutMenuItem = new MenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_ABOUT));
                ActionListener aboutMenuItemActionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "This is the preview version of build monitor, by sbrunot@gmail.com.\nBuild Revision: unknown\nCurrent monitor is the Bamboo monitor.\n\n", "About...", JOptionPane.INFORMATION_MESSAGE, aboutIcon);
                    }
                };
                aboutMenuItem.addActionListener(aboutMenuItemActionListener);
                trayMenu.add(aboutMenuItem);
                Menu exitMenu = new Menu(getMessage(MESSAGEKEY_TRAYICON_MENU_EXIT));
                MenuItem exitMenuItem = new MenuItem(getMessage(MESSAGEKEY_TRAYICON_MENUITEM_EXIT));
                ActionListener exitMenuItemActionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                };
                exitMenuItem.addActionListener(exitMenuItemActionListener);
                exitMenu.add(exitMenuItem);
                trayMenu.add(exitMenu);
                this.numberOfItemInEmptyTrayMenu = trayMenu.getItemCount();
                this.trayIcon = new TrayIcon(this.initialIcon, getMessage(MESSAGEKEY_TRAYICON_INITIAL_TOOLTIP), trayMenu);
                tray.add(trayIcon);
            } else {
                panic(getMessage(MESSAGEKEY_ERROR_SYSTEMTRAY_NOT_SUPPORTED));
            }
            Runtime.getRuntime().addShutdownHook(new ShutdownThread());
            this.monitorThread = new Thread(this.monitor, "Bamboo monitor thread");
            this.monitorThread.start();
            this.trayIcon.addActionListener(openBuildServerHomePageActionListener);
        } catch (Throwable t) {
            panic(t);
        }
    }
