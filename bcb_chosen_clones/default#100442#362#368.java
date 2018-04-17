        public void actionPerformed(ActionEvent e) {
            dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int i = dirChooser.showOpenDialog(gimme());
            if (i == JFileChooser.APPROVE_OPTION) {
                webBrowserField.setText(dirChooser.getSelectedFile().toString());
            }
        }
