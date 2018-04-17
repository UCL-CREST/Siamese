    void saveFile() {
        if (my_chooser.showSaveDialog(GhinWebit.this) != JFileChooser.APPROVE_OPTION) return;
        GhinWebit.this.repaint();
        File fChoosen = my_chooser.getSelectedFile();
        if (isfilesource == true) {
            saveAsCODE(fChoosen.getAbsolutePath());
        } else if (isfilesource == false) {
            saveAsHTML(fChoosen.getAbsolutePath());
        }
        int newIndex1 = top.getChildCount();
        myTreeModel.insertNodeInto(new DefaultMutableTreeNode(new IconData(ICON_FILES, null, fChoosen.getAbsolutePath())), top, newIndex1);
        myTree.expandRow(newIndex1);
        myTree.setSelectionRow(newIndex1 + 1);
        myTree.repaint();
        my_chooser.rescanCurrentDirectory();
    }
