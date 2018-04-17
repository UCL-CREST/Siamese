    private int browseImport() {
        JFileChooser chooser = getFileChooser();
        chooser.setDialogTitle("Import Project");
        chooser.setApproveButtonText("Import");
        chooser.setApproveButtonToolTipText("Import the selected project file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(projectJarFilter);
        chooser.setFileFilter(projectFilter);
        int result = chooser.showOpenDialog(getProjectFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().getPath();
            getImportTextField().setText(file);
            project = Project.fromFile(file);
            if (project == null) {
                JOptionPane.showMessageDialog(getProjectFrame(), "Error loading project file.");
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
