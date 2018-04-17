    void openFileChooser() {
        String where = destinationPath.getText();
        final JFileChooser fc = new JFileChooser(where);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            where = fc.getSelectedFile().getAbsolutePath();
            destinationPath.setText(where);
            ApplicationProperties.PROP.setProperty("moveToFolder", where);
        }
    }
