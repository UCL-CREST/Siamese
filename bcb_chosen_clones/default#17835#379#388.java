    private void browse() {
        JFileChooser chooser = new JFileChooser(buildField.getText().trim());
        chooser.addChoosableFileFilter(new AntFileFilter());
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            setBuildFile(file);
            loadBuildFile();
        }
    }
