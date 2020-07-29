    private void openFile() {
        JFileChooser filech = new JFileChooser();
        String theLine = "";
        filech.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = filech.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File filename = filech.getSelectedFile();
        if (filename == null || filename.getName().equals("")) JOptionPane.showMessageDialog(this, "Invalid File Name", "Error", JOptionPane.ERROR_MESSAGE); else {
            tfield01.setText(filename.getPath());
        }
    }
