        private void jInit() throws Exception {
            this.setTitle(ejenVersion + " Installation");
            contentPanel = (JPanel) (this.getContentPane());
            contentPanel.setLayout(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JTextArea welcome = new JTextArea(javaRuntimeName + ", version " + javaVersion + ",\n" + osName + " " + osVersion + " (" + osArch + ").\n\n" + "Based on the file separator (" + fileSeparator + "), this is a " + (fileSeparator.equals("/") ? "Unix" : "Windows") + " system.\n" + "Your JDK installation directory is:");
            welcome.setEditable(false);
            welcome.setBackground(contentPanel.getBackground());
            welcome.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String actionCommand = e.getActionCommand();
                    if (actionCommand.equals("fileChooserButton")) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setDialogTitle("Choose your JDK installation directory");
                        if (chooser.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) jdkTextField.setText(chooser.getSelectedFile().getAbsolutePath());
                    } else if (actionCommand.equals("okButton")) {
                        install();
                    } else if (actionCommand.equals("cancelButton")) {
                        System.exit(0);
                    }
                }
            };
            JPanel jdkPanel = new JPanel(new BorderLayout());
            jdkTextField = new JTextField(strippedJavaHome);
            JButton fileChooserButton = new JButton("...");
            fileChooserButton.setActionCommand("fileChooserButton");
            fileChooserButton.addActionListener(actionListener);
            jdkPanel.add(jdkTextField, BorderLayout.CENTER);
            jdkPanel.add(fileChooserButton, BorderLayout.EAST);
            JPanel actionPanel = new JPanel(new BorderLayout());
            JTextArea label = new JTextArea("Check those values before installing.");
            label.setEditable(false);
            label.setBackground(contentPanel.getBackground());
            label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JButton okButton = new JButton("Install");
            okButton.setActionCommand("okButton");
            okButton.addActionListener(actionListener);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setActionCommand("cancelButton");
            cancelButton.addActionListener(actionListener);
            actionPanel.add(label, BorderLayout.NORTH);
            actionPanel.add(okButton, BorderLayout.CENTER);
            actionPanel.add(cancelButton, BorderLayout.EAST);
            contentPanel.add(welcome, BorderLayout.NORTH);
            contentPanel.add(jdkPanel, BorderLayout.CENTER);
            contentPanel.add(actionPanel, BorderLayout.SOUTH);
        }
