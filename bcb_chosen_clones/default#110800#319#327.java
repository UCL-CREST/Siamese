    private void findJar(java.awt.event.ActionEvent evt) {
        final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            File jar = fc.getSelectedFile();
            String jarFile = jar.toString();
            jarField.setText(jarFile);
        }
    }
