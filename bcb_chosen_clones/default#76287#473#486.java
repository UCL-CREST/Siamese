    private void browseSrc() {
        JFileChooser chooser = getFileChooser();
        chooser.setDialogTitle("Choose source directory");
        chooser.setApproveButtonText("Open");
        chooser.setApproveButtonToolTipText("Open the selected directory.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setSelectedFile(new File(srcFolder));
        chooser.setFileFilter(null);
        if (chooser.showOpenDialog(getProjectFrame()) == JFileChooser.APPROVE_OPTION) {
            srcFolder = chooser.getSelectedFile().getPath();
            srcLabel.setText("Source: " + srcFolder);
            srcLabel.setToolTipText(srcFolder);
        }
    }
