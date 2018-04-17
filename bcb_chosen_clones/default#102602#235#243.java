        public void actionPerformed(ActionEvent e) {
            dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int i = dirChooser.showOpenDialog(getMe());
            if (i == JFileChooser.APPROVE_OPTION) {
                importPathField.setText(dirChooser.getSelectedFile().toString());
            }
            load();
            selectAll();
        }
