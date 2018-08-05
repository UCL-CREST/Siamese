    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("browse")) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int val = chooser.showOpenDialog(frame.getContentPane());
            if (val == JFileChooser.APPROVE_OPTION) {
                webappField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        } else if (e.getActionCommand().equals("install")) {
            String user = userField.getText();
            String wapp = webappField.getText();
            String dir = homeField.getText();
            runInstall(new File(dir), wapp, user);
        }
    }
