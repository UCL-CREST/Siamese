    private File getFile() {
        boolean cancelado = false;
        String path = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(path);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            cancelado = true;
        }
        File fileName;
        fileName = fileChooser.getSelectedFile();
        if (((fileName == null) || (fileName.getName().equals(""))) && cancelado == false) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return fileName;
    }
