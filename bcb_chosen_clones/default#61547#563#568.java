    private void classButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (m_fc3.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setJarText(m_fc3.getSelectedFile().getPath());
        }
        jarText.requestFocus();
    }
