            @Override
            protected void done() {
                logger.debug("checkForUpdates: done");
                if (checkForUpdatesDialog != null && checkForUpdatesDialog.isVisible()) {
                    checkForUpdatesDialog.dispose();
                }
                if (!isCancelled()) {
                    try {
                        newerVersion = get();
                        if (newerVersion != null && newerVersion.compareTo(currentVersion) == 1) {
                            logger.debug("checkForUpdates: currentVersion = {}", currentVersion);
                            logger.debug("checkForUpdates: newerVersion = {}", newerVersion);
                            logger.debug("SoftwareUpdate::checkForUpdates: updates found");
                            if (showDialogs) {
                                Object[] options = { LanguageBundle.getString("Button.Cancel") };
                                Desktop desktop = null;
                                if (Desktop.isDesktopSupported()) {
                                    desktop = Desktop.getDesktop();
                                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                        options = new Object[] { LanguageBundle.getString("Button.Download"), LanguageBundle.getString("Button.Cancel") };
                                    }
                                }
                                JPanel panel = new JPanel(new BorderLayout(0, 10));
                                panel.add(new JLabel("<html>" + LanguageBundle.getString("Dialog.SoftwareUpdate.UpdatesFound") + " (" + newerVersion + ")</html>"), BorderLayout.CENTER);
                                JCheckBox hideCheckBox = null;
                                if (Settings.isAutomaticCheckForUpdatesEnabled() && showDisableOption) {
                                    hideCheckBox = new JCheckBox(LanguageBundle.getString("Dialog.SoftwareUpdate.DisableAutomaticCheckForUpdates"));
                                    panel.add(hideCheckBox, BorderLayout.SOUTH);
                                }
                                int n = JOptionPane.showOptionDialog(XtremeMP.getInstance().getMainFrame(), panel, LanguageBundle.getString("Dialog.SoftwareUpdate"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                if (hideCheckBox != null) {
                                    Settings.setAutomaticCheckForUpdatesEnabled(!hideCheckBox.isSelected());
                                }
                                if ((options.length == 2) && (n == 0)) {
                                    try {
                                        URL url = new URL(newerVersion.getDownloadURL());
                                        desktop.browse(url.toURI());
                                    } catch (Exception ex) {
                                        logger.error(ex.getMessage(), ex);
                                    }
                                }
                            }
                        } else {
                            logger.debug("checkForUpdates: no updates found");
                            if (showDialogs && !showDisableOption) {
                                JOptionPane.showMessageDialog(XtremeMP.getInstance().getMainFrame(), LanguageBundle.getString("Dialog.SoftwareUpdate.NoUpdatesFound"), LanguageBundle.getString("Dialog.SoftwareUpdate"), JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
            }
