    private void BtnOpenDestinyActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser file = new JFileChooser();
        file.setDialogTitle("Save File");
        file.setMultiSelectionEnabled(false);
        int res = file.showSaveDialog(this);
        if (res == file.APPROVE_OPTION) {
            rootD = file.getSelectedFile().getAbsolutePath();
            rutaDestino.setText(rootD);
            isDestiny = true;
        }
    }
