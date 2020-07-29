    private void classButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (m_fc2.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            setClassText(m_fc2.getSelectedFile().getPath());
        }
        classText.requestFocus();
    }
