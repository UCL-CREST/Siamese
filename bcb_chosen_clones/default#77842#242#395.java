    protected JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        JMenu mFile = new JMenu("File");
        mFile.setMnemonic('f');
        JMenuItem item = new JMenuItem("New");
        item.setMnemonic('n');
        ActionListener lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                my_OpenFile = sourceDir.getAbsolutePath() + File.separator + "Default.html";
                goEditHTML(my_OpenFile);
                isfilesource = false;
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        item = new JMenuItem("Open...");
        item.setMnemonic('o');
        lst = new ActionListener() {

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
        };
        item.addActionListener(lst);
        mFile.add(item);
        item = new JMenuItem("Save As...");
        item.setMnemonic('A');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GhinWebit.this.repaint();
                Thread runner = new Thread() {

                    public void run() {
                        saveFile();
                    }
                };
                runner.start();
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        mFile.addSeparator();
        item = new JMenuItem("View HTML...");
        item.setMnemonic('H');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread runner = new Thread() {

                    public void run() {
                        try {
                            goViewHTML(my_OpenFile);
                        } catch (Exception ex) {
                            warnme("check the HTML file ");
                        }
                    }
                };
                runner.start();
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        item = new JMenuItem("Edit HTML...");
        item.setMnemonic('H');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread runner = new Thread() {

                    public void run() {
                        try {
                            goEditHTML(my_OpenFile);
                        } catch (Exception ex) {
                            warnme("check the HTML file ");
                        }
                    }
                };
                runner.start();
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        mFile.addSeparator();
        item = new JMenuItem("View Source...");
        item.setMnemonic('r');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread runner = new Thread() {

                    public void run() {
                        sourceHTML(my_OpenFile.toString());
                    }
                };
                runner.start();
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        item = new JMenuItem("About Ghinwebit 1.0");
        item.setMnemonic('A');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("About Ghinwebit 1.0");
                ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("logo.gif"));
                JLabel label1 = new JLabel(icon);
                frame.add("North", label1);
                JLabel label2 = new JLabel("<html><li>Ghinwebit 1.0� " + "</li><li><p>Ver# 1.0 </li>" + "<li><p>Develop by: Goen-Ghin</li><li><p>JavaGeo Technology System</li><li>" + "<p>Copyright<font size=\"2\">�</font> June 2008 @Pekanbaru Riau Indonesia</li></html>");
                label2.setFont(new Font("Tahoma", Font.PLAIN, 11));
                frame.add(label2);
                Toolkit kit = Toolkit.getDefaultToolkit();
                Image image = kit.getImage(ClassLoader.getSystemResource("logo.gif"));
                frame.setIconImage(image);
                java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                frame.setSize(new java.awt.Dimension(240, 150));
                frame.setLocation((screenSize.width - 240) / 2, (screenSize.height - 240) / 2);
                frame.setVisible(true);
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        item = new JMenuItem("Exit");
        item.setMnemonic('x');
        lst = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        item.addActionListener(lst);
        mFile.add(item);
        menuBar.add(mFile);
        return menuBar;
    }
