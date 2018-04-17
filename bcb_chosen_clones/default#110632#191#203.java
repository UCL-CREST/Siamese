    private File chooseFile(String title) {
        File selectedFile = null;
        JFileChooser fileUploadDialog;
        if (mLastDirectory.equals("")) fileUploadDialog = new JFileChooser("."); else fileUploadDialog = new JFileChooser(mLastDirectory);
        fileUploadDialog.setDialogTitle(title);
        int returnVal = fileUploadDialog.showOpenDialog(this);
        if (returnVal == 0) {
            File theFile = fileUploadDialog.getSelectedFile();
            mLastDirectory = theFile.getAbsolutePath();
            if (theFile != null) selectedFile = theFile;
        }
        return selectedFile;
    }
