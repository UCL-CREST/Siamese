    private JPanel buildFilePathSettings() {
        JPanel tp = new JPanel();
        JLabel jl = new JLabel("What is the path to the auctions save file:");
        tp.setBorder(BorderFactory.createTitledBorder("Save File Path"));
        tp.setLayout(new BorderLayout());
        filePath = new JTextField();
        filePath.addMouseListener(JPasteListener.getInstance());
        filePath.setToolTipText("Full path and filename to load auctions save file from.");
        updateValues();
        filePath.setEditable(true);
        filePath.getAccessibleContext().setAccessibleName("Full path and filename to load auctions save file from.");
        tp.add(jl, BorderLayout.NORTH);
        JPanel qp = new JPanel();
        JButton browseButton = new JButton("Browse...");
        qp.setLayout(new BoxLayout(qp, BoxLayout.Y_AXIS));
        qp.add(filePath);
        qp.add(browseButton);
        browseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (ae.getActionCommand().equals("Browse...")) {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setCurrentDirectory(new File(JConfig.getHomeDirectory("jbidwatcher")));
                    jfc.setApproveButtonText("Choose");
                    int rval = jfc.showOpenDialog(null);
                    if (rval == JFileChooser.APPROVE_OPTION) {
                        try {
                            filePath.setText(jfc.getSelectedFile().getCanonicalPath());
                        } catch (IOException ioe) {
                            filePath.setText(jfc.getSelectedFile().getAbsolutePath());
                        }
                    }
                }
            }
        });
        tp.add(qp, BorderLayout.SOUTH);
        return tp;
    }
