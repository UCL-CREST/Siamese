    public MenuBar() {
        applicationMenu = new JMenu("Application");
        appNew = new JMenuItem("New Application...");
        appNew.setMnemonic(KeyEvent.VK_N);
        appNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
        appNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataManager dm = DataManager.instance();
                    RTDocument rtDoc = dm.getRTDocument();
                    RT rt = rtDoc.getRT();
                    NewApplicationDialog newAppWindow = new NewApplicationDialog(MetaManager.getMainWindow(), true, rt);
                    newAppWindow.setVisible(true);
                } catch (Exception ex) {
                    logger.debug(ex, ex);
                }
            }
        });
        applicationMenu.add(appNew);
        applicationMenu.addSeparator();
        appImportXCCDF = new JMenuItem("Import XCCDF...");
        appImportXCCDF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Import XCCDF ActionEvent");
                ImportXCCDFDialog dialog = new ImportXCCDFDialog(MetaManager.getMainWindow(), true);
                dialog.setVisible(true);
            }
        });
        applicationMenu.add(appImportXCCDF);
        applicationMenu.addSeparator();
        appEdit = new JMenuItem("Edit Application...");
        appEdit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataManager dm = DataManager.instance();
                    RTDocument rtDoc = dm.getRTDocument();
                    RT rt = rtDoc.getRT();
                    EditApplicationDialog editApp = new EditApplicationDialog(MetaManager.getMainWindow(), true, MetaManager.getMainWindow().getApplicationBar().getApplication(), rt);
                    editApp.setVisible(true);
                } catch (Exception ex) {
                    logger.fatal(ex, ex);
                }
            }
        });
        applicationMenu.add(appEdit);
        appView = new JMenuItem("View Application...");
        appView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ApplicationType app = MetaManager.getMainWindow().getApplicationBar().getApplication();
                    File outputHtml = new ApplicationHelper().applyViewApplicationXsl(app);
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        URI uri = outputHtml.toURI();
                        logger.debug("Displaying in browser: " + uri.toString());
                        desktop.browse(uri);
                    } else {
                        logger.debug("Displaying via dialog");
                        String title = "View Recommendation: " + app.getName();
                        ViewHtmlDialog recDialog = new ViewHtmlDialog(MetaManager.getMainWindow(), true, title, outputHtml);
                        recDialog.setVisible(true);
                    }
                } catch (Exception ex) {
                    logger.fatal("Error while viewing an application.", ex);
                }
            }
        });
        applicationMenu.add(appView);
        appRemove = new JMenuItem("Remove Application...");
        appRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ApplicationType currentApp = MetaManager.getMainWindow().getApplicationBar().getApplication();
                if (currentApp != null) {
                    ApplicationRemoveDialog removeDialog = new ApplicationRemoveDialog(MetaManager.getMainWindow(), true, currentApp);
                }
            }
        });
        appRemove.setEnabled(false);
        applicationMenu.add(appRemove);
        applicationMenu.addSeparator();
        appGenXCCDF = new JMenuItem("Generate XCCDF...");
        appGenXCCDF.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Generate XCCDF ActionEvent");
                GenerateXCCDFDialog dialog = new GenerateXCCDFDialog(MetaManager.getMainWindow(), true, MetaManager.getMainWindow().getApplicationBar().getApplication());
                dialog.setVisible(true);
            }
        });
        applicationMenu.add(appGenXCCDF);
        adminMenu = new JMenu("Administration");
        userAdmin = new JMenuItem("Manage Users...");
        userAdmin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UserManageJDialog dialog = new UserManageJDialog(MetaManager.getMainWindow(), true);
                dialog.setVisible(true);
            }
        });
        adminMenu.add(userAdmin);
        adminMenu.addSeparator();
        rtEnums = new JMenuItem("Configure Defaults...");
        rtEnums.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("TODO - rtEnums: " + e.getActionCommand());
            }
        });
        rtEnums.setEnabled(false);
        adminMenu.add(rtEnums);
        serverMenu = new JMenu("Server");
        selectedApplications = new JMenuItem("Select Applications...");
        selectedApplications.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logger.debug("Selected Applications: " + e.getActionCommand());
                    RTServerProperties serverProps = RTServerProperties.instance();
                    boolean verified = serverProps.verifyProperties();
                    if (verified) {
                        SynchronizeClientDialog.displaySynchronizeDialog(true);
                        if (SynchronizeClientDialog.SYNC_COMPLETED == true) {
                            SynchronizationManager.instance().getApplications(serverProps.getDbHost());
                        }
                    } else {
                        GlobalUITools.displayWarningMessage(MetaManager.getMainWindow(), "Server Connection Error", "Unable to connect your RT Server. \nPlease verify that you are online and your server configuration settings are correct before trying again");
                    }
                } catch (DataManagerException ex) {
                    logger.warn(ex, ex);
                }
            }
        });
        selectedApplications.setEnabled(true);
        serverMenu.add(selectedApplications);
        serverMenu.addSeparator();
        sync = new JMenuItem("Synchronize...");
        sync.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SynchronizeClientDialog.displaySynchronizeDialog(false);
                } catch (DataManagerException ex) {
                    logger.warn(ex, ex);
                }
            }
        });
        serverMenu.add(sync);
        JPopupMenu serverPopup = serverMenu.getPopupMenu();
        serverPopup.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                sync.setEnabled(RTServerProperties.instance().isDbStandAlone() == false);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        revertLocalData = new JMenuItem("Revert Local Changes...");
        revertLocalData.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        revertLocalData.setEnabled(false);
        serverMenu.add(revertLocalData);
        serverMenu.addSeparator();
        configure = new JMenuItem("Configure...");
        configure.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RTServerProperties.instance().popUpAndCheckSettings();
            }
        });
        configure.setEnabled(true);
        serverMenu.add(configure);
        fileMenu = new JMenu("File");
        save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataManager.instance().saveWithWorkerDialog(true);
                } catch (DataManagerException ex) {
                    GlobalUITools.displayFatalExceptionMessage(null, "Error saving RTDocument", ex, false);
                }
            }
        });
        fileMenu.add(save);
        fileMenu.addSeparator();
        userProfile = new JMenuItem("My Account...");
        userProfile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                UserType user = MetaManager.getAuthenticatedUser();
                UserEditJDialog dialog = new UserEditJDialog(MetaManager.getMainWindow(), true, user);
                dialog.setVisible(true);
            }
        });
        fileMenu.add(userProfile);
        fileMenu.addSeparator();
        options = new JMenuItem("Options...");
        options.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RTConfigurationDialog dialog = new RTConfigurationDialog(MetaManager.getMainWindow(), true);
                dialog.setVisible(true);
            }
        });
        fileMenu.add(options);
        fileMenu.addSeparator();
        exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_Q);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("File: " + e.getActionCommand());
                MetaManager.getRtClient().shutdown(true);
            }
        });
        fileMenu.add(exit);
        helpMenu = new JMenu("Help");
        aboutRecommendationTracker = new JMenuItem("About...");
        aboutRecommendationTracker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("About: " + e.getActionCommand());
                AboutDialog dialog = new AboutDialog(MetaManager.getMainWindow(), true);
                dialog.setVisible(true);
            }
        });
        helpMenu.add(aboutRecommendationTracker);
        this.add(fileMenu);
        this.add(applicationMenu);
        this.add(serverMenu);
        this.add(adminMenu);
        this.add(helpMenu);
        this.restrictAdminOnlyItems();
        this.setApplication(null);
    }
