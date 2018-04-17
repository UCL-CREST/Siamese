    public boolean save() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to continue?", "Overwrit confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) return false;
            }
            String fileName = file.getName();
            if (fileName != null) {
                int extensionIndex = fileName.lastIndexOf('.');
                setTitle(fileName.substring(0, extensionIndex));
            }
            return writeFile(file, field.getText());
        }
        return false;
    }
