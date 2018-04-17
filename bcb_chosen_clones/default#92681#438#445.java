    private File chooseFile() {
        int option = fileChooser.showOpenDialog(getParent());
        if (option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }
