    private void createProject() {
        JFileChooser chooser = getFileChooser();
        chooser.setDialogTitle("Choose export directory");
        chooser.setApproveButtonText("Open");
        chooser.setApproveButtonToolTipText("Open the selected directory.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileFilter(null);
        if (chooser.showOpenDialog(getProjectFrame()) == JFileChooser.APPROVE_OPTION) {
            String file = getExportTextField().getText();
            project = new Project(file, srcFolder);
            if (project == null) {
                JOptionPane.showMessageDialog(getProjectFrame(), "Error creating project");
                hideTree();
            } else {
                String path = chooser.getSelectedFile().getPath();
                boolean jar = getCreateJar().isSelected();
                if (!jar) {
                    File f = new File(path + File.separator + "config.prj");
                    if (f.exists()) {
                        int result = JOptionPane.showConfirmDialog(projectFrame, "A project already exists in this directory, overwrite?");
                        if (result == JOptionPane.NO_OPTION) {
                            createProject();
                            return;
                        } else if (result == JOptionPane.CANCEL_OPTION) return;
                    }
                }
                project.exportFiles(path, jar);
                JOptionPane.showMessageDialog(getProjectFrame(), "Project successfully created:\n" + project.getLastSavedLocation());
                showTree(getProjectName(file));
            }
        }
    }
