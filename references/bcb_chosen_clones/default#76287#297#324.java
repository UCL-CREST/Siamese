    private int browseExport() {
        JFileChooser chooser = getFileChooser();
        chooser.setDialogTitle("Choose System file");
        chooser.setApproveButtonText("Open");
        chooser.setApproveButtonToolTipText("Open the selected file.");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(systemFilter);
        int result = chooser.showOpenDialog(getProjectFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().getPath();
            if (!file.startsWith(srcFolder)) {
                JOptionPane.showMessageDialog(projectFrame, "The system file must reside within the source directory.\n\n" + "Please set the make sure that the given source directory\n" + "is correct, and that the system file resides beneath it.");
                return JFileChooser.CANCEL_OPTION;
            }
            getExportTextField().setText(file);
            project = new Project(file, srcFolder);
            if (project == null) {
                JOptionPane.showMessageDialog(getProjectFrame(), "Error loading system file.");
                hideTree();
            } else {
                showTree(getProjectName(file));
            }
        } else {
            project = null;
            hideTree();
        }
        return result;
    }
