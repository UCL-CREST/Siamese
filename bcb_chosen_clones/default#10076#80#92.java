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
