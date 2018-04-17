    void saveProject_actionPerformed(java.awt.event.ActionEvent event) {
        JFileChooser chooser = new JFileChooser("projects/");
        chooser.setFileFilter(new InstallKitFileFilter("ikp", "Install Kit Projects"));
        chooser.setFileSelectionMode(chooser.FILES_ONLY);
        chooser.setDialogTitle("Save Project");
        int ret = chooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = chooser.getSelectedFile().getPath();
                currentProject = filename;
                comittProperties();
                myConfig.save(filename);
            } catch (IOException ioe) {
                try {
                    MessageBox mb = new MessageBox();
                    mb.label.setText("There was an error while saving the project: " + ioe);
                    mb.show();
                } catch (Exception e) {
                }
            }
        }
    }
