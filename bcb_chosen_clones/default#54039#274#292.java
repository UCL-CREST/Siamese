    private void saveFileActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == saveFile) {
            int returnVal = fc.showSaveDialog(ParameterSolutions.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File userFile = fc.getSelectedFile();
                String path = userFile.getPath();
                if (!((path.substring(path.length() - 4, path.length())).equals(".cpm"))) {
                    userFile = new File(userFile.getPath() + ".cpm");
                }
                log.append("Saving to: " + userFile.getName() + "\n");
                narr.println("Saving to: " + userFile.getName());
                if (!saveFile(userFile)) {
                    log.append("Please run up to hillclimbing before  saving to a file.\n");
                }
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
