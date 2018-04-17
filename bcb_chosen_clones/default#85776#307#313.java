        public void actionPerformed(ActionEvent e) {
            dirChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int i = dirChooser.showOpenDialog(getMe());
            if (i == JFileChooser.APPROVE_OPTION) {
                if (dirChooser.getSelectedFile().toString().toLowerCase().endsWith(".pls")) exportPathField.setText(dirChooser.getSelectedFile().toString()); else if (dirChooser.getSelectedFile().toString().toLowerCase().endsWith(".m3u")) exportPathField.setText(dirChooser.getSelectedFile().toString()); else exportPathField.setText(dirChooser.getSelectedFile().toString() + ".pls");
            }
        }
