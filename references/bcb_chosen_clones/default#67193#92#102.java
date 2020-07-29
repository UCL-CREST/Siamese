    public void OpenFile() {
        if (SaveIfDirty("Opening File")) {
            int rc = fc.showOpenDialog(parent);
            if (rc == JFileChooser.APPROVE_OPTION) {
                document = fc.getSelectedFile();
                System.out.println("I should be opening " + document.getPath());
                setupTree(document.getPath());
                setDirty(false, "opened new file");
            }
        }
    }
