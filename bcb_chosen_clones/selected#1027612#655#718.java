    private boolean processFile() {
        if (this.url != null) {
            try {
                boolean login = true, forceDialog = false;
                while (login) {
                    try {
                        login = false;
                        if (!this.login(forceDialog)) {
                            JOptionPane.showMessageDialog(this.frame, LC.tr("The application was not able to login at the wiki"));
                            boolean openDialog = true;
                            boolean loggedin = false;
                            while (openDialog && (loggedin = this.login(true)) == false) {
                                openDialog = JOptionPane.showConfirmDialog(this.frame, LC.tr("re-enter Benutzername/Passwort?"), LC.tr("Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                            }
                            if (!loggedin) {
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        XExternalEditor._logger.error("", e);
                        JOptionPane.showMessageDialog(this.frame, e.getClass().getSimpleName() + ": " + e.getMessage(), LC.tr("Error at login"), JOptionPane.ERROR_MESSAGE);
                        if (JOptionPane.showConfirmDialog(this.frame, LC.tr("Do you wan't to change username, password and/or proxy?"), LC.tr("Change settings?"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            forceDialog = true;
                            login = true;
                        }
                    }
                }
                URL tempUrl = new URL(this.url);
                String sTitleDocumentName = new File(this.getFileName(tempUrl)).getName();
                JPanel jPane = (JPanel) this.frame.getContentPane();
                jPane.setBorder(BorderFactory.createTitledBorder(sTitleDocumentName));
                this.tmpFile = this.download(this.url);
                if (this.frame == null || !this.frame.isVisible()) {
                    return false;
                }
                if (Desktop.isDesktopSupported()) {
                    this.text.setText(LC.tr("Opening file in external program"));
                    this.listenOnChanges(this.tmpFile);
                    try {
                        Desktop.getDesktop().open(this.tmpFile);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this.frame, LC.tr("The file has no application registered to launch it with!\nPleaser register an application for the file extension '" + this.extension + "'."));
                    }
                    this.text.setText(LC.tr("Ready. You can now edit the file ..."));
                    this.bar.setVisible(false);
                    this.uploadBtn.setEnabled(!this.getUploadOnSaveButton().isSelected());
                    this.previewBtn.setEnabled(this.type.equalsIgnoreCase("edit text"));
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this.frame, LC.tr("JAVA Desktop functions are not allowed!\nI have to stop the program!"));
                }
            } catch (IOException e) {
                XExternalEditor._logger.error("", e);
                JOptionPane.showMessageDialog(this.frame, e.getClass().getSimpleName() + ": " + e.getMessage(), LC.tr("Error"), JOptionPane.ERROR_MESSAGE);
            } catch (NotSupportedException e) {
                XExternalEditor._logger.error("", e);
                JOptionPane.showMessageDialog(this.frame, e.getClass().getSimpleName() + ": " + e.getMessage(), LC.tr("Error"), JOptionPane.ERROR_MESSAGE);
            } catch (Throwable e) {
                XExternalEditor._logger.error("", e);
                JOptionPane.showMessageDialog(this.frame, e.getClass().getSimpleName() + ": " + e.getMessage(), LC.tr("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
