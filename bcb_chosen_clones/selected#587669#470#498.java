    protected void actionOpenFolder(ActionEvent evt) {
        log.debug("actionOpenFolder");
        if (!Desktop.isDesktopSupported()) {
            log.info("Open Folder is not supported by the OS.");
            MessageBox.ok(this, resErrorMessages.getString("MainWindow.File.Folder.CantOpenFolder"));
        }
        JFileChooser fc;
        fc = createJFileChooser();
        fc.setDialogTitle(resources.getString("Chooser.FileOpen.Title"));
        int ret = fc.showOpenDialog(this);
        switch(ret) {
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.APPROVE_OPTION:
                File file = fc.getSelectedFile();
                log.debug("actionOpenFolder:" + file.getAbsolutePath());
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    log.error(e);
                    MessageBox.okError(this, "Can't open Folder '" + file.getAbsolutePath() + "'\nError:" + e.getMessage());
                }
                break;
            case JFileChooser.ERROR_OPTION:
                break;
            default:
                log.warn("File Chooser ends with wrong Return Code. Code=" + ret);
        }
    }
