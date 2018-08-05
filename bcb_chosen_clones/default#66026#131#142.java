    private boolean chooseDirectory() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        int returnval = fc.showOpenDialog(this);
        if (returnval == JFileChooser.APPROVE_OPTION) {
            directory = fc.getSelectedFile();
            btnOk.setEnabled(true);
            return true;
        }
        return false;
    }
