    public void initialize() {
        this.setExpandsSelectedPaths(true);
        File roots[] = File.listRoots();
        rootValue = new String("c:\\");
        File rootDir = new File("c:\\");
        if (!rootDir.exists()) {
            rootValue = new String("/");
            rootDir = new File("/");
            if (!rootDir.exists()) {
                System.out.println("Can't understand file system");
                System.out.println("Root is neither c:\\ nor / ");
                System.exit(-1);
            }
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(rootValue);
        DefaultMutableTreeNode subDir = null;
        DefaultMutableTreeNode dirEntry = null;
        File[] contents = rootDir.listFiles();
        java.util.Arrays.sort(contents);
        for (int x = 0; x < contents.length; x++) {
            if (contents[x].isDirectory()) {
                if (myParent.showHiddenFiles == false && contents[x].isHidden()) continue;
                subDir = new DefaultMutableTreeNode(contents[x].getName());
                subDir.setAllowsChildren(true);
                top.add(subDir);
            }
        }
        DefaultTreeModel myModel = new DefaultTreeModel(top, true);
        setModel(myModel);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        pum = new JPopupMenu();
        JMenuItem item1 = new JMenuItem("Add directory");
        item1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String newDir = javax.swing.JOptionPane.showInputDialog(myParent, new String("Directory to add to \n " + myParent.currentDir + java.io.File.separator), "Add directory", javax.swing.JOptionPane.QUESTION_MESSAGE);
                if (newDir == null) return;
                newDir = myParent.currentDir + java.io.File.separator + newDir;
                File dir = new File(newDir);
                boolean success = dir.mkdir();
                if (!success) {
                    JOptionPane.showMessageDialog(myParent, new String("Failed to create " + newDir), "Warning", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                initialize();
                myParent.scrollToSelectedDir();
            }
        });
        pum.add(item1);
        JMenuItem item2 = new JMenuItem("Delete directory");
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(myParent, new String("Do you really want to delete:\n" + myParent.currentDir), "Confirm delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer != JOptionPane.YES_OPTION) return;
                File dir = new File(myParent.currentDir);
                File contents[] = dir.listFiles();
                if (contents.length > 0) {
                    JOptionPane.showMessageDialog(myParent, new String("Can not delete " + dir.getName() + "\n" + "It is not empty."), "Warning", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                dir.delete();
                initialize();
                String tempString = myParent.currentDir;
                int location = tempString.lastIndexOf(File.separator);
                myParent.directoryChanged(tempString.substring(0, location - 1));
                myParent.scrollToSelectedDir();
            }
        });
        pum.add(item2);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.BUTTON3_MASK == e.getModifiers()) {
                    pum.show(e.getComponent(), e.getX(), e.getY());
                    return;
                }
            }
        });
    }
