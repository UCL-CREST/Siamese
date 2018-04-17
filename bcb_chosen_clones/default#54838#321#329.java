    private void findTheme(java.awt.event.ActionEvent evt) {
        final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            File theme = fc.getSelectedFile();
            themePack = theme.toString();
            themeField.setText(themePack);
        }
    }
