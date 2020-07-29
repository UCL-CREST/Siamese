    public JPanel buildRootPanel() {
        rootModel = new DefaultListModel();
        for (String s : project.getRoots()) {
            rootModel.addElement(s);
        }
        rootList = new JList(rootModel);
        rootList.setVisibleRowCount(6);
        rootList.setFont(STANDARD_FONT);
        JButton addRoot = new JButton("Add");
        addRoot.setAlignmentX(Component.CENTER_ALIGNMENT);
        addRoot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int rval = chooser.showOpenDialog(((JComponent) e.getSource()).getTopLevelAncestor());
                if (rval == JFileChooser.APPROVE_OPTION) {
                    rootModel.addElement(chooser.getSelectedFile().getAbsolutePath());
                    refreshFileListPreview();
                }
            }
        });
        JButton addCustomRoot = new JButton("Add Custom");
        addCustomRoot.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCustomRoot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String path = JOptionPane.showInputDialog(((JComponent) e.getSource()).getTopLevelAncestor(), "Enter a path:");
                if (path != null) {
                    rootModel.addElement(path);
                    refreshFileListPreview();
                }
            }
        });
        JButton removeRoot = new JButton("Remove");
        removeRoot.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeRoot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (rootList.getSelectedValue() != null) {
                    rootModel.remove(rootList.getSelectedIndex());
                    refreshFileListPreview();
                }
            }
        });
        JPanel rootButtonPanel = new JPanel();
        rootButtonPanel.setLayout(new BoxLayout(rootButtonPanel, BoxLayout.PAGE_AXIS));
        rootButtonPanel.add(addRoot);
        rootButtonPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        rootButtonPanel.add(addCustomRoot);
        rootButtonPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        rootButtonPanel.add(removeRoot);
        rootButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JPanel rootPanel = new JPanel();
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(new JLabel("Root folders:"), BorderLayout.NORTH);
        rootPanel.add(new JScrollPane(rootList), BorderLayout.CENTER);
        rootPanel.add(rootButtonPanel, BorderLayout.EAST);
        return rootPanel;
    }
