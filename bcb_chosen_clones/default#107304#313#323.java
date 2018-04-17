    private void SelectFileActionPerformed(ActionEvent evt) {
        System.out.println("SelectFile.actionPerformed, event=" + evt);
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showOpenDialog(new JPanel());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            FileFilter.setText(chooser.getSelectedFile().getAbsolutePath());
            FolderName.setText(chooser.getSelectedFile().getParentFile().getAbsolutePath());
        }
    }
