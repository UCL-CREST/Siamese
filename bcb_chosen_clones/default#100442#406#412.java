        public void actionPerformed(ActionEvent e) {
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = dirChooser.showOpenDialog(gimme());
            if (i == JFileChooser.APPROVE_OPTION) {
                generellPathField.setText(dirChooser.getSelectedFile().toString());
            }
        }
