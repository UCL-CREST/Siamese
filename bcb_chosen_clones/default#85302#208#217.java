    protected void browseFile(JTextField dest) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (f != null) dest.setText(f.getPath());
        }
    }
