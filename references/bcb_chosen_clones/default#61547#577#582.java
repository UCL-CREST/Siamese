    private void dirButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (m_fc1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setDirText(m_fc1.getSelectedFile().getAbsolutePath());
        }
        dirText.requestFocus();
    }
