    private void handleButton() {
        File file;
        int option;
        option = fileChooser.showOpenDialog(dialogParent);
        if (option == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            setText(file.getPath());
        }
    }
