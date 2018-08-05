    public String OpenFile() {
        filter1 = new ExtensionFileFilter("Microsoft Office Access (*.mdb)", "mdb");
        fc = new JFileChooser();
        fc.setFileFilter(filter1);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file.getPath();
        } else {
            return null;
        }
    }
