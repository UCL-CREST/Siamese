    TypedFile askForSaveFile() {
        String directory = (lastDirectory != null) ? lastDirectory : (experimentMode ? experimentSaveDir : "data");
        JFileChooser fileChooser = new JFileChooser(directory);
        JPanel panel = new JPanel();
        panel.add(new JLabel("File type"));
        JComboBox fileType = new JComboBox();
        fileType.addItem("ASCII");
        fileType.addItem("Binary");
        panel.add(fileType);
        panel.setMaximumSize(panel.getPreferredSize());
        fileChooser.setAccessory(panel);
        int returnVal = fileChooser.showSaveDialog(statusWindow.getTopLevelAncestor());
        switch(returnVal) {
            case JFileChooser.APPROVE_OPTION:
                return new TypedFile(fileChooser.getSelectedFile(), (String) fileType.getSelectedItem());
            case JFileChooser.CANCEL_OPTION:
                message("Save cancelled", 10);
                break;
            default:
                System.err.println("Bogosity from JFileChooser");
                break;
        }
        return null;
    }
