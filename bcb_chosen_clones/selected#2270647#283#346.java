    private void checkForUpdates(boolean silent) {
        QueryManager manager = null;
        if (QueryManagerFactory.isGlobalAvailable()) {
            manager = QueryManagerFactory.getGlobalManager();
        }
        if (manager == null) {
            if (QueryManagerFactory.isSyncAvailable()) {
                manager = QueryManagerFactory.getGlobalSyncManager();
            } else {
                log.warn(Messages.getString("GuiController.5"));
                if (!silent) {
                    showErrorDialog(Messages.getString("GuiController.6"));
                }
                return;
            }
        }
        if (manager == null) {
            log.error(Messages.getString("GuiController.7"));
            if (!silent) {
                showErrorDialog(Messages.getString("GuiController.7"));
            }
            return;
        }
        try {
            String globalVersion = manager.getOssobookVersion();
            try {
                double globalVersionNumeric = Double.parseDouble(globalVersion);
                double localVersionNumeric = Double.parseDouble(Configuration.config.getProperty("ossobook.version"));
                if (globalVersionNumeric > localVersionNumeric) {
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(ossobookFrame, String.format(Messages.getString("GuiController.11"), String.valueOf(globalVersion)), Messages.getString("GuiController.12"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                        String projectHomepage = Configuration.config.getProperty("ossobook.homepage");
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(new URI(projectHomepage));
                            } catch (IOException e) {
                                log.error(Messages.getString("GuiController.14"), e);
                                showWarnDialog(String.format(Messages.getString("GuiController.15"), projectHomepage));
                            } catch (URISyntaxException e) {
                                log.error(Messages.getString("GuiController.16"), e);
                                showErrorDialog(Messages.getString("GuiController.17"));
                            }
                        } else {
                            showWarnDialog(String.format(Messages.getString("GuiController.18"), projectHomepage));
                        }
                    }
                } else {
                    log.info(Messages.getString("GuiController.19"));
                    if (!silent) {
                        showNoticeDialog(Messages.getString("GuiController.20"));
                    }
                }
            } catch (NumberFormatException e) {
                log.error(Messages.getString("GuiController.21"));
                if (!silent) {
                    showErrorDialog(Messages.getString("GuiController.22"));
                }
            }
        } catch (StatementNotExecutedException e) {
            log.error(Messages.getString("GuiController.23"), e);
            if (!silent) {
                showErrorDialog(Messages.getString("GuiController.23"));
            }
        }
    }
