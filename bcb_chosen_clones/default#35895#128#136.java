    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser pickFile = new JFileChooser();
        pickFile.setCurrentDirectory(new File(txtPreviewFolder.getText()));
        pickFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retVal = pickFile.showOpenDialog(super.getParent());
        if (retVal == JFileChooser.APPROVE_OPTION) {
            txtPreviewFolder.setText(pickFile.getSelectedFile().toString());
        }
    }
