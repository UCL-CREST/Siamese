    private void BtnOpenFileActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser file = new JFileChooser();
        file.setDialogTitle("Open File");
        file.setMultiSelectionEnabled(false);
        int res = file.showOpenDialog(this);
        if (res == file.APPROVE_OPTION) {
            rootS = file.getSelectedFile().getAbsolutePath();
            rutaOrigen.setText(rootS);
            isSource = true;
        }
    }
