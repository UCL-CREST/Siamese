    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser pickFile = new JFileChooser();
        pickFile.setCurrentDirectory(new File(txtFlam3Folder.getText()));
        pickFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = pickFile.showOpenDialog(super.getParent());
        if (retVal == JFileChooser.APPROVE_OPTION) {
            if (Utils.isValidFlam3Folder(pickFile.getSelectedFile().toString())) {
                txtFlam3Folder.setText(pickFile.getSelectedFile().toString());
            } else {
                JOptionPane.showMessageDialog(parent, "FLAM3 folder is missing essential files, please make sure the folder contains all FLAM3 files.", "Missing FLAM3 files", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
