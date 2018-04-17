    private void openSaveFileActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == openSaveFile) {
            int returnVal = fc.showOpenDialog(ParameterSolutions.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File savedFile = fc.getSelectedFile();
                log.append("Opening: " + savedFile.getName() + "\n");
                String name = savedFile.getName();
                if (!savedFile.canRead() || !((name.substring(name.length() - 4, name.length()).equals(".cpm"))) || !recoverSavedData(savedFile)) {
                    log.append("That is not a valid saved file, please choose" + " a file previously saved in this program.\n");
                }
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
