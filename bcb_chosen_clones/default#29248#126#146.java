    private void onBrowseButton() {
        JFileChooser chooser = null;
        if (inputFileName.getText().length() > 0) {
            File currentFile = new File(inputFileName.getText());
            if (currentFile.exists()) {
                chooser = new JFileChooser(currentFile);
            }
        }
        if (chooser == null) {
            if (MailSorterFrame.getCurrentFile() == null) {
                chooser = new JFileChooser();
            } else {
                chooser = new JFileChooser(MailSorterFrame.getCurrentFile());
            }
        }
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            inputFileName.setText(selectedFile.getAbsolutePath());
        }
    }
