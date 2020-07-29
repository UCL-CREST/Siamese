    void openProject_actionPerformed(java.awt.event.ActionEvent event) {
        JFileChooser chooser = new JFileChooser("projects/");
        chooser.setFileFilter(new InstallKitFileFilter("ikp", "Install Kit Projects"));
        chooser.setFileSelectionMode(chooser.FILES_ONLY);
        chooser.setDialogTitle("Open Project");
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getPath();
            openProject(filename);
        }
    }
