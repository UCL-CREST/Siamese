    void newFile_actionPerformed(java.awt.event.ActionEvent event) {
        JFileChooser chooser = new JFileChooser(currentDir);
        chooser.setDialogTitle("Insert New File");
        chooser.setFileSelectionMode(chooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            currentDir = chooser.getCurrentDirectory().getPath();
            File[] files = chooser.getSelectedFiles();
            if (files == null || files.length == 0) {
                files = new File[1];
                files[0] = chooser.getSelectedFile();
            }
            for (int i = 0; i < files.length; i++) {
                String filename = files[i].getPath();
                InstallFile fileObj = myConfig.CreateNewFile(filename);
                fileObj.permissions = 644;
                fileObj.destination = files[i].getName();
                fileObj.action = InstallFile.UPDATE;
                fileObj.type = InstallFile.ASCII;
                DefaultMutableTreeNode file = new DefaultMutableTreeNode(fileObj, false);
                DefaultTreeModel model = (DefaultTreeModel) ikTree.getModel();
                model.insertNodeInto(file, (MutableTreeNode) model.getChild(model.getRoot(), 0), 0);
            }
        }
    }
