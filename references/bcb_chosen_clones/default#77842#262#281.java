            public void actionPerformed(ActionEvent e) {
                Thread runner = new Thread() {

                    public void run() {
                        if (my_chooser.showOpenDialog(GhinWebit.this) != JFileChooser.APPROVE_OPTION) return;
                        GhinWebit.this.repaint();
                        my_FChoosen = my_chooser.getSelectedFile();
                        my_OpenFile = my_chooser.getSelectedFile().toString();
                        sourceHTML(my_OpenFile);
                        toolbar.button[9].setVisible(false);
                        toolbar.button[7].setVisible(true);
                        int newIndex1 = top.getChildCount();
                        if (my_FChoosen.getAbsolutePath() != sourceDir.getAbsolutePath()) myTreeModel.insertNodeInto(new DefaultMutableTreeNode(new IconData(ICON_FILES, null, my_FChoosen.toString())), top, newIndex1); else myTreeModel.insertNodeInto(new DefaultMutableTreeNode(new IconData(ICON_FILES, null, my_FChoosen.getName())), top, newIndex1);
                        myTree.expandRow(newIndex1);
                        myTree.setSelectionRow(newIndex1 + 1);
                        myTree.repaint();
                    }
                };
                runner.start();
            }
