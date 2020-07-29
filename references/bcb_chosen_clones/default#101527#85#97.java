    protected void chooseFile() {
        int approveValue = fileChooser.showSaveDialog(null);
        File selectedFile;
        if (approveValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            if ((!getExtension(selectedFile).equals("html")) && (!getExtension(selectedFile).equals("htm"))) {
                selectedFile = (new File(selectedFile.getPath() + ".htm"));
            }
            export(selectedFile);
        } else {
            endExport();
        }
    }
