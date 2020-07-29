    public InitializerGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        sourceType = new JComboBox(new String[] { "TFRI Web Archive", "RBNB Source", "Single Image" });
        controls.add(new JLabel("Source Type: "));
        controls.add(sourceType);
        archiveControls = new JPanel();
        archiveUrl = new JTextField("http://");
        archiveStartDateTime = new JSpinner(new SpinnerDateModel());
        archiveEndDateTime = new JSpinner(new SpinnerDateModel());
        archiveControls.add(archiveUrl);
        archiveControls.add(archiveStartDateTime);
        archiveControls.add(archiveEndDateTime);
        fileControls = new JPanel();
        fileChooser.setMultiSelectionEnabled(true);
        fileBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(fileChooser);
                for (File file : fileChooser.getSelectedFiles()) System.err.println(file.getName());
            }
        });
        fileControls.add(fileBrowse);
        subControls.add(fileControls);
        controls.add(subControls);
        destinationType = new JComboBox(new String[] { "Console", "RBNB" });
        controls.add(new JLabel("Destination Type: "));
        controls.add(destinationType);
        apply = new JButton("Apply");
        controls.add(apply);
        frame.getContentPane().add(controls);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
