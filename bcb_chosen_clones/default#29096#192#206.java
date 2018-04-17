    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser pickFile = new JFileChooser();
        pickFile.setCurrentDirectory(new File(txtFolder.getText()));
        pickFile.setFileFilter(new LogFilter());
        pickFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = pickFile.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File seq = pickFile.getSelectedFile();
            if (!(new LogFilter().accept(seq))) {
                seq = new File(seq.toString() + ".fal");
            }
            txtFolder.setText(seq.toString());
            Debugger.liveOutputFile = seq.toString();
        }
    }
